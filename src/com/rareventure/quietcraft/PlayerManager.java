package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    private static final String SOUL_DISPLAY_NAME = "Soul";

    //TODO 2.5 make saying 'hello sailor' cause the person to be immediately transported to the nether
    //with no souls
    private static final List<String> SOUL_LORE = Arrays.asList(
            ("This is a soul. It's very valuable if you like living in this world.").split("\n"));

    private static final Material PORTAL_KEY_MATERIAL = Material.FLINT_AND_STEEL;

    private static final List<String> PORTAL_KEY_LORE = Arrays.asList(
            ("This flint of steel seems magical somehow...").split("\n"));
    public static final String PORTAL_KEY_DISPLAY_NAME_ENDING = " portal key";

    private final QuietCraftPlugin qcp;

    private EbeanServer db;

    public PlayerManager(QuietCraftPlugin qcp)
    {
        this.db = qcp.getDatabase();
        this.qcp = qcp;
    }

    private void addToPlayerLog(QCPlayer qcPlayer, QCPlayerLog.Action action)
    {
        db.insert(new QCPlayerLog(qcPlayer.getUuid(),new Date(),
                qcPlayer.getVisitedWorld().getId(),
                action));
    }

    public QCPlayer getQCPlayer(UUID uniqueId) {
        return db.find(QCPlayer.class).where().
                eq("uuid", uniqueId.toString()).findUnique();
    }

    public void onPlayerDeath(Player p, PlayerDeathEvent e) {
        //TODO 2 in the nether, souls aren't removed from inventory (are collectable), and the player is reborn
        //in the next world

        EbeanServer db = qcp.getDatabase();

        int soulCount = getSoulCount(p);

        QCPlayer qcPlayer = getQCPlayer(p.getUniqueId());

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
        if(im.getDisplayName().equals(SOUL_DISPLAY_NAME))
            return true;
        return false;
    }

    public QCPlayer getQCPlayer(Player player) {
        return getQCPlayer(player.getUniqueId());
    }

    public QCPlayer createQCPlayer(Player player, QCVisitedWorld vw) {
        QCPlayer qcPlayer = new QCPlayer(player.getUniqueId().toString(), vw, SOULS_PER_REBIRTH);
        db.insert(qcPlayer);

        return qcPlayer;
    }


    public void onPlayerJoin(Player player) {
        QCPlayer qcPlayer = getQCPlayer(player);

        db.beginTransaction();
        try {
            //new player
            if(qcPlayer == null)
            {
                QCVisitedWorld w = qcp.wm.findFirstJoinVisitedWorld();
                qcPlayer = createQCPlayer(player,w);

                player.sendMessage("You have been born in world '"+w.getName()+"'");

                Location spawnLocation = Bukkit.getWorld(w.getName()).getSpawnLocation();
                player.teleport(spawnLocation);
                giveInitialPackageToPlayer(player, SOULS_PER_REBIRTH, true);

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
     * Gives the player their initial items when they die or join a world
     * @param player
     * @param soulCount souls remaining
     */
    private void giveInitialPackageToPlayer(Player player, int soulCount, boolean isFirstAppearance) {
        Inventory i = player.getInventory();
        i.clear();

        if(isFirstAppearance && !player.getWorld().equals(WorldManager.NETHER_WORLD_NAME))
        {
            ItemStack is = new ItemStack(PORTAL_KEY_MATERIAL);
            ItemMeta im = is.getItemMeta();

            im.setDisplayName(player.getWorld().getName()+PORTAL_KEY_DISPLAY_NAME_ENDING);
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

    /**
     * This is called after onDeath() when the player has clicked the respawn button
     * and is ready to teleport to his new home.
     *
     * @return location to teleport to, or null if player should respawn as normal
     */
    public Location onRespawn(Player p) {
        QCPlayer player = qcp.pm.getQCPlayer(p);
        QCVisitedWorld vw;
        int soulCount = player.getSoulsKeptDuringDeath();

        if(soulCount > 0) {
            db.beginTransaction();
            try{
                soulCount--;
                //just incase something funny happens, we subtract a soul from their death count
                player.setSoulsKeptDuringDeath(soulCount);
                db.update(player);

                vw = player.getVisitedWorld();

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

                giveInitialPackageToPlayer(p,soulCount, false);
                db.commitTransaction();
            } finally {
                db.endTransaction();
            }

            debugPrintPlayerInfo("onRespawn with souls left",p);
            Location bedSpawnLocation = p.getBedSpawnLocation();
            if(bedSpawnLocation != null && bedSpawnLocation.getWorld() == p.getWorld())
                return bedSpawnLocation;

            Location spawnLocation = Bukkit.getWorld(vw.getName()).getSpawnLocation();
            return spawnLocation;
        }

        //db.beginTransaction();
        try{
            addToPlayerLog(player, QCPlayerLog.Action.PERMA_DEATH);

            vw = qcp.wm.findBestWorldForDeadPlayer(p);

            player.setVisitedWorld(vw);
            db.update(player);
            Bukkit.getLogger().info("onRespawn set visited world "+vw+","+player);

            debugPrintPlayerInfo("onRespawn without souls left",p);

            addToPlayerLog(player, QCPlayerLog.Action.MOVED_TO_WORLD);

            if(vw == qcp.wm.netherVisitedWorld)
                soulCount = 0;
            else soulCount = SOULS_PER_REBIRTH;
            p.sendMessage("You have been reborn into "+vw.getName());
            giveInitialPackageToPlayer(p,soulCount, true);

           // db.commitTransaction();
        } finally {
           // db.endTransaction();
        }

        debugPrintPlayerInfo("onRespawn without souls left",p);

        Location spawnLocation = Bukkit.getWorld(vw.getName()).getSpawnLocation();

        return spawnLocation;

    }

    /**
     * Returns players nearby a location
     * @param l location
     * @param maxDistance max distance from location
     * @return players within max distance of the location
     */
    public static List<Player> getNearbyPlayers(Location l, int maxDistance) {
        List<Player> players = new ArrayList<Player>();
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
}
