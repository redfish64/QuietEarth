package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
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
     * The id of the nether visited world
     */
    public static final int NETHER_VISITED_WORLD_ID = 1;
    private static final int NETHER_WORLD_ID = 1;

    /**
     * When deciding whether to recycle a world, the time a visited world with no
     * active players must be inactive before being recycled.
     * <p>Note that players may have teleported out of the world into the nether and may
     * want to come back later</p>
     */
    private static final long MAX_RECYCLE_LAST_PLAYER_LOG_NO_PLAYERS_MS = 1000 * 3600 * 24;

    /**
     * When deciding whether to recycle a world, the time a visited world must have no
     * log events before being recycled (with or without active players)
     * */
    private static final long MAX_RECYCLE_LAST_PLAYER_LOG_MS = 1000 * 3600 * 24 * 14;

    private final QuietCraftPlugin qcp;

    /**
     * All visited worlds of server. There is always one active visitedworld per world
     */
    public List<QCVisitedWorld> visitedWorlds;
    public QCVisitedWorld netherVisitedWorld;

    public WorldManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        visitedWorlds = new ArrayList<>();
        visitedWorlds.addAll(qcp.getDatabase().find(QCVisitedWorld.class).findList());

        for (QCVisitedWorld vw : visitedWorlds) {
            //we must call createworld for each world in setup, otherwise we won't be able
            //to respawn to it during gameplay
            //Calling createWorld() whether it exists or not, freezes the entire server for 10-20 seconds
            //so we have to do this ahead of time. A waste of memory? Yes.
            //Alternatives? None that are so good as far as I can tell...
            //Note that I tried 30 worlds and it clocked in under 2 gig, so I'm just going to leave it this
            //way. There isn't much I can do. Maybe allow multiple servers, but I think if we can get abandoned
            //worlds to work, we'll be ok
            //TODO 4 Can we fix this somehow
            WorldCreator c = new WorldCreator(vw.getName());
            c.createWorld();

            //find and populate the netherWorld
            //note that if this is a fresh install, then worlds will be size 0, and netherWorld won't
            //be populated until setupForNewInstall() is called.
            if (vw.getId() == NETHER_VISITED_WORLD_ID)
                netherVisitedWorld = vw;
        }
    }

    /**
     * Finds the best world for a newbie to join.
     */
    public QCVisitedWorld findFirstJoinVisitedWorld() {
        QCVisitedWorld bestW = null;

        for (QCVisitedWorld w : visitedWorlds) {
            if (w == netherVisitedWorld)
                continue;

            if (bestW == null)
                bestW = w;
//            else if (bestW.currentPlayerCount < w.currentPlayerCount)
//                bestW = w;
//            else if (bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
//                bestW = w;
        }

        return bestW;
    }

    /**
     * This should be called periodically (every day or so). It looks for worlds that haven't been visited in a long time
     * most likely due to all active players already visiting it and changes its world id and spawn
     * point to recycle it.
     */
    public void createNewVisitedWorlds()
    {
        EbeanServer db = qcp.getDatabase();

        db.beginTransaction();
        try {
            //find the worlds that haven't had activity since ABANDONED_WORLD_CUTOFF_MS
            SqlQuery q = db.
                    createSqlQuery("select world_visit_id, max(timestamp) from qc_player_log " +
                            "group by world_visit_id having max(timestamp) < :timestamp_cutoff " +
                            "order by max(timestamp)");
            q.setParameter("timestamp_cutoff", System.currentTimeMillis() - ABANDONED_WORLD_CUTOFF_MS);

            //create new active worlds for the ones that have been abandoned
            List<SqlRow> visitedWorldIdSqlRows = q.findList();
            for (SqlRow sr : visitedWorldIdSqlRows) {
                int visitedWorldId = sr.getInteger("visited_world_id");

                for(int i = 0 ; i < visitedWorlds.size(); i++)
                {
                    QCVisitedWorld vw = getQCVisitedWorld(visitedWorldId);

                    vw.setActive(false);
                    db.update(vw);
                    //TODO 2 FIXME
//                    QCVisitedWorld copy = vw.createCopy(
//                            createRandomSpawnQCLocation(getWorld(vw)),
//                            createRandomNetherSpawnQCLocation());
//                    copy.setActive(true);
//                    db.insert(copy);
                }
            }
            db.commitTransaction();
        }
        finally
        {
            db.endTransaction();
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
     * @return visited world, if any
     */
    public QCVisitedWorld findBestActiveWorldForPlayer(String playerId) {
        //see javadoc of this method for a description of what this does
        SqlQuery q = qcp.db.
                createSqlQuery(
                        " select id,\n" +
                                "   (select count(*) from qc_player p where p.visited_world_id = vw.id) as pc, \n" +
                                "   (select count (*) from qc_player_log where visited_world_id = vw.id) as plc\n" +
                                "  from qc_visited_world vw where vw.active = 1 and vw.id not in\n" +
                                "    (select visited_world_id from qc_player_log where player_id = :pid)\n" +
                                "    and vw.id != "+NETHER_VISITED_WORLD_ID+"\n" +
                                "    order by pc desc,plc desc limit 1;\n");

        q.setParameter(1,playerId);
        SqlRow sr = q.findUnique();
        if(sr == null)
            return null;

        return getQCVisitedWorld(sr.getInteger("id"));
    }

    /**
     * Finds the best world to join for a player that just died to join.
     * <p>If the player visited all visited worlds, look for an existing
     * world where the visited world can be destroyed.</p>
     */
    public QCVisitedWorld findOrCreateBestWorldForDeadPlayer(Player player) {
        if(DbUtil.findNumberOfDeathsInTimePeriodForPlayer(player.getUniqueId().toString(),
                MAX_DEATHS_BEFORE_NETHER_SPAWN_MS) > MAX_DEATHS_BEFORE_NETHER_SPAWN )
        {
            Bukkit.getLogger().info("Player "+player+" died too much and must go to the nether");
            return netherVisitedWorld;
        }

        //find the best world that already exists
        QCVisitedWorld bestVW = findBestActiveWorldForPlayer(player.getUniqueId().toString());

        if(bestVW != null)
            return bestVW;

        //recycle a visited world (if we can)
        QCVisitedWorld bestVWToRecycle = findBestVisitedWorldToRecycle();

        if(bestVWToRecycle == null)
        {
            Bukkit.getLogger().info("No worlds left to recycle, sending player to nether");
            return netherVisitedWorld;
        }

        return recycleVisitedWorld(bestVWToRecycle);
    }

    //TODO 2 can't create a portal until 5 days have passed (prevent soul farming)

    //TODO 2 get rid of max deaths

    private QCVisitedWorld recycleVisitedWorld(QCVisitedWorld bestVWToRecycle) {
        qcp.db.beginTransaction();
        try {
//TODO 1
        }
        return null;
    }

    /**
     * Finds the visited world which is eligible for recycling and is the best candidate
     * according to various criteria
     */
    private QCVisitedWorld findBestVisitedWorldToRecycle() {

        //finds the visited world that
        // * is active and is not the nether world
        // * and has the least amount of active players, and for an equal number of active players,
        // *   is been inactive the longest amount of time
        // * and
        // *   (there are no active players and no one teleported out
        // *       or
        // *    there are no active players and someone teleported out and
        // *            the world has been inactive for MAX_RECYCLE_LAST_PLAYER_LOG_NO_PLAYERS_MS
        // *       or
        // *    there are active players and the world has been inactive for
        // *       MAX_RECYCLE_LAST_PLAYER_LOG_MS)
        SqlQuery q = qcp.db.
                createSqlQuery(
                        "select vw.id as id,\n" +
                                "       (select count(*) from qc_player p where p.visited_world_id = vw.id) as active_players,\n" +
                                "       ifnull((select max(timestamp) from qc_player_log l where l.visited_world_id = vw.id),0) as last_action\n" +
                                "       from qc_visited_world vw\n" +
                                "       where vw.id != "+NETHER_VISITED_WORLD_ID+\n" +
                                "       and active = 1\n"+
                                "       and (active_players = 0 and\n" +
                                "              (not exists (select 'x' from qc_player_log l where action = 'L' and l.visited_world_id = vw.id)\n" +
                                "                or last_action < "+MAX_RECYCLE_LAST_PLAYER_LOG_NO_PLAYERS_MS+")\n" +
                                "           or last_action < "+MAX_RECYCLE_LAST_PLAYER_LOG_MS+")\n" +
                                " order by active_players, last_action;\n"
                );
        SqlRow sr = q.findUnique();
        if(sr != null)
            return getQCVisitedWorld(sr.getInteger("id"));

        return null;
    }

    private void sendPlayerToNether(Player p) {
        World nether = Bukkit.getWorld("world_nether");
        Location loc = nether.getSpawnLocation();
        p.teleport(loc);
    }

    public QCVisitedWorld getQCVisitedWorld(long id) {
        for (QCVisitedWorld w : visitedWorlds) {
            if (w.getId() == id)
                return w;
        }

        return null;
    }

    /**
     *
     * @return the active visited world for the given world name
     */
    public QCVisitedWorld getQCVisitedWorld(String worldName) {
        for (QCVisitedWorld w : visitedWorlds) {
            if (w.getName().equals(worldName))
                return w;
        }

        return null;
    }

    public void setupForNewInstall() {
        QCWorld w = new QCWorld(NETHER_WORLD_ID, WorldUtil.NETHER_WORLD_NAME);
        netherVisitedWorld = new QCVisitedWorld(NETHER_VISITED_WORLD_ID,w.getId(),0,0,0,0,0,0,true);
        qcp.db.insert(w);
        qcp.db.insert(netherVisitedWorld);
        visitedWorlds.add(netherVisitedWorld);

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
        int id = visitedWorlds.size() + 1;

        if (name == null) {
            name = "world_qc" + (id-1); //TODO 3 maybe come up with a random name generator
        }

        qcp.getLogger().info("Starting world creation for " + name + "...");
        WorldCreator c = new WorldCreator(name);
        //TODO 3 randomize world, maybe use generators from other plugins, etc.
        World w = c.createWorld();
        qcp.getLogger().info("Finished world creation for " + name);

        QCWorld qcw = new QCWorld(id,name);

        qcp.getDatabase().insert(qcw);

        Location spawnLocation = WorldUtil.getRandomSpawnLocation(w);

        QCVisitedWorld vw = new QCVisitedWorld(id, qcw,
                spawnLocation,
                createRandomNetherSpawnLocation(), true);
        qcp.getDatabase().insert(vw);

        visitedWorlds.add(vw);

        w.setSpawnLocation(
                (int)Math.floor(spawnLocation.getX()),
                (int)Math.floor(spawnLocation.getY()),
                (int)Math.floor(spawnLocation.getZ())
        );

        return qcw;
    }

    /**
     * Creates a random spawn location in the nether world.
     *
     * @return created location
     */
    public Location createRandomNetherSpawnLocation() {
        World w = getWorld(netherVisitedWorld);

        return WorldUtil.getRandomSpawnLocation(w);
    }

    private World getWorld(QCVisitedWorld vw) {
        return Bukkit.getWorld(vw.getName());
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
                        qcp.portalManager.createPortalLink(netherVisitedWorld.getId(), portalLocation,
                                qcPlayer.getVisitedWorldId(),
                                overWorldPortalLocation);
                QuietCraftPlugin.db.save(pl);

                QuietCraftPlugin.db.commitTransaction();
            }
            finally {
                QuietCraftPlugin.db.endTransaction();
            }

            return;
        }//if creating portal from nether world

        QCVisitedWorld visitedWorld = qcPlayer.getVisitedWorld();

        QuietCraftPlugin.db.beginTransaction();
        try {
            Location netherWorldLocation =
                    visitedWorld.getNetherLocation();

            QCPortalLink existingPortalLink = qcp.portalManager.
                    getPortalLinkForLocation(netherWorldLocation);
            if(existingPortalLink == null)
                constructNetherWorldPortal(netherWorldLocation, true);
            else
                Bukkit.getLogger().info("Not constructing nether world portal because " +
                        "a portal link already exists: "+existingPortalLink);

            //create a link from the current location to the nether world.
            QCPortalLink pl = qcp.portalManager.createPortalLink(
                    qcPlayer.getVisitedWorldId(),
                    portalLocation,
                    netherVisitedWorld.getId(),
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
