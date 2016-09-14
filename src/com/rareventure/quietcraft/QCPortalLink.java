package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.*;

/**
 * Represents a link between two portals in two different worlds.
 * <p>Note that we don't get notified when a portal is destroyed, so multiple portal links from and
 * to the same place may be created</p>
 */
@Entity()
@Table(name = "qc_portal_link")
public class QCPortalLink {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private int visitedWorldId1;

    @NotNull
    private int loc1X;

    @NotNull
    private int loc1Y;

    @NotNull
    private int loc1Z;

    @NotNull
    private int visitedWorldId2;

    @NotNull
    private int loc2X;

    @NotNull
    private int loc2Y;

    @NotNull
    private int loc2Z;

    @Transient
    private Location cachedLoc1;

    @Transient
    private Location cachedLoc2;


    public QCPortalLink() {
    }

    public QCPortalLink(int visitedWorldId1, int loc1X, int loc1Y, int loc1Z, int visitedWorldId2, int loc2X, int loc2Y, int loc2Z) {
        this.visitedWorldId1 = visitedWorldId1;
        this.loc1X = loc1X;
        this.loc1Y = loc1Y;
        this.loc1Z = loc1Z;
        this.visitedWorldId2 = visitedWorldId2;
        this.loc2X = loc2X;
        this.loc2Y = loc2Y;
        this.loc2Z = loc2Z;
    }

    public QCPortalLink(int vwId1, Location portalLocation, int vwId2, Location overWorldPortalLocation) {
        this.visitedWorldId1 = vwId1;
        this.loc1X = portalLocation.getBlockX();
        this.loc1Y = portalLocation.getBlockY();
        this.loc1Z = portalLocation.getBlockZ();
        this.visitedWorldId2 = vwId2;
        this.loc2X = overWorldPortalLocation.getBlockX();
        this.loc2Y = overWorldPortalLocation.getBlockY();
        this.loc2Z = overWorldPortalLocation.getBlockZ();
    }

    public int getVisitedWorldId1() {
        return visitedWorldId1;
    }

    public void setVisitedWorldId1(int visitedWorldId1) {
        this.visitedWorldId1 = visitedWorldId1;
    }

    public int getVisitedWorldId2() {
        return visitedWorldId2;
    }

    public void setVisitedWorldId2(int visitedWorldId2) {
        this.visitedWorldId2 = visitedWorldId2;
    }

    /**
     * Returns the other location of a portal link.
     * @param wm
     * @param l must be the representative location of the portal
     * @return
     */
    public Location getOtherLoc(WorldManager wm, Location l) {

        if(WorldUtil.isAt(l,loc1X,loc1Y,loc1Z) && wm.getQCVisitedWorld(l.getWorld().getName()).getId() ==
                getVisitedWorldId1())
            return new Location(getWorld2(wm),loc2X,loc2Y,loc2Z);

        return new Location(getWorld1(wm),loc1X,loc1Y,loc1Z);
    }

    private World getWorld1(WorldManager wm) {
        return Bukkit.getWorld(wm.getQCVisitedWorld(visitedWorldId1).getName());
    }

    private World getWorld2(WorldManager wm) {
        return Bukkit.getWorld(wm.getQCVisitedWorld(visitedWorldId2).getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLoc1() {
        if(cachedLoc1 == null) {
            World w = DbUtil.getWorldFromQCVisitedWorldId(getVisitedWorldId1());
            assert(w != null) : "World is null for "+getVisitedWorldId1();
            cachedLoc1 = new Location(w, loc1X, loc1Y, loc1Z);
        }

        return cachedLoc1;
    }

    public Location getLoc2() {
        if(cachedLoc2 == null) {
            World w = DbUtil.getWorldFromQCVisitedWorldId(getVisitedWorldId2());
            assert(w != null) : "World is null for "+getVisitedWorldId2();
            cachedLoc2 = new Location(w, loc2X, loc2Y, loc2Z);
        }

        return cachedLoc2;
    }

    public int getLoc1X() {
        return loc1X;
    }

    public void setLoc1X(int loc1X) {
        this.loc1X = loc1X;
    }

    public int getLoc1Y() {
        return loc1Y;
    }

    public void setLoc1Y(int loc1Y) {
        this.loc1Y = loc1Y;
    }

    public int getLoc1Z() {
        return loc1Z;
    }

    public void setLoc1Z(int loc1Z) {
        this.loc1Z = loc1Z;
    }

    public int getLoc2X() {
        return loc2X;
    }

    public void setLoc2X(int loc2X) {
        this.loc2X = loc2X;
    }

    public int getLoc2Y() {
        return loc2Y;
    }

    public void setLoc2Y(int loc2Y) {
        this.loc2Y = loc2Y;
    }

    public int getLoc2Z() {
        return loc2Z;
    }

    public void setLoc2Z(int loc2Z) {
        this.loc2Z = loc2Z;
    }

    @Override
    public String toString() {
        return "QCPortalLink{" +
                "id=" + id +
                ", visitedWorldId1=" + visitedWorldId1 +
                ", loc1X=" + loc1X +
                ", loc1Y=" + loc1Y +
                ", loc1Z=" + loc1Z +
                ", visitedWorldId2=" + visitedWorldId2 +
                ", loc2X=" + loc2X +
                ", loc2Y=" + loc2Y +
                ", loc2Z=" + loc2Z +
                '}';
    }
}
