package com.rareventure.quietcraft.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A 3D rectangular area in block units
 */
public class BlockArea
{
    /**
     * Max values are exclusive (ex 0, 3 is 3 blocks long)
     */
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
            expandArea(b.getState());
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

    /**
     * @return an array of all the blocks within the blockarea
     */
    public ArrayList<BlockState> getBlocks(World w) {
        ArrayList<BlockState> bs = new ArrayList<>((maxX - minX+1)*(maxY-minY+1)*(maxZ-minZ+1));

        for(int x = minX; x < maxX; x++)
        {
            for(int y = minY; y < maxY; y++)
            {
                for(int z = minZ; z < maxZ; z++)
                {
                    bs.add(w.getBlockAt(x,y,z).getState());
                }
            }
        }

        return bs;
    }

    /**
     * Expands the block area to encompass given block location.
     * Note the value is assumed to be a 1x1x1 cube, *NOT* a point.
     */
    public void expandArea(int x, int y, int z) {
        minX = Math.min(x, minX);
        minY = Math.min(y, minY);
        minZ = Math.min(z, minZ);

        maxX = Math.max(x+1, maxX);
        maxY = Math.max(y+1, maxY);
        maxZ = Math.max(z+1, maxZ);
    }

    /**
     * Finds the approximate center (subject to rounding errors)
     */
    public int getCenterX() {
        return ((minX>>1) + (maxX>>1));
    }

    /**
     * Finds the approximate center (subject to rounding errors)
     */
    public int getCenterY() {
        return ((minY>>1) + (maxY>>1));
    }

    /**
     * Finds the approximate center (subject to rounding errors)
     */
    public int getCenterZ() {
        return ((minZ>>1) + (maxZ>>1));
    }

    public int getLengthX() {
        return maxX - minX;
    }
    public int getLengthY() {
        return maxY - minY;
    }
    public int getLengthZ() {
        return maxZ - minZ;
    }

    /**
     * Swaps the x and z dimensions
     */
    public void swapXZDim() {
        int t;

        t = minX;
        minX = minZ;
        minZ = t;

        t = maxX;
        maxX = maxZ;
        maxZ = t;
    }

    /**
     * Expands the block area to encompass blockstate
     * @param b
     */
    public void expandArea(BlockState b) {
        w = b.getWorld();
        expandArea(b.getX(),b.getY(),b.getZ());
    }

    @Override
    public String toString() {
        return "BlockArea{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                ", w=" + w+
                '}';
    }

    public void expandArea(Block block) {
        expandArea(block.getState());
    }
}
