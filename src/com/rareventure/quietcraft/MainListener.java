package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * Listens for world events and ferries the data off to the right manager
 */
public class MainListener implements Listener {
    private final QuietCraftPlugin qcp;

    public MainListener(QuietCraftPlugin qcp)
    {
        this.qcp = qcp;
    }

    public static boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cannot execute that command, I don't know who you are!");
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Bukkit.getLogger().info("onPlayerJoin: "+event.getPlayer());
        qcp.pm.onPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Bukkit.getLogger().info("onPlayerQuit: "+event.getPlayer());
        qcp.pm.onPlayerQuit(event.getPlayer());
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent e){
        if(e.getEntityType() != EntityType.PLAYER)
            return;

        Bukkit.getLogger().info("onPlayerDeath: "+e.getEntity());
        qcp.pm.onPlayerDeath(e.getEntity(), e);
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onRespawn(PlayerRespawnEvent e) {

        Location l = qcp.pm.onRespawn(e.getPlayer());

        Bukkit.getLogger().info(
                String.format("onRespawn: %s respawned to %s",e.getPlayer().getName(),String.valueOf(l)));

        if(l != null)
            e.setRespawnLocation(l);
    }

    //note that EntityCreatePortalEvent doesn't go off
    //for portals created
    //by fire, so we use just PortalCreateEvent instead

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPortalCreate(PortalCreateEvent event) {
        Bukkit.getLogger().info("onPortalCreate: "+event.getBlocks().get(0).getLocation());

        qcp.wm.onPortalCreate(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPortalEvent(PlayerPortalEvent event)
    {
        Bukkit.getLogger().info("onPlayerPortalEvent: "+event.getPlayer()+" "+event.getPlayer().getLocation());
        qcp.pm.onPlayerPortalEvent(event);
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent e)
    {
        qcp.wm.onBlockPhysicsEvent(e);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Bukkit.getLogger().info("onPlayerChat: "+event.getPlayer()+" "+event.getPlayer().getLocation()
                +" msg: "+event.getMessage());
        qcp.chatManager.onPlayerChat(event);
    }
}
