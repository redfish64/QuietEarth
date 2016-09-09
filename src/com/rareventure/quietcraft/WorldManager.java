package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO 2 revisit spawn points. Also beds. When a player dies who is a leader in the community, we don't want them
// to have to respawn at the spawn point, so that the spawn point can be booby trapped without affecting long standing
// members

//TODO 2 update visit id and change spawn point of worlds when visit id changes
//TODO 2 spawn players randomly in the nether. We can't have a booby trapped spawn point in the nether or it'd
//be no fun
//TODO 2 when opening a portal to the nether, randomize the location of the portal in the nether.
//TODO 2.5 possibly notify everybody in the nether where the portal opened, and give them a way to see their
// own location, so they can attack it.

//TODO 2 setup choosing a world using visit ids.
//TODO 2 Update visit ids periodically (once a day or so)
//TODO 3 delete all player logs where visit ids no longer exist to save space??

/**
 * Created by tim on 7/10/16.
 */
public class WorldManager {
    private static final int MAX_WORLDS = 3;

    /**
     * The amount of time before we start recycling old worlds as new
     */
    private static final long ABANDONED_WORLD_CUTOFF_MS = 1000l * 60l * 60l * 24l * 30l
            ;

    //TODO 2.5 saying "hello sailor" should kill player and send them to the nether (they'll respawn in the next
    // world when they die in the nether)
    // should check for 17173 speak (1's as l's, etc.) as well, maybe

    /**
     * The max distance from a portal for a player to have a portal key in order to
     * have the portal created when lit.
     */
    private static final int MAX_KEY_PORTAL_DISTANCE = 5;

    /**
     * Maximum distance from 0,0,0 for random spawn locations
     */
    private static final double MAX_SPAWN_RADIUS = 10000.;
    private final QuietCraftPlugin qcp;

    /**
     * All visited worlds of server. There is always one active visitedworld per world
     */
    public List<QCVisitedWorld> visitedWorlds;
    public QCVisitedWorld netherVisitedWorld;

    public WorldManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        visitedWorlds = new ArrayList<QCVisitedWorld>();
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
            if (vw.getId() == 1)
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
                    QCVisitedWorld copy = vw.createCopy(
                            createRandomSpawnLocation(getWorld(vw)),
                            createRandomNetherSpawnLocation());
                    copy.setActive(true);
                    db.insert(copy);
                }
            }
            db.commitTransaction();
        }
        finally
        {
            db.endTransaction();
        }
    }

    /**
     * Finds the best world to join for a player that just died to join.
     */
    public QCVisitedWorld findBestWorldForDeadPlayer(Player player) {
        SqlQuery q = qcp.getDatabase().createSqlQuery("select distinct world_visit_id from qc_player_log where player_uuid = :uuid");
        q.setParameter("uuid", player.getUniqueId().toString());

        //finds out the worlds the player already visited
        Set<SqlRow> visitedWorldIdSqlRows = q.findSet();
        Set<Long> visitedWorldIds = new HashSet<Long>(visitedWorldIdSqlRows.size());
        for (SqlRow sr : visitedWorldIdSqlRows) {
            visitedWorldIds.add(sr.getLong("world_visit_id"));
        }

        QCVisitedWorld bestW = null;

        for (QCVisitedWorld w : visitedWorlds) {
            //the player doesn't go to the nether until he's not allowed anywhere else
            if (w == netherVisitedWorld)
                continue;

            //don't choose a world that has been spawned in already
            if (visitedWorldIds.contains(w.getId()))
                continue;

            if (bestW == null)
                bestW = w;
            //TODO 2 make choosing next world use better statistics to find more active worlds
//            else if (bestW.currentPlayerCount < w.currentPlayerCount)
//                bestW = w;
//            else
//                //choose the most visited world.. since it is the most interesting
//                if (bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
//                    bestW = w;
        }

        //if there is no good world for the player anymore, we just send them to the nether
        if (bestW == null) {
            return netherVisitedWorld;
        }

        return bestW;
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

    public void setupForNewInstall() {
        QCWorld w;
        netherVisitedWorld = new QCVisitedWorld(1,w = new QCWorld(1,"world_nether"),null,null,true);
        qcp.getDatabase().insert(w);
        qcp.getDatabase().insert(netherVisitedWorld);
        visitedWorlds.add(netherVisitedWorld);

        //create a bunch of empty worlds
        //we can't create worlds adhoc, or it freezes the server while the creation is in process
        //so we create them ahead of time
        //TODO 2 different types of worlds???
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

        QCVisitedWorld vw = new QCVisitedWorld(id, qcw,
                createRandomSpawnLocation(w),
                createRandomNetherSpawnLocation(), true);

        visitedWorlds.add(vw);

        return qcw;
    }

    private QCLocation createRandomNetherSpawnLocation() {
        World w = getWorld(netherVisitedWorld);

        return createRandomSpawnLocation(w);
    }

    /**
     * Places we make sure the spawn location is not at. Note that we are a little rougher here and allow
     * water as a place to spawn above *splash*, as well as cacti.
     */
    private static final HashSet<Material> SPAWN_LOCATION_BLACKLIST = Sets.newHashSet(Material.LAVA,Material.STATIONARY_LAVA);

    private World getWorld(QCVisitedWorld vw) {
        return Bukkit.getWorld(vw.getName());
    }

    /**
     * Finds a semi safe place to put a spawn location
     * @return
     */
    private QCLocation createRandomSpawnLocation(World w) {
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

            y = Util.getValidHighestY(loc, SPAWN_LOCATION_BLACKLIST);
        }
        while(y == -1);

        return new QCLocation(loc);
    }

    public void onPortalCreate(PortalCreateEvent event) {
        if(!isPortalValid(event.getBlocks())) {
            qcp.getLogger().info("denied portal creation");
            event.setCancelled(true);
//            List<Block> blocks = event.getBlocks();
//            for(Block b : blocks)
//            {
//                b.setType(Material.SAND);
//            }
//
//        event.getWorld().createExplosion (blocks.get(0).getLocation(), 3);
            return; //TODO 3 maybe turn all blocks to sand or create an explosion or something
        }

        String worldName = event.getWorld().getName();

        if(worldName.equals(netherVisitedWorld.getName()))
        {
            //TODO 2 we have to go back to the world according to the flint and steel
            return;
        }

        //create a link from the current location to the nether world.
        //TODO 1 I think we should have a QCVisitedWorld table for all the crap we want to
        //add to it, rather than resetting stuff within world. We need to add a
        // nether location for each visited world.
    }

    private boolean isPortalValid(ArrayList<Block> blocks) {
        BlockArea ba = new BlockArea();
        Location l;

        //first get the rectangle of the portal
        for(Block b : blocks)
        {
            ba.expandArea(b.getLocation());
        }

        //we can't actually tell who let the fire, so we just look for nearby players and
        //if they have the portal key, the portal is considered valid.
        for(Player p : PlayerManager.getNearbyPlayers(ba.getCenter(),MAX_KEY_PORTAL_DISTANCE))
        {
            if(PlayerManager.containsPortalKey(p))
                return true;
        }

        return false;
    }

}
