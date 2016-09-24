package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Periodically gives soul shards to everyone on the server.
 */
public class SoulShardTimer {
    private QuietCraftPlugin qcp;

    public SoulShardTimer(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    public void start()
    {
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                giveawaySoulShards();
            }
        };

        qcp.i("Starting soul shard giveaway task");
        br.runTaskTimer(qcp, 0, (long) Math.round(Config.SOUL_SHARD_GIVEAWAY_MINUTES
                * WorldUtil.TICKS_PER_SECOND * 60));
        }

    private void giveawaySoulShards() {
        qcp.i("Putting soul shard in worlds");
        qcp.wm.qcWorlds.stream().filter(w -> !WorldUtil.isNetherWorld(w.getName()))
                .forEach(w -> putShardNearSpawnPoint(
                        WorldUtil.getRandomSpawnLocation(w.getSpawnLocation(w.getWorld()),
                                Config.SOUL_SHARD_RNP)));
        Bukkit.broadcastMessage("A Soul shard has appeared at the spawn point...");
    }

    private void putShardNearSpawnPoint(Location spawnLocation) {
        ItemStack soulShard = WorldUtil.createSoulShard();
        spawnLocation.getWorld().dropItemNaturally(spawnLocation, soulShard);
        qcp.i("Spawned soul shard at "+spawnLocation);
    }

}
