package com.rareventure.quietcraft;

import com.avaje.ebean.SqlQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles portal link stuff
 */
public class PortalManager {

    /**
     * Cache of both loc1 and loc2 to the corresponding portal links.
     * We need a fast cache because we monitor physic block events and
     * whenever a nether block is destroyed, we need to find any portal
     * links its associated to and remove them.
     */
    private Map<Location, Set<QCPortalLink>> locToPortalLinkCache = new HashMap<>();


    private final QuietCraftPlugin qcp;

    public PortalManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        constructCache();
    }

    private void constructCache() {
        qcp.db.find(QCPortalLink.class).findList().
                forEach(pl -> addPortalLinkToCache(pl));
    }

    private void addPortalLinkToCache(QCPortalLink pl) {
        synchronized (locToPortalLinkCache)
        {
            Set<QCPortalLink> plSet = locToPortalLinkCache.getOrDefault(pl.getLoc1(),new HashSet<>());
            plSet.add(pl);
            locToPortalLinkCache.put(pl.getLoc1(), plSet);

            plSet = locToPortalLinkCache.getOrDefault(pl.getLoc2(),new HashSet<>());
            plSet.add(pl);
            locToPortalLinkCache.put(pl.getLoc2(), plSet);
        }
    }


    /**
     * Returns all portals that links to the specified location
     */
    public List<QCPortalLink> getPortalLinksForLocation(Location l) {
        synchronized (locToPortalLinkCache) {
            Set<QCPortalLink> r = locToPortalLinkCache.get(l);
            if(r == null)
                return new ArrayList<>();
            return new ArrayList<>(r);
        }
    }

    //TODO 2 make whisper and normal say commands

    /**
     * Returns a portal that links to the specified location
     */
    public QCPortalLink getPortalLinkForLocation(Location l) {
        synchronized (locToPortalLinkCache) {
            Set<QCPortalLink> r = locToPortalLinkCache.get(l);
            if (r == null || r.isEmpty())
                return null;
            return r.iterator().next();
        }
    }


    public QCPortalLink createPortalLink(int vwId1, Location loc1, int vwId2, Location loc2) {
        QCPortalLink r = new QCPortalLink(vwId1, loc1, vwId2, loc2);

        addPortalLinkToCache(r);

        Bukkit.getLogger().info("Creating portal link "+r);
        return r;
    }

    /**
     * Deletes the portal link from the database and the cache
     */
    private void deletePortalLink(QCPortalLink pl) {
        synchronized (locToPortalLinkCache)
        {
            Set<QCPortalLink> plSet = locToPortalLinkCache.get(pl.getLoc1());
            if(!plSet.remove(pl))
                Bukkit.getLogger().warning("Couldn't find portal link to delete from cache for loc1, "+pl);
            plSet = locToPortalLinkCache.get(pl.getLoc2());
            if(!plSet.remove(pl))
                Bukkit.getLogger().warning("Couldn't find portal link to delete from cache for loc2, "+pl);
        }

        //delete from the db
        qcp.db.delete(pl);
    }

    /**
     * This deletes the portal link and destroys portals on both ends of portal link,
     * unless that portal is linked to the same location as another working portal, in which
     * case the portal is left alone.
     */
    public void destroyPortalLink(QCPortalLink pl)
    {
        boolean destroyP1, destroyP2;

        synchronized (locToPortalLinkCache) {
            destroyP1 = getPortalLinksForLocation(pl.getLoc1()).size() <= 1;
            destroyP2 = getPortalLinksForLocation(pl.getLoc2()).size() <= 1;
            deletePortalLink(pl);
        }
        
        Bukkit.getLogger().info("Destroying portal link "+pl+" p1 destroyed? "+destroyP1+", p2 destroyed? "+destroyP2);

        if(destroyP1) {
            WorldUtil.destroyPortal(pl.getLoc1(),true);
        }
        if(destroyP2) {
            WorldUtil.destroyPortal(pl.getLoc2(),true);
        }

    }
}
