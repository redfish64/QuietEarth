package com.rareventure.quietcraft;

import com.avaje.ebean.SqlQuery;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles portal link stuff
 */
public class PortalManager {
    private final QuietCraftPlugin qcp;

    public PortalManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    /**
     * Returns all portals that links to the specified location
     */
    public List<QCPortalLink> getPortalLinksForLocation(Location l) {
        List<QCPortalLink> pl1List = getPortalLinksFromLocation1(l);
        List<QCPortalLink> pl2List = getPortalLinksFromLocation2(l);

        pl1List.addAll(pl2List);

        return pl1List;
    }

    private List<QCPortalLink> getPortalLinksFromLocation1(Location l) {
        SqlQuery q = qcp.db.
                createSqlQuery("select pl.id as pl_id from qc_portal_link pl, qc_location l" +
                        " where l.id = pl.loc1id" +
                        " and l.x = :x" +
                        " and l.y = :y" +
                        " and l.z = :z" +
                        " and pl.visited_world_id1 = :world_id ");
        q.setParameter(1, l.getX())
                .setParameter(2, l.getY())
                .setParameter(3, l.getZ())
                .setParameter(4, qcp.wm.getQCVisitedWorld(l.getWorld().getName()).getId());

        return q.findList().stream().map(row -> getPortalLink(row.getInteger("pl_id"))).
                collect(Collectors.toList());
    }

    private List<QCPortalLink> getPortalLinksFromLocation2(Location l) {
        SqlQuery q = qcp.db.
                createSqlQuery("select pl.id as pl_id from qc_portal_link pl, qc_location l" +
                        " where l.id = pl.loc2id" +
                        " and l.x = :x" +
                        " and l.y = :y" +
                        " and l.z = :z" +
                        " and pl.visited_world_id2 = :world_id ");
        q.setParameter(1, l.getX())
                .setParameter(2, l.getY())
                .setParameter(3, l.getZ())
                .setParameter(4, qcp.wm.getQCVisitedWorld(l.getWorld().getName()).getId());

        return q.findList().stream().map(row -> getPortalLink(row.getInteger("pl_id"))).
                collect(Collectors.toList());
    }

    private QCPortalLink getPortalLink(int id) {
        return qcp.db.find(QCPortalLink.class).where().
                eq("id", String.valueOf(id)).findUnique();
    }

    /**
     * Returns a portal that links to the specified location
     */
    public QCPortalLink getPortalLinkForLocation(Location l) {
        List<QCPortalLink> r = getPortalLinksForLocation(l);
        if(r.isEmpty())
            return null;
        return r.get(0);
    }


}
