package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.rareventure.quietcraft.utils.BlockArea;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Contains standard procedures for handling interaction with the system state and database
 */
public class PlayerManager {
    private static final int SOULS_PER_REBIRTH = 1;
    private static final Material SOUL_MATERIAL_TYPE = Material.DIAMOND;
    private static final String SOUL_DISPLAY_NAME = "Soul Gem";

    //TODO 2 nether care pack
    //TODO 2 sound travels everywhere in nether
    //TODO 2 vw that cannot be visited (all players dead or moved to other qcWorlds)
    //  are recycled immediately (to prevent a griefer from spawn trapping all qcWorlds)

    //TODO 2 no more wild portals
    //TODO 2 if player dies x number of times in 24 hours, they nether spawn for awhile

    //TODO 2, maybe all portal keys to work as is
    //TODO 2.5 make saying 'hello sailor' cause the person to be immediately transported to the nether
    //with no souls
    private static final List<String> SOUL_LORE = Arrays.asList(
            ("This is a soul gem. It's very valuable, if you like living in this world.").split("\n"));

    private static final Material PORTAL_KEY_MATERIAL = Material.FLINT_AND_STEEL;

    private static final List<String> PORTAL_KEY_LORE = Arrays.asList(
            ("This flint of steel seems magical somehow...").split("\n"));
    public static final String PORTAL_KEY_DISPLAY_NAME_ENDING = " portal key";

    private final QuietCraftPlugin qcp;

    private EbeanServer db;

    private Map<String,QCPlayer> uuidToQCPlayerCache = new HashMap<>();

    public PlayerManager(QuietCraftPlugin qcp)
    {
        this.db = qcp.db;
        this.qcp = qcp;

        db.find(QCPlayer.class).findList().forEach(p -> uuidToQCPlayerCache.put(p.getUuid(),p));
    }

    private void addToPlayerLog(QCPlayer qcPlayer, QCPlayerLog.Action action)
    {
        QCWorld w = qcPlayer.getWorld();
        db.insert(new QCPlayerLog(qcPlayer.getUuid(),new Date(),
                w.getId(), w.getRecycleCounter(),
                action));
    }

    public void onPlayerDeath(Player p, PlayerDeathEvent e) {
        EbeanServer db = QuietCraftPlugin.db;

        if(WorldUtil.isNetherWorld(p.getWorld().getName())) {
            //in the nether, souls aren't removed from inventory (are collectable), and the player
            //loses all their souls
            QCPlayer qcPlayer = getQCPlayer(p.getUniqueId().toString());

            qcPlayer.setSoulsKeptDuringDeath(0);

            db.update(qcPlayer);
            return;
        }


        int soulCount = getSoulCount(p);

        QCPlayer qcPlayer = getQCPlayer(p.getUniqueId().toString());

        qcPlayer.setSoulsKeptDuringDeath(soulCount);

        db.update(qcPlayer);

        //remove the souls from inventory, so they don't spray out everywhere allowing someone else
        //to collect them
        for(Iterator<ItemStack> isi = e.getDrops().listIterator(); isi.hasNext();)
        {
            ItemStack is = isi.next();
            if(is.getType().equals(SOUL_MATERIAL_TYPE)
                    && is.getItemMeta().getDisplayName().equals(SOUL_DISPLAY_NAME))
                isi.remove();
        }

        debugPrintPlayerInfo("onPlayerDeath",p);
    }

    private QCPlayer getQCPlayer(String uniqueId) {
        synchronized (uuidToQCPlayerCache)
        {
            return uuidToQCPlayerCache.get(uniqueId);
        }
    }

    private int getSoulCount(Player p) {
        int souls = 0;

        for(ItemStack itemStack : p.getInventory())
        {
            if(itemStack == null)
                continue;
            if(itemStack.getType() == SOUL_MATERIAL_TYPE) {
                ItemMeta im = itemStack.getItemMeta();
                if(isSoulMeta(im))
                    souls += itemStack.getAmount();
            }
        }

        return souls;
    }

    private boolean isSoulMeta(ItemMeta im) {
        return im.getDisplayName().equals(SOUL_DISPLAY_NAME);
    }

    public QCPlayer getQCPlayer(Player player) {
        return getQCPlayer(player.getUniqueId().toString());
    }

    public QCPlayer createQCPlayer(Player player, QCWorld w) {
        QCPlayer qcPlayer = new QCPlayer(player.getUniqueId().toString(), w, SOULS_PER_REBIRTH);
        db.insert(qcPlayer);

        synchronized (uuidToQCPlayerCache)
        {
            uuidToQCPlayerCache.put(player.getUniqueId().toString(),qcPlayer);
        }

        return qcPlayer;
    }


    public void onPlayerJoin(Player player) {
        QCPlayer qcPlayer = getQCPlayer(player);

        db.beginTransaction();
        try {
            //new player
            if(qcPlayer == null)
            {
                QCWorld w = qcp.wm.findBestActiveWorldForPlayer(player.getUniqueId().toString());
                qcPlayer = createQCPlayer(player,w);

                player.sendMessage("You have been born in world '"+w.getName()+"'");

                Location spawnLocation = Bukkit.getWorld(w.getName()).getSpawnLocation();
                player.teleport(spawnLocation);
                giveInitialPackageToPlayer(player, player.getWorld(), SOULS_PER_REBIRTH, true);

                addToPlayerLog(qcPlayer, QCPlayerLog.Action.JOIN);
            }
            else {
                addToPlayerLog(qcPlayer, QCPlayerLog.Action.JOIN);

                player.sendMessage("You are in world '" +
                        player.getWorld().getName() + "'");
            }
            db.commitTransaction();
        }
        finally {
            db.endTransaction();
        }

        debugPrintPlayerInfo("onPlayerJoin",player);
    }

    private void debugPrintPlayerInfo(String message, Player player) {
        QCPlayer p = getQCPlayer(player);
        Bukkit.getLogger().info(message+", "+p);
    }

    /**
     * Gives the player their initial items when they die or join a visited world
     * @param soulCount souls remaining
     * @param isFirstAppearance true if this is the first time the player has appeared in
     *                          this visited world
     */
    private void giveInitialPackageToPlayer(Player player, World world, int soulCount, boolean isFirstAppearance) {
        Inventory i = player.getInventory();
        i.clear();

        if(isFirstAppearance)
        {
            ItemStack is = new ItemStack(PORTAL_KEY_MATERIAL);
            ItemMeta im = is.getItemMeta();

            im.setDisplayName(world.getName()+PORTAL_KEY_DISPLAY_NAME_ENDING);
            im.setLore(PORTAL_KEY_LORE);

            is.setItemMeta(im);
            i.addItem(is);
        }

        //TODO 3 choose a better material type for souls
        while(soulCount != 0) {
            //in case the user has a lot of souls, we create a max stack of 64
            int sc = soulCount%64;
            ItemStack is = new ItemStack(SOUL_MATERIAL_TYPE, sc);
            ItemMeta im = is.getItemMeta();

            im.setDisplayName(SOUL_DISPLAY_NAME);
            im.setLore(SOUL_LORE);
            is.setItemMeta(im);
            i.addItem(is);
            soulCount -= sc;
        }
    }

    public void onPlayerQuit(Player player) {
        addToPlayerLog(getQCPlayer(player), QCPlayerLog.Action.QUIT);
        debugPrintPlayerInfo("onPlayerQuit",player);
    }

    //TODO 2 remove "from nether" portals

    /**
     * This is called after onDeath() when the player has clicked the respawn button
     * and is ready to teleport to his new home.
     *
     * @return location to teleport to, or null if player should respawn as normal
     */
    public Location onRespawn(Player p) {
        QCPlayer player = qcp.pm.getQCPlayer(p);
        QCWorld w;
        int soulCount = player.getSoulsKeptDuringDeath();

        if(soulCount > 0) {
            db.beginTransaction();
            try{
                soulCount--;
                //just incase something funny happens, we subtract a soul from their death count
                player.setSoulsKeptDuringDeath(soulCount);
                db.update(player);

                w = player.getWorld();

                if(soulCount == 0)
                    p.sendMessage("You have no souls left, you are not long for this world.");
                else if(soulCount == 1)
                    p.sendMessage("You feel a lot less than whole. You have one soul left.");
                else if(soulCount < SOULS_PER_REBIRTH)
                    p.sendMessage("You feel a little less than whole. You have "+(soulCount)+" souls left.");
                else if(soulCount == SOULS_PER_REBIRTH)
                    p.sendMessage("You feel spiritually content. You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *2)
                    p.sendMessage("You feel spiritually bloated, as if you are greater than mortal. You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *3)
                    p.sendMessage("You feel like a ethereal king, fear your wrath! You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *4)
                    p.sendMessage("You feel like a soul sucking demon, who has fallen so you may grow so large? You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *5)
                    p.sendMessage("You've become almost immortal, flee, mortals, flee! You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *6)
                    p.sendMessage("Are you a god? You have "+(soulCount)+" souls left.");
                else if(soulCount < SOULS_PER_REBIRTH *7)
                    p.sendMessage("Yes, you are a god. You have "+(soulCount)+" souls left.");
                else
                    p.sendMessage("You can stop now... You have "+(soulCount)+" souls left.");

                giveInitialPackageToPlayer(p, p.getWorld() , soulCount, false);
                db.commitTransaction();
            } finally {
                db.endTransaction();
            }

            debugPrintPlayerInfo("onRespawn with souls left",p);
            Location bedSpawnLocation = p.getBedSpawnLocation();
            if(bedSpawnLocation != null && bedSpawnLocation.getWorld() == p.getWorld())
                return bedSpawnLocation;

            return Bukkit.getWorld(w.getName()).getSpawnLocation();
        }

        db.beginTransaction();
        try{
            addToPlayerLog(player, QCPlayerLog.Action.PERMA_DEATH);

            w = qcp.wm.findOrCreateBestWorldForDeadPlayer(p);

            player.setWorld(w);
            db.update(player);
            Bukkit.getLogger().info("onRespawn set world "+w+","+player);

            debugPrintPlayerInfo("onRespawn without souls left",p);

            addToPlayerLog(player, QCPlayerLog.Action.MOVED_TO_WORLD);

           db.commitTransaction();
        } finally {
           db.endTransaction();
        }

        debugPrintPlayerInfo("onRespawn without souls left",p);

        Location spawnLocation;

        if(WorldUtil.isNetherWorld(w.getName())) {
            //we only want this message to appear the first time the player entered the nether
            if(!WorldUtil.isNetherWorld(p.getWorld().getName()))
                p.sendMessage("The world slowly comes into place. You blink twice... wait a minute, where *ARE* you?");
            //TODO 3 maybe put up a message indicating how much time before an abandoned world can be
            //revisited
            //TODO 2.5 maybe allow someone to revisit a world if they were the only one who revisited it
            //since the abandonment time (in another random spawn location of course... create a
            // new visit id). Or should the visited qcWorlds be based on the people in them? In other
            // words, dying in a world with only one player doesn't prevent that world from being
            // visited again.


            //in the nether the spawn location is always random, to prevent someone from creating a booby
            //trap at a spawn location
            spawnLocation = WorldUtil.getRandomSpawnLocation(Bukkit.getWorld(WorldUtil.NETHER_WORLD_NAME));

        }
        else
        {
            World spawnedWorld = Bukkit.getWorld(w.getName());

            giveInitialPackageToPlayer(p,spawnedWorld, SOULS_PER_REBIRTH, true);
            p.sendMessage("You have been reborn into " + w.getName());

            spawnLocation = w.getSpawnLocation(spawnedWorld);
        }


        return spawnLocation;

    }

    /**
     * Returns players nearby a location
     * @param l location
     * @param maxDistance max distance from location
     * @return players within max distance of the location
     */
    public static List<Player> getNearbyPlayers(Location l, int maxDistance) {
        List<Player> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.getLocation().distance(l) < maxDistance)
                players.add(p);
        }

        return players;
    }

    /**
     * Searches a players inventory for a portal key
     *
     * @param p player
     * @return portal key in players inventory, or null if it doesn't exist
     */
    public static ItemStack getPortalKey(Player p) {
        for(ItemStack i : p.getInventory())
        {
            if(i != null && i.getType() == PORTAL_KEY_MATERIAL && i.getItemMeta() != null
                    && i.getItemMeta().getDisplayName() != null
                    && i.getItemMeta().getDisplayName().endsWith(PORTAL_KEY_DISPLAY_NAME_ENDING))
                return i;
        }

        return null;
    }

    //TODO 2 create some reasonable logging messages so when we go alpha we'll have more to go on

    /**
     * Called when a player is about to be teleported by a portal
     */
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        BlockArea b = WorldUtil.findActivePortal(event.getFrom());

        if (b == null) {
            return;
        }

        Location l = WorldUtil.getRepresentativePortalLocation(b);
        Bukkit.getLogger().info("Player portal location"+l);

        QCPortalLink pl = qcp.portalManager.getPortalLinkForLocation(l);
        Bukkit.getLogger().info("Portal link for location " +pl);

        if(pl == null) {
            Bukkit.getLogger().info("Could not find link for " +l);
            WorldUtil.destroyPortal(l, false);
            return;
        }

        QCPlayer qcPlayer = getQCPlayer(event.getPlayer());

        Location otherLocation = pl.getOtherLoc(qcp.wm,l);
        Location teleportLocation = WorldUtil.findPortalTeleportPlaceForUser(otherLocation);
        WorldUtil.makeTeleportLocationSafe(teleportLocation);
        event.getPlayer().teleport(teleportLocation);
        event.setCancelled(true);
    }

    //TODO 2 when a player sleeps in a bed in a new world, we change their worldId
    //TODO 2 figure out what we want in the qcplayerlog, we no longer are using
    // MOVE_TO_WORLD, MOVE_FROM_WORLD actions
}
