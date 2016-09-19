package com.rareventure.quietcraft;

import com.google.common.collect.Sets;
import com.rareventure.quietcraft.utils.BlockArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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
    /**
     * List of portal areas that were recently constructed (or being constructed)
     * automatically. We keep track of this so that the block physics code doesn't
     * remove nether material blocks as we add them
     */
    static final List<BlockArea> recentConstructedPortalAreas = new ArrayList<>();
    /**
     * The last time a portal began automatic construction
     */
    static long lastPortalConstructionMS;

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

        Bukkit.getLogger().info("findPortal ba="+ba);
        return ba;
    }

    private static final int MAX_ACTIVE_PORTAL_LOC_DIST = 3;

    public static void createPortal()
    {

    }

    public static BlockArea getPortalArea(List<Block> blocks)
    {
        boolean alignedWithX = isPortalAlignedWithX(blocks);

        return WorldUtil.findPortal(new BlockArea(blocks).getCenter(), alignedWithX);
    }

    /**
     * Gives a bunch of blocks associated with a portal, finds out
     * whether the portal is aligned with the X dimension or the Z dimension
     *
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

    public static void destroyPortal(World w, BlockArea portalArea, double explosionSize) {
        portalArea.getBlocks(w).stream().filter(b -> b.getType() == Material.OBSIDIAN).
                forEach(b -> b.setType(Material.SAND));

        if(explosionSize > 0)
            w.createExplosion(portalArea.getCenter(), (float) (explosionSize
                                * portalArea.getLengthX()));

    }

    public static void destroyPortal(List<Block> blocks, double explosionSize) {
        destroyPortal(blocks.get(0).getWorld(), getPortalArea(blocks), explosionSize);
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
        if((b.getRelative(1, 0, 0).getType() == b.getType()) ||
                (b.getRelative(-1, 0, 0).getType() == b.getType())) return true;

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
     * Creates a portal with a floor, and optionally a ceiling. Also updates
     * recentConstructedPortalAreas for block area
     *
     *  @param l center of portal
     * @param w width of portal
     * @param h height of portal
     * @param xAligned if true, portal will be created along the x axis, otherwise along the z axis
     * @param activate if true, the portal will be active (contain PORTAL, not AIR)
     */
    public static void constructPortal(Location l, int w, int h, boolean xAligned, boolean activate
    ,boolean withCeiling) {
        Block b1 = l.getBlock();

        {
            BlockArea ba = new BlockArea();
            if(xAligned)
            {
                ba.expandArea(b1.getX() - w / 2, b1.getY() - 1, b1.getZ() - FLOOR_LENGTH);
                ba.expandArea(b1.getX() + w / 2, b1.getY() + h + 1, b1.getZ() + FLOOR_LENGTH);
            }
            else {
                ba.expandArea(b1.getX() - FLOOR_LENGTH, b1.getY() - 1, b1.getZ() - w / 2);
                ba.expandArea(b1.getX() + FLOOR_LENGTH, b1.getY() + h + 1, b1.getZ() + w / 2);
            }
            addConstructingBlockArea(ba);
        }

        //only odd sized portals are allowed, since we must center on location
        if(w % 2 == 0) w++;

        for(int x = 0; x < w; x++)
        {
            for(int y = 0; y < h; y++)
            {
                Block b;
                if(xAligned)
                    b = b1.getRelative(x - w/2,y-1,0);
                else
                    b = b1.getRelative(0,y-1,x - w/2);

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
        BlockArea ba = new BlockArea();
        if(xAligned) {
            ba.expandArea(b1.getX() - w / 2, b1.getY() - 2, b1.getZ() - FLOOR_LENGTH);
            ba.expandArea(b1.getX() + w / 2, b1.getY() - 2, b1.getZ() + FLOOR_LENGTH);
        }
        else {
            ba.expandArea(b1.getX() - FLOOR_LENGTH, b1.getY() - 2, b1.getZ() - w / 2);
            ba.expandArea(b1.getX() + FLOOR_LENGTH, b1.getY() - 2, b1.getZ() + w / 2);
        }
        Bukkit.getLogger().info("ba "+ba);
        Bukkit.getLogger().info("b1 "+b1.getX()+","+b1.getY()+","+b1.getZ());

        ba.getBlocks(b1.getWorld()).forEach(b ->
                {
                    b.setType(Material.SANDSTONE);
                });

        if(withCeiling) {
            ba.minY += h + 1;
            ba.maxY += h + 1;
            ba.getBlocks(b1.getWorld()).forEach(b -> b.setType(Material.SANDSTONE));
        }

        assert(WorldUtil.findPortalLocation(l,xAligned).equals(b1.getLocation())) : "portal location "
                +b1.getLocation()
                +" doesn't match findPortalLocation "+WorldUtil.findPortalLocation(l,xAligned)
                +" xAligned "+xAligned;
    }

    /**
     * Adds a block area to the structures currently being constructed.
     * Any physics events that occur within 1 second of portal blocks within
     * this construction will be ignored.
     */
    private static void addConstructingBlockArea(BlockArea ba) {
        synchronized (recentConstructedPortalAreas)
        {
            recentConstructedPortalAreas.add(ba);
            lastPortalConstructionMS = System.currentTimeMillis();
        }
    }

    /**
     * Returns the representative location of a portal, which is defined as the portals center in x and z,
     * and minY + 1). This would be the center location where a user could stand within the portal.
     */
    public static Location getRepresentativePortalLocation(BlockArea portal)
    {
        Location l = portal.getBlockCenter();

        l.setY(portal.minY+1);

        return l;
    }

    /**
     * Finds the portals representative location given a location nearby in in portal.
     */
    private static Location findPortalLocation(Location l, boolean xAligned) {
        //we add 1 to location because the location is at the base of the portal,
        //and findPortal only works if inside the portal
        BlockArea ba = findPortal(l.clone().add(0, 1, 0), xAligned);
        Location l2 = getRepresentativePortalLocation(ba);

        Bukkit.getLogger().info("findPortalLocation: ba="+ba+" loc "+l+" result "+l2);

        return l2;
    }

    public static World getNetherWorld() {
        return Bukkit.getWorld(NETHER_WORLD_NAME);
    }

    /**
     * Destroys the portal near the given location
     */
    public static boolean destroyPortal(Location loc, double explosionSize) {
        BlockArea ba = findPortal(loc,true);
        if(ba == null) ba = findPortal(loc,false);

        if(ba == null) {
            Bukkit.getLogger().info("No portal to destroy at " + loc);
            return false;
        }

        destroyPortal(loc.getWorld(),ba, explosionSize);

        return true;
    }

    /**
     * Finds a semi safe place to put a spawn location.
     */
    public static Location getRandomSpawnLocation(World w, MathUtil.RandomNormalParams rnp) {
        Location loc;
        int y;
        do {
            loc =
                    //TODO 3 maybe make this allow for rare occurrences of spawning at far away places
                    // (x+k)^2 or something

                    new Location(w,
                            MathUtil.normalRandomInt(rnp),
                            0,
                            MathUtil.normalRandomInt(rnp)
                    );

            loc.getChunk().load();

            y = getValidHighestY(loc, SPAWN_LOCATION_BLACKLIST);
        }
        while(y == -1);

        loc.setY(y);
        return loc;
    }

    public static boolean isAt(Location l, int loc1X, int loc1Y, int loc1Z) {
        return l.getBlockX() == loc1X
                && l.getBlockY() == loc1Y
                && l.getBlockZ() == loc1Z;
    }

    public static boolean isNetherWorld(String worldName) {
        return worldName.equals(NETHER_WORLD_NAME);
    }

    /**
     * Returns a location that is offset from given location so if a user
     * teleports there, they won't be smack dab inside a portal, but rather
     * in a reasonable sensible spot.
     * <p>The location should be the representative location of the portal</p>
     */
    public static Location findPortalTeleportPlaceForUser(Location l) {
        Location r = findPortalTeleportPlaceForUser2(l);
        //put user in the middle (.5) of the block loc, and -1 down which is below the lip of the portal
        r.add(.5, -1, .5);

        return r;
    }

    private static Location findPortalTeleportPlaceForUser2(Location l) {
        Block cb = l.getBlock();

        //-x direction
        Block b = cb.getRelative(-1,0,0);
        if(b.getType() != Material.PORTAL)
        {
            Location r = b.getLocation();
            r.setDirection(new Vector(-1,0,0));

            return r;
        }

        //+x direction
        b = cb.getRelative(1,0,0);
        if(b.getType() != Material.PORTAL)
        {
            Location r = b.getLocation();
            r.setDirection(new Vector(1,0,0));

            return r;
        }

        //-z direction
        b = cb.getRelative(0,0,-1);
        if(b.getType() != Material.PORTAL)
        {
            Location r = b.getLocation();
            r.setDirection(new Vector(0,0,-1));

            return r;
        }

        //+z direction
        b = cb.getRelative(0,0,1);
        if(b.getType() != Material.PORTAL)
        {
            Location r = b.getLocation();
            r.setDirection(new Vector(0,0,1));

            return r;
        }

        Bukkit.getLogger().warning("Couldn't find air around portal, choosing original location: "+l);
        return l;
    }

    /**
     * Creates some air and puts a place for our feeties at location safe to teleport to
     */
    public static void makeTeleportLocationSafe(Location teleportLocation) {
        Block b = teleportLocation.getBlock();
        b.setType(Material.AIR);
        b.getRelative(0,1,0).setType(Material.AIR);
        Block f = b.getRelative(0,-1,0);

        //TODO 3 make safer?? what about fire?? how safe is too safe
        if(!isBlockStandable(f))
            f.setType(Material.SANDSTONE);
    }

    /**
     * True if can stand on the block, not water, lava, on fire, etc.
     */
    private static boolean isBlockStandable(Block f) {
        Material m = f.getType();
        if(!m.isSolid()) return false;

        if(f.isLiquid()) return false;
        return true;
    }

    public static final String [] HORIZONTAL_DIRS = {
            "ahead","ahead right","right","behind right",
            "behind","behind left","left","ahead left"};
    public static double DIR_DEGREES = 360./HORIZONTAL_DIRS.length;

    /**
     * Given a vector to an event source, and a yaw angle, returns a text description of where
     * the location is relative to the vector.
     */
    public static String getTextDirectionFromSource(Vector vecToSource, float yawOfReceiver) {
        double yawOfVector = MathUtil.getYawOfVector(vecToSource);
        double pitchOfVector = MathUtil.getPitchOfVector(vecToSource);

        if(pitchOfVector > 60)
        {
            return "down";
        }
        if(pitchOfVector < -60)
        {
            return "up";
        }

        StringBuffer result = new StringBuffer();
        if(pitchOfVector > 25)
            result.append("down ");
        if(pitchOfVector < -25)
            result.append("up ");

        //get angle of event in the perspective of the user (on the horizontal plane)
        double yaw = yawOfVector - yawOfReceiver;

        int index = (int)Math.floor(((yaw + DIR_DEGREES/2 + 720) % 360)/DIR_DEGREES);

        return result.append(HORIZONTAL_DIRS[index]).toString();
    }

    public static boolean testAdminPlayer(CommandSender sender) {
        if(sender instanceof Player)
        {
            if(sender.hasPermission("quietcraftadmin"))
                return true;
            sender.sendMessage("You don't have quietcraftadmin permissions");
        }
        else
            sender.sendMessage("Only a player can do that");

        return false;
    }
}
