package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Periodically gives soul shards to everyone on the server.
 */
public class SoulTimer {
    private QuietCraftPlugin qcp;

    public SoulTimer(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    public void start()
    {
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                qcp.i("Dropping a soul in populated worlds");
                dropSouls();
            }
        };

        qcp.i("Starting soul drop task");
        long time = (long) Math.round(Config.SOUL_DROP_MINUTES
                * WorldUtil.TICKS_PER_SECOND * 60);
        br.runTaskTimer(qcp, time, time);
        }

    private void dropSouls() {
        Bukkit.getOnlinePlayers().stream().map( p -> p.getWorld()).collect(Collectors.toSet())
                .forEach(w -> dropSoulAtSpawnPoint(w));
    }

    private void dropSoulAtSpawnPoint(World w) {
        qcp.i("Droppped soul in "+w.getName());
        w.dropItemNaturally(WorldUtil.getRandomSpawnLocation(w.getSpawnLocation(),
                new MathUtil.RandomNormalParams(0,1,-5,5)), WorldUtil.createSoulGem(1));
    }


}
