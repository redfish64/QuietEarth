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

//TODO 2.5 possibly notify everybody in the nether where the portal opened, and give them a way to see their
// own location, so they can attack it.

//TODO 3 delete all player logs where visit ids no longer exist to save space??


//TODO 3 flying admin user

//TODO 3 new rule: when in nether with at least one soul, all souls drop in nether,
// player is teleported back to world (so dying in nether isn't so harsh and allows player to
// try and recover souls)


/**
 * Manages qcWorlds and visited qcWorlds.
 */
public class WorldManager {

    //TODO 2.5 saying "hello sailor" should kill player and send them to the nether (they'll respawn in the next
    // world when they die in the nether)
    // should check for 17173 speak (1's as l's, etc.) as well, maybe


    private final QuietCraftPlugin qcp;

    /**
     * All qcWorlds of the server
     */
    public List<QCWorld> qcWorlds;
    public QCWorld netherQCWorld;

    public WorldManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        qcWorlds = new ArrayList<>();
        qcWorlds.addAll(qcp.getDatabase().find(QCWorld.class).findList());

        for (QCWorld w : qcWorlds) {
            //we must call createworld for each world in setup, otherwise we won't be able
            //to respawn to it during gameplay
            //Calling createWorld() whether it exists or not, freezes the entire server for 10-20 seconds
            //so we have to do this ahead of time. A waste of memory? Yes.
            //Alternatives? None that are so good as far as I can tell...
            //Note that I tried 30 qcWorlds and it clocked in under 2 gig, so I'm just going to leave it this
            //way. There isn't much I can do. Maybe allow multiple servers, but I think if we can get abandoned
            //qcWorlds to work, we'll be ok
            //TODO 4 Can we fix this somehow
            WorldCreator c = new WorldCreator(w.getName());
            c.createWorld();

            //find and populate the netherQCWorld
            //note that if this is a fresh install, then qcWorlds will be size 0, and netherQCWorld won't
            //be populated until setupForNewInstall() is called.
            if (w.getId() == Config.NETHER_WORLD_ID)
                netherQCWorld = w;
        }
    }

    /**
     * Finds the visited world that:
     * <ul>
     * <li>either has the most live players currently,
     * or if tied, then the world with the most events,</li>
     * <li>and the player hasn't visited the latest recycled version of the world</li>
     * <li>and is not the nether world</li>
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
                                "   (select count(*) from qc_player p where p.world_id = w.id) as pc, \n" +
                                "   (select count (*) from qc_player_log where world_id = w.id) as plc\n" +
                                "  from qc_world w where not exists \n" +
                                "    (select 'x' from qc_player_log l where player_id = :pid and " +
                                "       l.world_id = w.id and l.world_recycle_counter = w.recycle_counter)\n" +
                                "    and w.id != "+ Config.NETHER_WORLD_ID+"\n" +
                                "    order by pc desc,plc desc limit 1;\n");

        q.setParameter(1,playerId);
        SqlRow sr = q.findUnique();
        if(sr == null)
            return null;

        return getQCWorld(sr.getInteger("id"));
    }

    /**
     * Finds the best world to join for a player that just died to join.
     * <p>If the player visited all visited qcWorlds, look for an existing
     * world where the visited world can be destroyed.</p>
     */
    public QCWorld findOrCreateBestWorldForDeadPlayer(Player player) {
        //find the best world that already exists
        QCWorld bestW = findBestActiveWorldForPlayer(player.getUniqueId().toString());

        if(bestW != null)
            return bestW;

        //recycle a visited world (if we can)
        QCWorld bestWToRecycle = findBestWorldToRecycle(player.getUniqueId().toString());

        if(bestWToRecycle == null)
        {
            Bukkit.getLogger().info("No qcWorlds left to recycle, sending player to nether");
            return netherQCWorld;
        }

        recycleWorld(bestWToRecycle);

        return bestWToRecycle;
    }

    /**
     * Recycles the world. This changes the spawn and nether points, increments
     * the world counter, and destroys all previous portals
     */
    private void recycleWorld(QCWorld w) {
        Location spawnLocation = WorldUtil.getRandomSpawnLocation(w.getWorld(), Config.OVERWORLD_SPAWN_RNP);
        Location randomNetherPortalLocation = createRandomNetherPortalLocation();

        w.setSpawnLocation(spawnLocation);
        w.setNetherLocation(randomNetherPortalLocation);

        w.setRecycleCounter(w.getRecycleCounter()+1);
        w.setLastRecycleTimestamp(new Date());
        w.setSoulInflowOutflow(0);
        qcp.db.save(w);


        //destroy all the old portal links, so that the recycled world won't be
        //enterable from the nether
        getAllPortalLinks(w.getId()). forEach( pl -> destroyPortalsForPortalLink(pl));
    }

    private List<QCPortalLink> getAllPortalLinks(int worldId) {
        return qcp.db.find(QCPortalLink.class).where("world_id1 = :wid or world_id2 = :wid")
                .setParameter(1,worldId).findList();
    }

    /**
     * Finds the world which is eligible for recycling and is the best candidate
     * according to various criteria
     */
    private QCWorld findBestWorldToRecycle(String deadPlayerId) {

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
        "       (select count(*) from qc_player p where p.uuid != :pid and p.world_id = w.id) as active_players,\n" +
        "       ifnull((select max(timestamp) from qc_player_log l where l.world_id = w.id),0) as last_action\n" +
        "       from qc_world w\n" +
        "       where w.id != "+ Config.NETHER_WORLD_ID+"\n" +
        "       and (active_players = 0 " +
        " or last_action < "+ Config.MAX_RECYCLE_LAST_PLAYER_LOG_MS+")\n" +
        " order by active_players, last_action limit 1;\n"
                ).setParameter(1,deadPlayerId);
        SqlRow sr = q.findUnique();
        if(sr != null)
            return getQCWorld(sr.getInteger("id"));

        return null;
    }

    private void sendPlayerToNether(Player p) {
        World nether = Bukkit.getWorld("world_nether");
        Location loc = nether.getSpawnLocation();
        WorldUtil.makeTeleportLocationSafe(loc);
        p.teleport(loc);
    }

    public QCWorld getQCWorld(long id) {
        for (QCWorld w : qcWorlds) {
            if (w.getId() == id)
                return w;
        }

        return null;
    }

    public void setupForNewInstall() {
        netherQCWorld = new QCWorld(WorldUtil.NETHER_WORLD_NAME,0,0,0,0,0,0);
        netherQCWorld.setId(Config.NETHER_WORLD_ID);
        qcp.db.insert(netherQCWorld);
        qcWorlds.add(netherQCWorld);

        //create a bunch of empty qcWorlds
        //we can't create qcWorlds adhoc, or it freezes the server while the creation is in process
        //so we create them ahead of time
        //TODO 2.5 different types of qcWorlds???
        for (int i = 0; i < Config.MAX_WORLDS; i++)
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
        int id = qcWorlds.size() + 1;

        if (name == null) {
            name = "world_qc" + (id-1); //TODO 3 maybe come up with a random name generator
        }

        qcp.getLogger().info("Starting world creation for " + name + "...");
        WorldCreator c = new WorldCreator(name);
        //TODO 3 randomize world, maybe use generators from other plugins, etc.
        World w = c.createWorld();
        qcp.getLogger().info("Finished world creation for " + name);

        Location spawnLocation = WorldUtil.getRandomSpawnLocation(w, Config.OVERWORLD_SPAWN_RNP);
        Location randomNetherPortalLocation = createRandomNetherPortalLocation();
        QCWorld qcw = new QCWorld(name, spawnLocation, randomNetherPortalLocation);
        qcw.setId(id);

        qcWorlds.add(qcw);
        qcp.getDatabase().insert(qcw);

        return qcw;
    }


    /**
     * Creates a random portal location in the nether world.
     * All world portals will come here
     *
     * @return created location
     */
    public Location createRandomNetherPortalLocation() {
        World w = getWorld(netherQCWorld);

        return WorldUtil.getRandomSpawnLocation(w, Config.NETHER_PORTAL_RNP);
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
            WorldUtil.destroyPortal(event.getBlocks(), MathUtil.normalRandom(Config.PORTAL_EXPLOSION_SIZE_PERC_RNP)/100f);
            return;
        }

        QCPlayer qcPlayer = qcp.pm.getQCPlayer(playerAndPortalKey.getKey());

        BlockArea ba = WorldUtil.getPortalArea(event.getBlocks());
        Location portalLocation = WorldUtil.getRepresentativePortalLocation(ba);

        //we don't allow portals from the nether, because it gets out of control
        //if a worlds portal keys gets distributed outside the world
        if(WorldUtil.isNetherWorld(event.getWorld().getName()))
        {
            qcp.getLogger().info("denied portal creation");
            event.setCancelled(true);

            WorldUtil.destroyPortal(event.getBlocks(), 
                    MathUtil.normalRandom(Config.PORTAL_EXPLOSION_SIZE_PERC_RNP));
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
                    qcp.wm.getQCWorld(event.getWorld().getName()).getId(),
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
//TODO 2.5 test that players spawn in populated qcWorlds first!
    /**
     * Destroys portals on both sides of a portal link, and then deletes the portal link,
     * and qclocations from the database
     */
    private void destroyPortalsForPortalLink(QCPortalLink pl) {

        //the block physics stuff will delete the portals from the db for us
        WorldUtil.destroyPortal(pl.getLoc1(),MathUtil.normalRandom(Config.PORTAL_EXPLOSION_SIZE_PERC_RNP));
        WorldUtil.destroyPortal(pl.getLoc2(),MathUtil.normalRandom(Config.PORTAL_EXPLOSION_SIZE_PERC_RNP));
    }

    /**
     * Creates a portal for the nether world
     */
    private void constructNetherWorldPortal(Location netherWorldLocation, boolean active) {
        int width = MathUtil.normalRandomInt(Config.NETHER_PORTAL_WIDTH_PARAMS);

        WorldUtil.constructPortal(netherWorldLocation, width,
                (int)Math.round(width* Config.PORTAL_HEIGHT_TO_WIDTH),
                true, //TODO 3.5 if we let this be false, portal blocks face sideways
                //Math.random() < .5,
                active, true //we add ceilings to nether qcWorlds incase there is lava falling down
                // from the ceiling. Since each world only gets one nether location that can't be
                // moved, we want to make it extra safe
        );
    }

    /**
     * Creates a portal for the overworld
     */
    private void constructOverWorldPortal(Location overWorldLocation, boolean active) {
        int width = MathUtil.normalRandomInt(Config.OVERWORLD_PORTAL_WIDTH_PARAMS);

        WorldUtil.constructPortal(overWorldLocation, width,
                (int)Math.round(width* Config.PORTAL_HEIGHT_TO_WIDTH),
                true, //TODO 3.5 if we let this be false, portal blocks face sideways
                //Math.random() < .5,
                active, false);
    }

    private String getWorldNameForPortalKey(ItemStack portalKey) {
        String s = portalKey.getItemMeta().getDisplayName();

        return s.substring(0, s.length() - Config.PORTAL_KEY_DISPLAY_NAME_ENDING.length());
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
        for(Player p : PlayerManager.getNearbyPlayers(ba.getCenter(), Config.MAX_KEY_PORTAL_DISTANCE))
        {
            ItemStack portalKey = qcp.pm.getPortalKey(p);

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
                            Bukkit.getLogger().info("cancelled destruction of portal for "+e.getBlock());
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
                    .forEach(pl -> {
                        Bukkit.getLogger().info("PORTAL block physics event caused pl destruction");
                        qcp.portalManager.destroyPortalLink(pl);
                    });
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

    public QCWorld getQCWorld(String name) {
        return qcWorlds.stream().filter(w -> w.getName().equals(name)).findFirst().get();
    }
}
