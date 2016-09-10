package com.rareventure.quietcraft;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Collection;

/**
 * A 3D rectangular area in block units
 */
public class BlockArea
{
    public int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
    public int minY= Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
    public int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

    public World w;

    public BlockArea()
    {}

    public BlockArea(Collection<Block> bC)
    {
        for(Block b : bC)
        {
            expandArea(b.getLocation());
        }
    }

    public void expandArea(Location location) {
        w = location.getWorld();
        minX = Math.min(location.getBlockX(), minX);
        minY = Math.min(location.getBlockY(), minY);
        minZ = Math.min(location.getBlockZ(), minZ);

        maxX = Math.max(location.getBlockX(), maxX);
        maxY = Math.max(location.getBlockY(), maxY);
        maxZ = Math.max(location.getBlockZ(), maxZ);
    }

    public Location getCenter() {
        return new Location(w,
                ((double)minX+maxX)*.5,
                ((double)minY+maxY)*.5,
                ((double)minZ+maxZ)*.5
        );
    }
}
