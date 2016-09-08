package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Date;
import java.util.logging.Level;

/**
 * Listens for world events and ferries the data off to the right manager
 */
public class QCListener implements Listener {
    private final QuietCraftPlugin qcp;

    public QCListener(QuietCraftPlugin qcp)
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

        e.getDrops().clear(); //HACK, filter for souls

        qcp.pm.onPlayerDeath(e.getEntity());
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onRespawn(PlayerRespawnEvent e) {

        Location l = qcp.pm.onRespawn(e.getPlayer());

        e.setRespawnLocation(l);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPortalCreate(PortalCreateEvent event) {
        String world = event.getWorld().getName();
        //qcp.getLogger().info("denied portal creation");
        //event.setCancelled(true);
    }
}
