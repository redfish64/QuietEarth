package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.rareventure.quietcraft.commands.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.util.*;

//TODO 2.5 if a portal gets rebuilt in the same location as a previous one,
//and it didn't have a special block before (because it was automatically created),
//then it can be reactivated even without a special block


public class QuietCraftPlugin extends JavaPlugin {
    private static final String DEFAULT_CONFIG_RESOURCE_PATH = "/default_config.yml";
    private static final String CONFIG_FILENAME = "config.yml";
    public static EbeanServer db;
    public WorldManager wm;
    public PlayerManager pm;
    public PortalManager portalManager;
    public ChatManager chatManager;
    public static FileConfiguration cfg;

    public void onEnable() {
        readConfigFile();

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
        this.getCommand("w").setExecutor(new WorldCommandExecutor());
        this.getCommand("cp").setExecutor(new AdminCreatePortal());
        this.getCommand("nb").setExecutor(new AdminNoisyBlock(this));
        this.getCommand("jw").setExecutor(new AdminJumpToWorld(this));
        this.getCommand("say_mode").setExecutor(new SetSpeakStyleSay(this));
        this.getCommand("whisper_mode").setExecutor(new SetSpeakStyleWhisper(this));
        this.getCommand("shout_mode").setExecutor(new SetSpeakStyleShout(this));

    }

    private void readConfigFile() {
        //look for normal config file first. If it doesn't exist, read default hardcoded one
        try {
            File configFile = new File(getDataFolder(),CONFIG_FILENAME);
            if(!configFile.exists()) {
                this.getConfig().load(new InputStreamReader(this.getClass().getResourceAsStream(DEFAULT_CONFIG_RESOURCE_PATH)));
                //TODO 2 reenable this after we finished default_config.yml
                //this.getConfig().save(configFile);
            }
            else
                this.getConfig().load(configFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }

        this.cfg = getConfig();

        Config.reloadConfig();

        getLogger().info("config file test: "+cfg.getString("hello.world"));
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
            return true;
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

}