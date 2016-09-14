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
                createSqlQuery("select id from qc_portal_link pl" +
                        " where loc1x = :x" +
                        " and loc1y = :y" +
                        " and loc1z = :z" +
                        " and visited_world_id1 = :vw_id");
        q.setParameter(1, l.getX())
                .setParameter(2, l.getY())
                .setParameter(3, l.getZ())
                .setParameter(4, qcp.wm.getQCVisitedWorld(l.getWorld().getName()).getId());

        return q.findList().stream().map(row -> getPortalLink(row.getInteger("id"))).
                collect(Collectors.toList());
    }

    private List<QCPortalLink> getPortalLinksFromLocation2(Location l) {
        SqlQuery q = qcp.db.
                createSqlQuery("select id from qc_portal_link pl" +
                        " where loc2x = :x" +
                        " and loc2y = :y" +
                        " and loc2z = :z" +
                        " and visited_world_id2 = :vz_id");
        q.setParameter(1, l.getX())
                .setParameter(2, l.getY())
                .setParameter(3, l.getZ())
                .setParameter(4, qcp.wm.getQCVisitedWorld(l.getWorld().getName()).getId());

        return q.findList().stream().map(row -> getPortalLink(row.getInteger("id"))).
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
