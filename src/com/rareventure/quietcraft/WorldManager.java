package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
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
    private static final int MAX_KEY_PORTAL_DISTANCE = 5;
    private final QuietCraftPlugin qcp;

    public List<QCWorld> worlds;
    public QCWorld netherWorld;

    public WorldManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        worlds = new ArrayList<QCWorld>();
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

            //find and populate the netherWorld
            //note that if this is a fresh install, then worlds will be size 0, and netherWorld won't
            //be populated until setupForNewInstall() is called.
            if (w.getId() == 1)
                netherWorld = w;
        }
    }

    /**
     * Finds the best world for a newbie to join.
     */
    public QCWorld findFirstJoinWorld() {
        QCWorld bestW = null;

        for (QCWorld w : worlds) {
            if (w == netherWorld)
                continue;

            if (bestW == null)
                bestW = w;
            else if (bestW.currentPlayerCount < w.currentPlayerCount)
                bestW = w;
            else if (bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
                bestW = w;
        }

        return bestW;
    }

    /**
     * This should be called periodically (every day or so). It looks for worlds that haven't been visited in a long time
     * most likely due to all active players already visiting it and changes its world id and spawn
     * point to recycle it.
     */
    public void updateWorldVisitIds()
    {
        EbeanServer db = qcp.getDatabase();

        db.beginTransaction();
        try {
            //find the max visited_world id, so we know what to upgrade to
            SqlQuery q = db.createSqlQuery("select max(visit_id) as max_visit_id from world");
            long nextVisitId = q.findUnique().getLong("max_visit_id") + 1;

            //find the worlds that haven't had activity since ABANDONED_WORLD_CUTOFF_MS
            q = db.
                    createSqlQuery("select world_id, world_visit_id, max(timestamp) from qc_player_log " +
                            "group by world_visit_id having max(timestamp) < :timestamp_cutoff " +
                            "order by max(timestamp)");
            q.setParameter("timestamp_cutoff", System.currentTimeMillis() - ABANDONED_WORLD_CUTOFF_MS);

            //finds out the worlds the player already visited
            List<SqlRow> visitedWorldIdSqlRows = q.findList();
            for (SqlRow sr : visitedWorldIdSqlRows) {
                long worldId = sr.getLong("world_id");
                QCWorld w = getQCWorld(worldId);
                w.setVisitId(nextVisitId++);
                db.update(w);
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
    public QCWorld findBestWorldForDeadPlayer(Player player) {
        SqlQuery q = qcp.getDatabase().createSqlQuery("select distinct world_visit_id from qc_player_log where player_uuid = :uuid");
        q.setParameter("uuid", player.getUniqueId().toString());

        //finds out the worlds the player already visited
        Set<SqlRow> visitedWorldIdSqlRows = q.findSet();
        Set<Long> visitedWorldIds = new HashSet<Long>(visitedWorldIdSqlRows.size());
        for (SqlRow sr : visitedWorldIdSqlRows) {
            visitedWorldIds.add(sr.getLong("world_visit_id"));
        }

        QCWorld bestW = null;

        for (QCWorld w : worlds) {
            //the player doesn't go to the nether until he's not allowed anywhere else
            if (w == netherWorld)
                continue;

            //don't choose a world that has been spawned in already
            if (visitedWorldIds.contains(w.getVisitId()))
                continue;

            if (bestW == null)
                bestW = w;
            else if (bestW.currentPlayerCount < w.currentPlayerCount)
                bestW = w;
            else
                //choose the most visited world.. since it is the most interesting
                if (bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
                    bestW = w;
        }

        //if there is no good world for the player anymore, we just send them to the nether
        if (bestW == null) {
            return netherWorld;
        }

        return bestW;
    }

    private void sendPlayerToNether(Player p) {
        World nether = Bukkit.getWorld("world_nether");
        Location loc = nether.getSpawnLocation();
        p.teleport(loc);
    }

    //TODO 3 add a worldcreatorparams object for setting up different parameters for new world generation
    //(especially for hell and purgatory)
    public synchronized QCWorld createNewWorld(String name) {
        int id = worlds.size() + 1;

        if (name == null) {
            name = "world_qc" + (id-1); //TODO 3 maybe come up with a random name generator
        }

        QCWorld qcw = new QCWorld(id,id,name);

        qcp.getDatabase().insert(qcw);

        worlds.add(qcw);

        qcp.getLogger().info("Starting world creation for " + name + "...");
        WorldCreator c = new WorldCreator(name);

        //TODO 3 randomize world, maybe use generators from other plugins, etc.
        c.createWorld();
        qcp.getLogger().info("Finished world creation for " + name);

        return qcw;
    }

    public QCWorld getQCWorld(long id) {
        for (QCWorld w : worlds) {
            if (w.getId() == id)
                return w;
        }

        return null;
    }

    public void setupForNewInstall() {
        netherWorld = new QCWorld(1,1,"world_nether");
        qcp.getDatabase().insert(netherWorld);
        worlds.add(netherWorld);

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

    public void onPortalCreate(PortalCreateEvent event) {
        if(!isPortalValid(event.getBlocks())) {
            qcp.getLogger().info("denied portal creation");
            event.setCancelled(true);
            return; //TODO 3 maybe turn all blocks to sand or create an explosion or something
        }
//            List<Block> blocks = event.getBlocks();
//            for(Block b : blocks)
//            {
//                b.setType(Material.SAND);
//            }
//
//        event.getWorld().createExplosion (blocks.get(0).getLocation(), 3);

        String world = event.getWorld().getName();
        //qcp.getLogger().info("denied portal creation");
        //event.setCancelled(true);

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
