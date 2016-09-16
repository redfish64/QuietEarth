package com.rareventure.quietcraft;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.rareventure.quietcraft.utils.BlockArea;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

//TODO 2 update visit id and change spawn point of worlds when visit id changes
//TODO 2.5 possibly notify everybody in the nether where the portal opened, and give them a way to see their
// own location, so they can attack it.

//TODO 2 handle creation of new visit worlds to recycle old worlds
//TODO 3 delete all player logs where visit ids no longer exist to save space??


//TODO 2 decide on random distance for the following. Do we want them close or far away from each other?:
//  nether spawn
//  portals to nether
//  portals from nether (wild portals)
//  reused world spawn

//TODO 3 flying admin user

//TODO 2 if we allow world reuse, handle cases where a user doesn't log in for a long time
// and the world they were in no longer exists

//TODO 3 new rule: when in nether with at least one soul, all souls drop in nether,
// player is teleported back to world (so dying in nether isn't so harsh and allows player to
// try and recover souls)


//TODO 2 see if we can change configuration on the fly so we can properly alpha

/**
 * Manages worlds and visited worlds.
 */
public class WorldManager {
    private static final int MAX_WORLDS = 3;

    /**
     * The amount of time before we start recycling old worlds as new
     */
    private static final long ABANDONED_WORLD_CUTOFF_MS = 1000L * 60L * 60L * 24L * 30L
            ;

    //TODO 2.5 saying "hello sailor" should kill player and send them to the nether (they'll respawn in the next
    // world when they die in the nether)
    // should check for 17173 speak (1's as l's, etc.) as well, maybe

    /**
     * The max distance from a portal for a player to have a portal key in order to
     * have the portal created when lit.
     */
    private static final int MAX_KEY_PORTAL_DISTANCE = 5;

    private static final MathUtil.RandomNormalParams NETHER_PORTAL_WIDTH_PARAMS =
            new MathUtil.RandomNormalParams(4,3,4,15);
    /**
     * The ratio between height and width. It's important to make sure that
     * given the minimum width in NETHER_PORTAL_WIDTH_PARAMS, that this
     * value creates a valid height.
     */
    private static final double PORTAL_HEIGHT_TO_WIDTH = 6f/4f;
    private static final MathUtil.RandomNormalParams OVERWORLD_PORTAL_WIDTH_PARAMS =
            new MathUtil.RandomNormalParams(4,1,4,7);

    /**
     * The id of the nether world
     */
    private static final int NETHER_WORLD_ID = 1;

    /**
     * When deciding whether to recycle a world, the time a visited world must have no
     * log events before being recycled (with or without active players)
     * */
    private static final long MAX_RECYCLE_LAST_PLAYER_LOG_MS = 1000 * 3600 * 24 * 14;

    private final QuietCraftPlugin qcp;

    /**
     * All worlds of the server
     */
    public List<QCWorld> worlds;
    public QCWorld netherQCWorld;

    public WorldManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        worlds = new ArrayList<>();
        worlds.addAll(qcp.getDatabase().find(QCWorld.class).findList());

        for (QCWorld w : worlds) {
            //we must call createworld for each world in setup, otherwise we won't be able
            //to respawn to it during gameplay
            //Calling createWorld() whether it exists or not, freezes the entire server for 10-20 seconds
            //so we have to do this ahead of time. A waste of memory? Yes.
            //Alternatives? None that are so good as far as I can tell...
            //Note that I tried 30 worlds and it clocked in under 2 gig, so I'm just going to leave it this
            //way. There isn't much I can do. Maybe allow multiple servers, but I think if we can get abandoned
            //worlds to work, we'll be ok
            //TODO 4 Can we fix this somehow
            WorldCreator c = new WorldCreator(w.getName());
            c.createWorld();

            //find and populate the netherQCWorld
            //note that if this is a fresh install, then worlds will be size 0, and netherQCWorld won't
            //be populated until setupForNewInstall() is called.
            if (w.getId() == NETHER_WORLD_ID)
                netherQCWorld = w;
        }
    }

    //TODO 2 create table indexes

    /**
     * Finds the visited world that:
     * <ul>
     * <li>either has the most live players currently,
     * or if tied, then the world with the most events,</li>
     * <li>and the player hasn't visited the world</li>
     * <li>and is not the nether world</li>
     * <li>and is active</li>
     *
     * @param playerId it is allowed for the player id to be of a player that has not
     *                 yet joined (doesn't have a QCPlayer record)
     * @return visited world, if any
     */
    public QCWorld findBestActiveWorldForPlayer(String playerId) {
        //see javadoc of this method for a description of what this does
        SqlQuery q = qcp.db.
                createSqlQuery(
                        " select id,\n" +
                                "   (select count(*) from qc_player p where p.visited_world_id = vw.id) as pc, \n" +
                                "   (select count (*) from qc_player_log where visited_world_id = vw.id) as plc\n" +
                                "  from qc_visited_world vw where vw.active = 1 and vw.id not in\n" +
                                "    (select visited_world_id from qc_player_log where player_id = :pid)\n" +
                                "    and vw.id != "+NETHER_WORLD_ID+"\n" +
                                "    order by pc desc,plc desc limit 1;\n");

        q.setParameter(1,playerId);
        SqlRow sr = q.findUnique();
        if(sr == null)
            return null;

        return getQCWorld(sr.getInteger("id"));
    }

    /**
     * Finds the best world to join for a player that just died to join.
     * <p>If the player visited all visited worlds, look for an existing
     * world where the visited world can be destroyed.</p>
     */
    public QCWorld findOrCreateBestWorldForDeadPlayer(Player player) {
        //find the best world that already exists
        QCWorld bestW = findBestActiveWorldForPlayer(player.getUniqueId().toString());

        if(bestW != null)
            return bestW;

        //recycle a visited world (if we can)
        QCWorld bestWToRecycle = findBestWorldToRecycle();

        if(bestWToRecycle == null)
        {
            Bukkit.getLogger().info("No worlds left to recycle, sending player to nether");
            return netherQCWorld;
        }

        return recycleWorld(bestWToRecycle);
    }

    //TODO 2 can't create a portal until 5 days have passed (prevent soul farming)

    //TODO 2 player doesn't switch worlds unless they sleep in a bed in the new one

    /**
     * Recycles the world. This changes the spawn and nether points and increments
     * the world counter
     *
     * @param w
     * @return
     */
    private QCWorld recycleWorld(QCWorld w) {
        //TODO 1 FIXME
        qcp.db.beginTransaction();
        try {

        }
        finally {
            qcp.db.endTransaction();
        }
        return null;
    }

    /**
     * Finds the world which is eligible for recycling and is the best candidate
     * according to various criteria
     */
    private QCWorld findBestWorldToRecycle() {

        //finds the visited world that
        // * is active and is not the nether world
        // * and has the least amount of active players, and for an equal number of active players,
        // *   is been inactive the longest amount of time
        // * and
        // *    there are active players and the world has been inactive for
        // *       MAX_RECYCLE_LAST_PLAYER_LOG_MS)
        SqlQuery q = qcp.db.
                createSqlQuery(
        "select w.id as id,\n" +
        "       (select count(*) from qc_player p where p.world_id = w.id) as active_players,\n" +
        "       ifnull((select max(timestamp) from qc_player_log l where l.world_id = w.id),0) as last_action\n" +
        "       from qc_world w\n" +
        "       where w.id != "+NETHER_WORLD_ID+"\n" +
        "       and active = 1\n"+
        "       and (active_players = 0 " +
        " or last_action < "+MAX_RECYCLE_LAST_PLAYER_LOG_MS+")\n" +
        " order by active_players, last_action limit 1;\n"
                );
        SqlRow sr = q.findUnique();
        if(sr != null)
            return getQCWorld(sr.getInteger("id"));

        return null;
    }

    private void sendPlayerToNether(Player p) {
        World nether = Bukkit.getWorld("world_nether");
        Location loc = nether.getSpawnLocation();
        p.teleport(loc);
    }

    public QCWorld getQCWorld(long id) {
        for (QCWorld w : worlds) {
            if (w.getId() == id)
                return w;
        }

        return null;
    }

    public void setupForNewInstall() {
        QCWorld w = new QCWorld(WorldUtil.NETHER_WORLD_NAME,0,0,0,0,0,0);
        w.setId(NETHER_WORLD_ID);
        qcp.db.insert(w);
        qcp.db.insert(netherQCWorld);
        worlds.add(netherQCWorld);

        //create a bunch of empty worlds
        //we can't create worlds adhoc, or it freezes the server while the creation is in process
        //so we create them ahead of time
        //TODO 2.5 different types of worlds???
        for (int i = 0; i < MAX_WORLDS; i++)
            createNewWorld();
    }

    public QCWorld createNewWorld() {
        return createNewWorld(null);
    }

    /**
     * Creates a new world and a new active visited world to go with it.
     *
     * @param name may be null
     * @return the visited world
     */
    //TODO 3 add a worldcreatorparams object for setting up different parameters for new world generation
    //(especially for hell and purgatory)
    public synchronized QCWorld createNewWorld(String name) {
        int id = worlds.size() + 1;

        if (name == null) {
            name = "world_qc" + (id-1); //TODO 3 maybe come up with a random name generator
        }

        qcp.getLogger().info("Starting world creation for " + name + "...");
        WorldCreator c = new WorldCreator(name);
        //TODO 3 randomize world, maybe use generators from other plugins, etc.
        World w = c.createWorld();
        qcp.getLogger().info("Finished world creation for " + name);

        Location spawnLocation = WorldUtil.getRandomSpawnLocation(w);
        Location randomNetherPortalLocation = createRandomNetherPortalLocation();
        QCWorld qcw = new QCWorld(name, spawnLocation, randomNetherPortalLocation);
        qcw.setId(id);

        qcp.getDatabase().insert(qcw);

        return qcw;
    }

    //TODO 2 when a world is recycled, all the old portals must be destroyed. Otherwise
    //it would allow random people to enter the world if the new owners didn't know about
    //the old portals

    /**
     * Creates a random portal location in the nether world.
     * All world portals will come here
     *
     * @return created location
     */
    public Location createRandomNetherPortalLocation() {
        World w = getWorld(netherQCWorld);

        return WorldUtil.getRandomSpawnLocation(w);
    }

    private World getWorld(QCWorld w) {
        return Bukkit.getWorld(w.getName());
    }


    //TODO 3 allow souls to be split into soul fragments, and those split again
    //to make a soul economy

    /**
     * Called when a portal is created.
     */
    public void onPortalCreate(PortalCreateEvent event) {
        Map.Entry<Player,ItemStack> playerAndPortalKey = getPortalKeyNearPortal(event.getBlocks());

        //portal was created without a portal key
        if(playerAndPortalKey == null) {
            qcp.getLogger().info("denied portal creation");
            event.setCancelled(true);

            //we have to destroy the portal somehow. Otherwise people could
            //create long standing non working portals, and when someone with a portal
            //key walks by unknowningly, they portal would be created
            WorldUtil.destroyPortal(event.getBlocks(), true);
            return;
        }

        QCPlayer qcPlayer = qcp.pm.getQCPlayer(playerAndPortalKey.getKey());

        BlockArea ba = WorldUtil.getPortalArea(event.getBlocks());
        Location portalLocation = WorldUtil.getRepresentativePortalLocation(ba);

        //if we are creating a portal in the nether, we choose the world to go to
        //based on the portal key used.
        if(WorldUtil.isNetherWorld(event.getWorld().getName()))
        {
            World overWorld = Bukkit.getWorld(getWorldNameForPortalKey(playerAndPortalKey.getValue()));

            // netherworld portals go to a random place in the overworld
            // (the idea is that getting a portal key from another player is valuable and can
            //  be used to sneak into the overworld)
            Location overWorldPortalLocation = WorldUtil.getRandomSpawnLocation(overWorld);

            constructOverWorldPortal(overWorldPortalLocation, true);

            QuietCraftPlugin.db.beginTransaction();
            try {
                //destroy any existing portals that exist in the same spot
                qcp.portalManager.getPortalLinksForLocation(portalLocation).
                        forEach(this::destroyPortalsForPortalLink);
                qcp.portalManager.getPortalLinksForLocation(overWorldPortalLocation).
                        forEach(this::destroyPortalsForPortalLink);


                //create a link from the current location to the nether world.
                QCPortalLink pl =
                        qcp.portalManager.createPortalLink(netherQCWorld.getId(),
                                portalLocation,
                                qcPlayer.getWorldId(),
                                overWorldPortalLocation);
                QuietCraftPlugin.db.save(pl);

                QuietCraftPlugin.db.commitTransaction();
            }
            finally {
                QuietCraftPlugin.db.endTransaction();
            }

            return;
        }//if creating portal from nether world

        QCWorld qcWorld = qcPlayer.getWorld();

        QuietCraftPlugin.db.beginTransaction();
        try {
            Location netherWorldLocation =
                    qcWorld.getNetherLocation();

            QCPortalLink existingPortalLink = qcp.portalManager.
                    getPortalLinkForLocation(netherWorldLocation);
            if(existingPortalLink == null)
                constructNetherWorldPortal(netherWorldLocation, true);
            else
                Bukkit.getLogger().info("Not constructing nether world portal because " +
                        "a portal link already exists: "+existingPortalLink);

            //create a link from the current location to the nether world.
            QCPortalLink pl = qcp.portalManager.createPortalLink(
                    qcPlayer.getWorldId(),
                    portalLocation,
                    netherQCWorld.getId(),
                    netherWorldLocation);
            QuietCraftPlugin.db.save(pl);

            QuietCraftPlugin.db.commitTransaction();
        }
        finally {
            QuietCraftPlugin.db.endTransaction();
        }
    }
//TODO 2 make sure that players spawn in populated worlds first!
    /**
     * Destroys portals on both sides of a portal link, and then deletes the portal link,
     * and qclocations from the database
     */
    private void destroyPortalsForPortalLink(QCPortalLink pl) {
        WorldUtil.destroyPortal(pl.getLoc1(),true);
        WorldUtil.destroyPortal(pl.getLoc2(),true);

        QuietCraftPlugin.db.delete(pl);
    }

    /**
     * Creates a portal for the nether world
     */
    private void constructNetherWorldPortal(Location netherWorldLocation, boolean active) {
        int width = MathUtil.normalRandomInt(NETHER_PORTAL_WIDTH_PARAMS);

        WorldUtil.constructPortal(netherWorldLocation, width,
                (int)Math.round(width* PORTAL_HEIGHT_TO_WIDTH),
                true, //TODO 3.5 if we let this be false, portal blocks face sideways
                //Math.random() < .5,
                active, true //we add ceilings to nether worlds incase there is lava falling down
                // from the ceiling. Since each world only gets one nether location that can't be
                // moved, we want to make it extra safe
        );
    }

    /**
     * Creates a portal for the overworld
     */
    private void constructOverWorldPortal(Location overWorldLocation, boolean active) {
        int width = MathUtil.normalRandomInt(OVERWORLD_PORTAL_WIDTH_PARAMS);

        WorldUtil.constructPortal(overWorldLocation, width,
                (int)Math.round(width* PORTAL_HEIGHT_TO_WIDTH),
                true, //TODO 3.5 if we let this be false, portal blocks face sideways
                //Math.random() < .5,
                active, false);
    }

    private static String getWorldNameForPortalKey(ItemStack portalKey) {
        String s = portalKey.getItemMeta().getDisplayName();

        return s.substring(0, s.length() - PlayerManager.PORTAL_KEY_DISPLAY_NAME_ENDING.length());
    }

    /**
     * Looks for a player holding a portal key near the portal creation.
     *
     * @param blocks blocks of portal
     * @return name of world of portal key
     */
    private Map.Entry<Player,ItemStack> getPortalKeyNearPortal(ArrayList<Block> blocks) {
        BlockArea ba = new BlockArea();

        //first get the rectangle of the portal
        for(Block b : blocks)
        {
            ba.expandArea(b.getLocation());
        }

        //we can't actually tell who let the fire, so we just look for nearby players
        //for the portal key
        for(Player p : PlayerManager.getNearbyPlayers(ba.getCenter(),MAX_KEY_PORTAL_DISTANCE))
        {
            ItemStack portalKey = PlayerManager.getPortalKey(p);

            if(portalKey != null)
                return new AbstractMap.SimpleEntry<>(p, portalKey);
        }

        return null;
    }

    public void onBlockPhysicsEvent(BlockPhysicsEvent e) {
        clearStaleConstructedPortals();

        if(e.getBlock().getType() == Material.PORTAL) {
            //look to see if we have recently begun constructing any portals (automatically)
            //and if so, cancel any physics events associated with them
            synchronized (WorldUtil.recentConstructedPortalAreas) {
                //if we cancelled the physics event, we return immediately
                if(
                    WorldUtil.recentConstructedPortalAreas.stream().anyMatch(ba -> {
                        if (ba.containsBlock(e.getBlock())) {
                            e.setCancelled(true);
                            return true;
                        }
                        return false;
                    })
                        )
                    return;
            }

            //now we need to check if any existing portals link to the given nether block
            //if so, we need to delete the link and possibly delete the other side of the portal
            qcp.portalManager.getPortalLinksForLocation(e.getBlock().getLocation())
                    .forEach(pl -> qcp.portalManager.destroyPortalLink(pl));
        }


        //TODO 2 for multiple world portals linked to the same nether portal,
        //each player gets a last portal link entered db field, so they go back the same
        //place they left. Maybe 1 out of every 20 times, they go to a random portal linked
        //to the same
    }

    /**
     * This checks if enough time has passed that the constructed portals
     * should have gone through all the block physics events, and we can
     * stop blocking the event
     */
    private void clearStaleConstructedPortals() {
        synchronized(WorldUtil.recentConstructedPortalAreas) {
            //we give them one second
            if (WorldUtil.lastPortalConstructionMS + 1000 < System.currentTimeMillis())
                WorldUtil.recentConstructedPortalAreas.clear();
        }
    }
}
