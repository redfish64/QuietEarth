package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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

        qcp.pm.onPlayerDeath(e.getEntity());
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onRespawn(PlayerRespawnEvent e) {
        QCPlayer player = qcp.pm.getQCPlayer(e.getPlayer());
        QCWorld world = qcp.wm.getQCWorld(player.getWorldId());

        e.setRespawnLocation(Bukkit.getWorld(world.getName()).getSpawnLocation());
        e.getPlayer().sendMessage("You have been reborn into "+world.getName());
    }

}
