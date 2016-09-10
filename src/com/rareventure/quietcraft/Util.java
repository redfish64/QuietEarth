package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Set;

/**
 * Created by tim on 9/10/16.
 */
public class Util {

    public static Block getHighestFreeBlockAt(final int posX, final int posZ, final World world)
    {
        final int maxHeight = world.getMaxHeight();

        int searchedHeight = maxHeight - 1;

        Block lastBlock = null;

        while (searchedHeight > 0)
        {
            final Block block = world.getBlockAt(posX, searchedHeight, posZ);

            if (lastBlock != null && lastBlock.getType() == Material.AIR &&
                    block.getType() != Material.AIR)
            { break; }

            lastBlock = block;
            searchedHeight--;
        }

        return world.getBlockAt(posX,++searchedHeight,posZ);
    }

    public static int getValidHighestY(Location l, Set<Material> blackList) {

        l.getWorld().getChunkAt(l).load();

        BlockState b = getHighestFreeBlockAt(l.getBlockX(),l.getBlockZ(),l.getWorld()).getState();
        BlockState ground = l.getWorld().getBlockAt(b.getX(),b.getY()-1,b.getZ()).getState();
        if(blackList.contains(ground.getType()))
            return -1;

        Bukkit.getLogger().info("spawn point ground material "+ground.getType());
        Bukkit.getLogger().info("spawn point block material "+b.getType());
        return b.getY();
    }

}
