package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.util.*;

//TODO 2.5 if a portal gets rebuilt in the same location as a previous one,
//and it didn't have a special block before (because it was automatically created),
//then it can be reactivated even without a special block


public class QuietCraftPlugin extends JavaPlugin {
    public WorldManager wm;
    public PlayerManager pm;

    public void onEnable() {
        PluginDescriptionFile desc = getDescription();

        System.out.println(desc.getFullName() + " has been enabled");

        boolean freshInstallation = setupDatabase();

        wm = new WorldManager(this);
        pm = new PlayerManager(this);

        if(freshInstallation) {
            wm.setupForNewInstall();
        }

        getServer().getPluginManager().registerEvents(new QCListener(this), this);

        this.getCommand("kill").setExecutor(new KillCommandExecutor());
        this.getCommand("gm").setExecutor(new HackGiveStuffCommandExecutor());
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
        list.add(QCWorld.class);
        return list;
    }

    public static boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
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