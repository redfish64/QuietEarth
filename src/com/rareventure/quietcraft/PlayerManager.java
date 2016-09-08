package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
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
    private static final int SOULS_PER_REBIRTH = 4;
    private static final Material SOUL_MATERIAL_TYPE = Material.DIAMOND;
    private static final String SOUL_DISPLAY_NAME = "Soul";

    //TODO 2.5 make saying 'hello sailor' cause the person to be immediately transported to the nether
    //with no souls
    private static final List<String> SOUL_LORE = Arrays.asList(
            ("Oh ye who go about saying " +
                    "unto each:  \"Hello sailor\":\n" +
            "Dost thou know the magnitude " +
                    "of thy sin before the gods?\n" +
            "Yea, verily, thou shalt be " +
                    "ground between two stones.\n" +
            "Shall the angry gods cast thy " +
                    "body into the whirlpool?\n" +
            "Surely, thy eye shall be put " +
                    "out with a sharp stick!\n" +
            "Even unto the ends of the " +
                    "earth shalt thou wander and\n" +
            "unto the land of the dead " +
                    "shalt thou be sent at last.\n" +
            "Surely thou shalt repent of" +
                    " thy cunning.").split("\n"));

    private static final Material PORTAL_KEY_MATERIAL = Material.FLINT_AND_STEEL;

    private static final List<String> PORTAL_KEY_LORE = Arrays.asList(
            ("This flint of steel seems magical somehow...").split("\n"));
    private static final String PORTAL_KEY_DISPLAY_NAME_ENDING = " portal key";

    private final QuietCraftPlugin qcp;
    private final EbeanServer db;

    public PlayerManager(QuietCraftPlugin qcp)
    {
        this.db = qcp.getDatabase();
        this.qcp = qcp;
    }

    private void addToPlayerLog(Player player, QCPlayerLog.Action action)
    {
        QCWorld w = getPlayersWorld(player);
        db.insert(new QCPlayerLog(player.getUniqueId().toString(),new Date(),
                w.getId(), w.getVisitId(),
                action));
    }

    public QCPlayer getQCPlayer(UUID uniqueId) {
        return db.find(QCPlayer.class).where().
                eq("uuid", uniqueId.toString()).findUnique();
    }

    public QCWorld getPlayersWorld(Player player) {
        QCPlayer qcPlayer = getQCPlayer(player.getUniqueId());

        if(qcPlayer != null)
            return qcp.wm.getQCWorld(qcPlayer.getWorldId());

        return null;
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

    public QCPlayer createQCPlayer(Player player, QCWorld w) {
        QCPlayer qcPlayer = new QCPlayer(player.getUniqueId().toString(), w.getId());
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
                giveInitialPackageToPlayer(player, SOULS_PER_REBIRTH, true);

                QCWorld w = qcp.wm.findFirstJoinWorld();
                createQCPlayer(player,w);

                player.sendMessage("You have been born in world '"+w.getName()+"'");
                player.teleport(Bukkit.createWorld(new WorldCreator(w.getName())).getSpawnLocation());

                addToPlayerLog(player, QCPlayerLog.Action.JOIN);
                db.commitTransaction();
                return;
            }

           addToPlayerLog(player, QCPlayerLog.Action.JOIN);
            db.commitTransaction();

            player.sendMessage("You are in world '"+
                    qcp.pm.getPlayersWorld(player.getPlayer()).getName()+"'");
            return;

        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Gives the player their initial items when they die or join a world
     * @param player
     * @param soulCount souls remaining
     */
    private void giveInitialPackageToPlayer(Player player, int soulCount, boolean isFirstAppearance) {
        Inventory i = player.getInventory();
        i.clear();

        if(isFirstAppearance)
        {
            ItemStack is = new ItemStack(PORTAL_KEY_MATERIAL, 20);
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
        addToPlayerLog(player, QCPlayerLog.Action.QUIT);
    }

    /**
     * This is called after onDeath() when the player has clicked the respawn button
     * and is ready to teleport to his new home.
     *
     * @return location to teleport to.
     */
    public Location onRespawn(Player p) {
        QCPlayer player = qcp.pm.getQCPlayer(p);
        QCWorld w;
        int soulCount = player.getSoulsKeptDuringDeath();

        db.beginTransaction();
        try{
            if(soulCount > 0) {
                soulCount--;
                //just incase something funny happens, we subtract a soul from their death count
                player.setSoulsKeptDuringDeath(soulCount);
                db.update(player);

                w = getPlayersWorld(p);

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
            }
            else {
                w = qcp.wm.findBestWorldForDeadPlayer(p);

                player.setWorldId(w.getId());
                db.update(player);

                addToPlayerLog(p, QCPlayerLog.Action.PERMA_DEATH);

                if(w == qcp.wm.netherWorld)
                    soulCount = 0;
                else soulCount = SOULS_PER_REBIRTH;
                p.sendMessage("You have been reborn into "+w.getName());
                giveInitialPackageToPlayer(p,soulCount, true);
            }
            db.commitTransaction();
        } finally {
            db.endTransaction();
        }


        return Bukkit.getWorld(w.getName()).getSpawnLocation();
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

    public static boolean containsPortalKey(Player p) {
        for(ItemStack i : p.getInventory())
        {
            if(i != null && i.getType() == PORTAL_KEY_MATERIAL && i.getItemMeta() != null
                    && i.getItemMeta().getDisplayName() != null
                    && i.getItemMeta().getDisplayName().endsWith(PORTAL_KEY_DISPLAY_NAME_ENDING))
                return true;
        }

        return false;
    }
}
