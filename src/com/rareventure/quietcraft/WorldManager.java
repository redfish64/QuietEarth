package com.rareventure.quietcraft;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tim on 7/10/16.
 */
public class WorldManager {
    private static final int MAX_WORLDS = 30;
    private final QuietCraftPlugin qcp;

    public List<QCWorld> worlds;
    private QCWorld netherWorld;

    public WorldManager(QuietCraftPlugin qcp)
    {
        this.qcp = qcp;

        worlds = new ArrayList<QCWorld>();
        worlds.addAll(qcp.getDatabase().find(QCWorld.class).findList());

        for(QCWorld w: worlds)
        {
            //we must call createworld for each world in setup, otherwise we won't be able
            //to respawn to it during gameplay
            //Calling createWorld() whether it exists or not, freezes the entire server for 10-20 seconds
            //so we have to do this ahead of time. A waste of memory? Yes.
            //Alternatives? None that are so good as far as I can tell...
            //TODO 2.5 Can we fix this somehow
            WorldCreator c = new WorldCreator(w.getName());
            c.createWorld();

            //find and populate the netherWorld
            //note that if this is a fresh install, then worlds will be size 0, and netherWorld won't
            //be populated until setupForNewInstall() is called.
            if(w.getId() == 0)
                netherWorld = w;
        }
    }

    /**
     * Finds the best world for a newbie to join.
     */
    public QCWorld findFirstJoinWorld() {
        QCWorld bestW = null;

        for(QCWorld w : worlds) {
            if(bestW == null)
                bestW = w;
            else if (bestW.currentPlayerCount < w.currentPlayerCount)
                bestW = w;
            else if (bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
                bestW = w;
        }

        return bestW;
    }

    /**
     * Finds the best world to join for a player that just died to join.
     */
    public QCWorld findBestWorldForDeadPlayer(Player player) {
        SqlQuery q = qcp.getDatabase().createSqlQuery("select world_id from qc_player_log where player_uuid = :uuid");
        q.setParameter("uuid",player.getUniqueId().toString());

        //finds out the worlds the playe already visited
        Set<SqlRow> visitedWorldIdSqlRows = q.findSet();
        Set<Long> visitedWorldIds = new HashSet<Long>(visitedWorldIdSqlRows.size());
        for(SqlRow sr : visitedWorldIdSqlRows) {
            visitedWorldIds.add(sr.getLong("world_id"));
        }

        QCWorld bestW = null;

        for(QCWorld w : worlds)
        {
            //the player doesn't go to the nether until he's really dead
            if(w == netherWorld)
                continue;

            //don't choose a world that has been spawned in already
            if(visitedWorldIds.contains(w.getId()))
                continue;

            if(bestW == null)
                bestW = w;
            else if (bestW.currentPlayerCount < w.currentPlayerCount)
                bestW = w;
            else
                //choose the most visited world.. since it is the most interesting
                if(bestW.getWorldVisitedCount(qcp) < w.getWorldVisitedCount(qcp))
                    bestW = w;
        }

        //TODO 2 if the player visited all worlds, we create another strategy for visiting very old worlds.

        //if there is no good world for the player anymore, we just send them to the nether
        if(bestW == null)
        {
            return netherWorld;
        }

        return bestW;
    }

    private void sendPlayerToNether(Player p)
    {
        World nether = Bukkit.getWorld("world_nether");
        Location loc = nether.getSpawnLocation();
        p.teleport(loc);
    }

    //TODO 2 add a worldcreatorparams object for setting up different parameters for new world generation
    //(especially for hell and purgatory)
    public synchronized QCWorld createNewWorld(String name) {
        int id = worlds.size()+1;

        if(name == null)
        {
            name = "world_qc"+id; //TODO 3 maybe come up with a random name generator
        }

        QCWorld qcw = new QCWorld();
        qcw.setName(name);
        qcw.setId(id);

        qcp.getDatabase().insert(qcw);

        worlds.add(qcw);

        qcp.getLogger().info("Starting world creation for "+name+"...");
        WorldCreator c = new WorldCreator(name);

        //TODO 3 randomize world, maybe use generators from other plugins, etc.
        c.createWorld();
        qcp.getLogger().info("Finished world creation for "+name);

        return qcw;
    }

    public QCWorld getQCWorld(long id) {
        for(QCWorld w : worlds)
        {
            if(w.getId() == id)
                return w;
        }

        return null;
    }

    public void setupForNewInstall() {
        netherWorld = new QCWorld();
        netherWorld.setName("world_nether");
        netherWorld.setId(1);
        qcp.getDatabase().insert(netherWorld);
        worlds.add(netherWorld);

        //create a bunch of empty worlds
        //we can't create worlds adhoc, or it freezes the server while the creation is in process
        //so we create them ahead of time
        //TODO 2 different types of worlds???
        for(int i = 0; i < MAX_WORLDS; i++)
            createNewWorld();
    }

    public QCWorld createNewWorld() {
        return createNewWorld(null);
    }

}
