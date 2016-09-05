package com.rareventure.quietcraft;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.util.*;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kill")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
                Bukkit.getPluginManager().callEvent(ede);
                if (ede.isCancelled()) return true;

                ede.getEntity().setLastDamageCause(ede);
                player.setHealth(0);

                //TODO 2 maybe instead of killing the player, just teleport them to the next world
                //(of course all items would have to be reset). That way, "Respawn" won't appear for the
                //player..
                //sender.sendMessage("Ouch. That look like it hurt.");
            } else {
                sender.sendMessage("Only a player can do that.");
            }
            return true;
        }

        return false;
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