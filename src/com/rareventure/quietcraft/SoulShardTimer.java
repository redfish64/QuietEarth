package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

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
                qcp.i("Giving soul shards to players");
                giveawaySoulShards();
            }
        };

        qcp.i("Starting soul shard giveaway task");
        br.runTaskTimer(qcp, 0, (long) Math.round(Config.SOUL_SHARD_GIVEAWAY_MINUTES
                * WorldUtil.TICKS_PER_SECOND * 60));
        }

    private void giveawaySoulShards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            giveSoulShardToPlayer(player);
        }
    }

    private void giveSoulShardToPlayer(Player player) {
        qcp.i("Giving soul shard to %s",player.getName());
        player.sendMessage("You've earned a soul shard");

        ItemStack soulShard = createSoulShard();

        HashMap<Integer, ItemStack> leftOverItems = player.getInventory().addItem(soulShard);

        //if we couldn't save the soul shard to inventory, drop it at their feet
        if(!leftOverItems.isEmpty()) {
            player.sendMessage("Soul shard couldn't fit in inventory, dropped");
            player.getWorld().dropItemNaturally(player.getLocation(), soulShard);
        }
    }

    private ItemStack createSoulShard() {
        return
                WorldUtil.createSpecialItem(
                        Config.SOUL_SHARD_MATERIAL,
                        Config.SOUL_SHARD_DISPLAY_NAME,
                        Config.SOUL_SHARD_LORE, 1
                        );
    }

}
