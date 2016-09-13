package com.rareventure.quietcraft;

import com.google.common.collect.Sets;
import com.rareventure.quietcraft.utils.BlockArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods relating to dealing with the actual blocks and so forth
 */
public class WorldUtil {

    public static final String NETHER_WORLD_NAME = "world_nether";
    private static final int FLOOR_LENGTH = 2;

    /**
     * When portals are destroyed and exploded, this specifies the ratio of width of portal
     * to explosion power (as defined by World.createExplosion())
     */
    private static final float DESTROY_PORTAL_EXPLOSION_RATIO = 1.2f;
    /**
     * Maximum distance from 0,0,0 for random spawn locations
     */
    private static final double MAX_SPAWN_RADIUS = 10000.;
    /**
     * Places we make sure the spawn location is not at.
     */
    private static final HashSet<Material> SPAWN_LOCATION_BLACKLIST =
            Sets.newHashSet(
                    Material.LAVA,
                    Material.STATIONARY_LAVA,
                    Material.WATER,
                    Material.STATIONARY_WATER,
                    Material.CACTUS,
                    Material.FIRE
            );

    /**
     * Max height to search for an open area in the nether
     */
    private static final int NETHER_WORLD_MAX_HEIGHT = 125;

    public static Block getHighestFreeBlockAt(final int posX, final int posZ, final World world)
    {
        final int maxHeight = 
                isNetherWorld(world) ? NETHER_WORLD_MAX_HEIGHT :
                world.getMaxHeight();

//        Bukkit.getLogger().info("maxHeight is "+maxHeight);

        int searchedHeight = maxHeight - 1;

        Block lastBlock = null;

        while (searchedHeight > 0)
        {
            final Block block = world.getBlockAt(posX, searchedHeight, posZ);
//            Bukkit.getLogger().info("height is "+searchedHeight+" material is "+block.getType());

            if (lastBlock != null && lastBlock.getType() == Material.AIR &&
                    block.getType() != Material.AIR)
            { break; }

            lastBlock = block;
            searchedHeight--;
        }

        return world.getBlockAt(posX,++searchedHeight,posZ);
    }

    public static boolean isNetherWorld(World world) {
        return world.getName().equals(NETHER_WORLD_NAME);
    }

    public static int getValidHighestY(Location l, Set<Material> blackList) {

        l.getWorld().getChunkAt(l).load();

        BlockState b = getHighestFreeBlockAt(l.getBlockX(),l.getBlockZ(),l.getWorld()).getState();
        BlockState ground = l.getWorld().getBlockAt(b.getX(),b.getY()-1,b.getZ()).getState();
        if(blackList.contains(ground.getType()))
            return -1;

//        Bukkit.getLogger().info("spawn point ground material "+ground.getType());
//        Bukkit.getLogger().info("spawn point block material "+b.getType());
//        Bukkit.getLogger().info("spawn point highest y "+b.getY());
        return b.getY();
    }

    /**
     * Max portal size in any dimension
     */
    private static final int MAX_PORTAL_DIM = 22;

    /**
     * Finds a portal facing a particular direction. This does not validate
     * the portal.
     *
     * @param l location inside portal
     * @param isXDim True if portal is lined up with the x dimension
     * @return
     */
    public static BlockArea findPortal(Location l, boolean isXDim) {
        World w = l.getWorld();
        BlockState b = l.getBlock().getState();

        BlockArea ba = new BlockArea();
        ba.expandArea(b);

        //we simply swap x and z dimensions and always pretend we are looking
        //for a Z aligned portal
        if(!isXDim)
            ba.swapXZDim();

        //keep expanding ba until we found an obsidian edge in all directions
        //top = 0, left = 1, bottom = 2, right = 3
        for (int side = 0; side < 4; side++) {

            int newX = !isXDim ? l.getBlockZ() : l.getBlockX();
            int newY = l.getBlockY();

            int xDelta, yDelta;

            switch (side) {
                case 0:
                    xDelta = 0;
                    yDelta = 1;
                    break;
                case 1:
                    xDelta = -1;
                    yDelta = 0;
                    break;
                case 2:
                    xDelta = 0;
                    yDelta = -1;
                    break;
                default: //case 3:
                    xDelta = 1;
                    yDelta = 0;
                    break;
            }

            //expand a single side
            for (; ; ) {
                newX += xDelta;
                newY += yDelta;

//                Bukkit.getLogger().info(String.format("nx %d ny %d side %d ba %s", newX, newY, side, ba));

                ba.expandArea(newX, newY, ba.minZ);

                //if we are at the edge (we swap x and y if we are searching along x dim)
                if (isXDim &&
                        w.getBlockAt(newX, newY, ba.minZ).getType() == Material.OBSIDIAN
                        ||
                        !isXDim &&
                                w.getBlockAt(ba.minZ, newY, newX).getType() == Material.OBSIDIAN) {
                    break;
                }

                //if we've searched for too long and didn't find a portal, give up
                if (ba.getLengthX() > MAX_PORTAL_DIM || ba.getLengthY() > MAX_PORTAL_DIM)
                    return null;
            }
        } //for each side

        if(!isXDim)
        {
            ba.swapXZDim();
        }
        return ba;
    }

    //TODO 2 make explosion bigger, 5?
    private static final float DESTROY_PORTAL_EXPLOSION_SIZE = 1f;
    private static final int MAX_ACTIVE_PORTAL_LOC_DIST = 3;

    public static void createPortal()
    {

    }

    public static BlockArea getPortalArea(List<Block> blocks)
    {
        boolean alignedWithX = isPortalAlignedWithX(blocks);

        BlockArea portalArea = WorldUtil.findPortal(new BlockArea(blocks).getCenter(), alignedWithX);

        return portalArea;
    }

    /**
     * Gives a bunch of blocks associated with a portal, finds out
     * whether the portal is aligned with the X dimension or the Z dimension
     *
     * @param blocks
     * @return
     */
    private static boolean isPortalAlignedWithX(List<Block> blocks) {
        Block b1 = blocks.get(0);

        for(Block b : blocks)
        {
            if(b.getX() != b1.getX())
                return true;
            if(b.getZ() != b1.getZ())
                return false;
        }

        throw new IllegalArgumentException("Blocks don't contain any difference in X or Z!");
    }

    public static void destroyPortal(World w, BlockArea portalArea, boolean hasExplosion) {
        portalArea.getBlocks(w).stream().filter(b -> b.getType() == Material.OBSIDIAN).
                forEach(b -> b.getBlock().setType(Material.SAND));

        if(hasExplosion)
            w.createExplosion(portalArea.getCenter(), DESTROY_PORTAL_EXPLOSION_RATIO
                    * portalArea.getLengthX());

    }

    public static void destroyPortal(List<Block> blocks, boolean hasExplosion) {
        destroyPortal(blocks.get(0).getWorld(), getPortalArea(blocks), hasExplosion);
    }

    /**
     * Finds a nearby active portal, probably the nearest, to the location, within MAX_ACTIVE_PORTAL_LOC_DIST
     * @return block area of nearby portal
     */
    public static BlockArea findActivePortal(Location l) {
        Block b = WorldUtil.findNearbyBlockContainingMaterial(l,MAX_ACTIVE_PORTAL_LOC_DIST,Material.PORTAL);
        boolean isXAligned = WorldUtil.isMaterialXAligned(b);

        return WorldUtil.findPortal(b.getLocation(),isXAligned);
    }

    /**
     * Assuming there is a plane aligned either in the x or z dimension of given material,
     * finds whether it is aligned in x or z
     */
    public static boolean isMaterialXAligned(Block b) {
        if(b.getRelative(1,0,0).getType() == b.getType() ||
           b.getRelative(-1,0,0).getType() == b.getType())
            return true;

        return false;
    }

    /**
     * This searches for a nearby block containing the given material. The block found may not be the nearest,
     * but it will be close to the nearest.
     *
     * @param l  location to search from
     * @param maxActivePortalLocDist  maximum distance of block as a corner of a cube (max dist actually sqrt(d^2*3) )
     * @param m material to look for
     * @return block or null if couldn't find
     */
    public static Block findNearbyBlockContainingMaterial(Location l, int maxActivePortalLocDist, Material m) {
        World w = l.getWorld();

        Block cb = l.getBlock();

        int dist = 0;

        while(dist <= maxActivePortalLocDist) {
            for (int x = -dist; x <= dist; x++) {
                for (int y = -dist; y <= dist; y++) {
                    for (int z = -dist; z <= dist; z++) {
                        //if we are in the meat of the cube, we can skip past it, because
                        //we checked it for a previous dist
                        if (z != -dist && z != dist
                                && x != -dist && x != dist
                                && y != -dist && y != dist)
                            z = dist;

                        Block b = w.getBlockAt(
                                x + cb.getX() - dist,
                                y + cb.getY() - dist,
                                z + cb.getZ() - dist);
                        if (b.getType() == m)
                            return b;
                    }
                }
            }
            dist++;
        }

        return null;
    }

    /**
     * Creates a portal with a floor.
     *  @param l center of portal
     * @param w width of portal
     * @param h height of portal
     * @param xAligned if true, portal will be created along the x axis, otherwise along the z axis
     * @param activate if true, the portal will be active (contain PORTAL, not AIR)
     */
    public static Location constructPortal(Location l, int w, int h, boolean xAligned, boolean activate) {
        Block b1 = l.getBlock();

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Block b;
                if(xAligned)
                    b = b1.getRelative(x - w/2,y,0);
                else
                    b = b1.getRelative(0,y,x - w/2);

                if(x == 0 || x == w-1 || y == 0 || y == h-1)
                    b.setType(Material.OBSIDIAN);
                else
                {
                    if(activate) {
                        b.setType(Material.PORTAL);
                        b.getState().getData();
                    }
                    else
                        b.setType(Material.AIR);
                }


                //create air around the portal
                if(xAligned) {
                    b.getRelative(0, 0, -1).setType(Material.AIR);
                    b.getRelative(0, 0, 1).setType(Material.AIR);
                }
                else {
                    b.getRelative(-1, 0, 0).setType(Material.AIR);
                    b.getRelative(1, 0, 0).setType(Material.AIR);
                }
            }
        }

        //create a little floor for their tender feeties
        for(int x = 0; x < w; x++) {
            for (int z = -FLOOR_LENGTH; z <= FLOOR_LENGTH; z++) {
                Block b;
                if(xAligned)
                    b=b1.getRelative(x - w/2,-1,z);
                else
                    b=b1.getRelative(z,-1,x - w/2);
                b.setType(Material.SANDSTONE);
            }
        }


        return WorldUtil.findPortalLocation(l,xAligned);
    }

    /**
     * Returns the representative location of a portal, which is defined as the portals center in x and z,
     * and minY + 1). This would be the center location where a user could stand within the portal.
     */
    public static Location getRepresentativePortalLocation(BlockArea portal)
    {
        Location l = portal.getCenter();

        l.setY(portal.minY+1);

        return l;
    }

    /**
     * Finds the portals representative location given a location nearby in in portal.
     */
    private static Location findPortalLocation(Location l, boolean xAligned) {
        //we add 1 to location because the location is at the base of the portal,
        //and findPortal only works if inside the portal
        return getRepresentativePortalLocation(findPortal(l.clone().add(0,1,0),xAligned));
    }

    public static World getNetherWorld() {
        return Bukkit.getWorld(NETHER_WORLD_NAME);
    }

    /**
     * Destroys the portal near the given location
     */
    public static boolean destroyPortal(Location loc, boolean hasExplosion) {
        BlockArea ba = findPortal(loc,true);
        if(ba == null) ba = findPortal(loc,false);

        if(ba == null)
            return false;

        destroyPortal(loc.getWorld(),ba, hasExplosion);

        return true;
    }

    /**
     * Finds a semi safe place to put a spawn location.
     * @return
     */
    public static Location getRandomSpawnLocation(World w) {
        Location loc;
        int y;
        do {
            loc =
                    //TODO 3 maybe make this allow for rare occurrences of spawning at far away places
                    // (x+k)^2 or something

                    new Location(w,
                            MAX_SPAWN_RADIUS * (Math.random() * 2 - 1),
                            0,
                            MAX_SPAWN_RADIUS * (Math.random() * 2 - 1)
                    );

            y = getValidHighestY(loc, SPAWN_LOCATION_BLACKLIST);
        }
        while(y == -1);

        loc.setY(y);
        return loc;
    }
}
//TODO 2 find why we are spawning underground sometimes
//TODO 2 find why we sometimes spawn in the wrong world on connect???