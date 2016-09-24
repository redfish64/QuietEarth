package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import com.rareventure.quietcraft.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Exchanger;
import java.util.logging.Level;

//TODO 2.5 if a portal gets rebuilt in the same location as a previous one,
//and it didn't have a special block before (because it was automatically created),
//then it can be reactivated even without a special block


public class QuietCraftPlugin extends JavaPlugin {
    public static EbeanServer db;
    public static FileConfiguration defaultCfg;
    public WorldManager wm;
    public PlayerManager pm;
    public PortalManager portalManager;
    public ChatManager chatManager;
    public static FileConfiguration cfg;
    public CraftManager craftManager;

    public void onEnable() {
        Config.readConfigFile(this);

        //store db as a static variable because there will only be one instance of this
        //plugin, for sure.
        db = getDatabase();

        PluginDescriptionFile desc = getDescription();

        System.out.println(desc.getFullName() + " has been enabled");

        boolean freshInstallation = setupDatabase();

        wm = new WorldManager(this);
        pm = new PlayerManager(this);

        //note this MUST be run after world manager is set up, or qcWorlds
        //will not be created by the time we look for them (when we populate
        // the portallink cache)
        portalManager = new PortalManager(this);

        chatManager = new ChatManager(this);

        if(freshInstallation) {
            wm.setupForNewInstall();
        }


        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        this.getCommand("kill").setExecutor(new KillCommandExecutor());
        this.getCommand("gm").setExecutor(new AdminGiveStuffCommandExecutor());
        this.getCommand("w").setExecutor(new AdminWorldCommandExecutor(this));
        this.getCommand("cp").setExecutor(new AdminCreatePortal());
        this.getCommand("nb").setExecutor(new AdminNoisyBlock(this));
        this.getCommand("jw").setExecutor(new AdminJumpToWorld(this));
        this.getCommand("say_mode").setExecutor(new SetSpeakStyleSay(this));
        this.getCommand("whisper_mode").setExecutor(new SetSpeakStyleWhisper(this));
        this.getCommand("shout_mode").setExecutor(new SetSpeakStyleShout(this));
        this.getCommand("lc").setExecutor(new AdminListConfig(this));
        this.getCommand("ec").setExecutor(new AdminEditConfig(this));
        this.getCommand("sc").setExecutor(new AdminSaveConfig(this));
        this.getCommand("g").setExecutor(new AdminGive(this));

        SoulShardTimer soulShardTimer = new SoulShardTimer(this);
        soulShardTimer.start();

        SoulTimer soulTimer = new SoulTimer(this);
        soulTimer.start();

        craftManager = new CraftManager(this);

    }

    /**
     *
     * @return true if this is a fresh installation
     */
    private boolean setupDatabase() {
        try {
            getDatabase().find(QCPlayer.class).findRowCount();
            return false;
        } catch (PersistenceException ex) {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            super.installDDL();
            setupDatabaseIndexes();
            return true;
        }
    }

    private void setupDatabaseIndexes() {
        try {
            DbUtil.createIndex("qc_player","world_id");
            DbUtil.createIndex("qc_player_log","player_id");
            DbUtil.createIndex("qc_player_log","timestamp");
            DbUtil.createIndex("qc_player_log","world_id");
            //qc_portal_links are cached in memory, so no index is necessary
            //qc_world is too small to worry about it
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<>();
        list.add(QCPlayer.class);
        list.add(QCPlayerLog.class);
        list.add(QCPortalLink.class);
        list.add(QCWorld.class);
        return list;
    }

    //    public static Player getPlayer(CommandSender sender, String[] args, int index) {
//        if (args.length > index) {
//            List<Player> players = sender.getServer().matchPlayer(args[index]);
//
//            if (players.isEmpty()) {
//                sender.sendMessage("I don't know who '" + args[index] + "' is!");
//                return null;
//            } else {
//                return players.get(0);
//            }
//        } else {
//            if (anonymousCheck(sender)) {
//                return null;
//            } else {
//                return (Player)sender;
//            }
//        }
//    }

    @Override
    public void onDisable(){

    }

    public static void i(String s, Object ... f) {
        log(Level.INFO, s, f);
    }

    public static void w(String s, Object ... f) {
        log(Level.WARNING, s, f);
    }

    public static void e(String s, Object ... f) {
        log(Level.SEVERE, s, f);
    }

    public static void log(Level l, String s, Object ... f) {
        if(f.length > 0)
            Bukkit.getLogger().log(l,String.format(s, f));
        else
            Bukkit.getLogger().log(l,s);
    }


}