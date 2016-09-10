package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        qcp.pm.onPlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        qcp.pm.onPlayerQuit(event.getPlayer());
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent e){
        if(e.getEntityType() != EntityType.PLAYER)
            return;

        qcp.pm.onPlayerDeath(e.getEntity(), e);
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onRespawn(PlayerRespawnEvent e) {

        Location l = qcp.pm.onRespawn(e.getPlayer());

        if(l != null)
            e.setRespawnLocation(l);
    }

    //note that EntityCreatePortalEvent for portals triggered
    //by fire, so we use just PortalCreateEvent instead

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPortalCreate(PortalCreateEvent event) {
        qcp.wm.onPortalCreate(event);
    }
}
