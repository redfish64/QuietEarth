package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a link between two portals in two different worlds.
 */
@Entity()
@Table(name = "qc_portal_link")
public class QCPortalLink {

    @NotNull
    private int visitedWorldId1;

    @NotNull
    private int loc1Id;

    @NotNull
    private int visitedWorldId2;

    @NotNull
    private int loc2Id;

    public QCPortalLink() {
    }

    public QCPortalLink(int visitedWorldId1, int loc1Id, int visitedWorldId2, int loc2Id) {
        this.visitedWorldId1 = visitedWorldId1;
        this.loc1Id = loc1Id;
        this.visitedWorldId2 = visitedWorldId2;
        this.loc2Id = loc2Id;
    }

    public QCPortalLink(QCVisitedWorld vw1, QCLocation loc1,
                        QCVisitedWorld vw2, QCLocation loc2) {
        this.visitedWorldId1 = vw1.getId();
        this.visitedWorldId2 = vw2.getId();
        this.loc1Id = loc1.getId();
        this.loc2Id = loc2.getId();
    }

    public QCLocation getLoc1() {
        return QuietCraftPlugin.db.find(QCLocation.class).where().
                eq("id", String.valueOf(loc1Id)).findUnique();
    }

    public void setLoc1(QCLocation loc1) {
        setLoc1Id(loc1.getId());
    }

    public QCLocation getLoc2() {
        return QuietCraftPlugin.db.find(QCLocation.class).where().
                eq("id", String.valueOf(loc2Id)).findUnique();
    }

    public void setLoc2(QCLocation loc2) {
        setLoc2Id(loc2.getId());
    }

    public int getLoc1Id() {
        return loc1Id;
    }

    public void setLoc1Id(int loc1Id) {
        this.loc1Id = loc1Id;
    }

    public int getLoc2Id() {
        return loc2Id;
    }

    public void setLoc2Id(int loc2Id) {
        this.loc2Id = loc2Id;
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
}
