package com.rareventure.quietcraft;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tim on 9/10/16.
 */
public class Util {

    public static int getValidHighestY(Location l, Set<Material> blackList) {

        l.getWorld().getChunkAt(l).load();

        BlockState b = l.getWorld().getHighestBlockAt(l).getState();
        if(blackList.contains(b.getType()))
            return -1;

        return b.getY();
    }

}
