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
    @ManyToOne
    private QCLocation loc1;

    @ManyToOne
    private QCLocation loc2;

    public QCPortalLink() {
    }

    public QCPortalLink(QCLocation loc1, QCLocation loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public QCLocation getLoc1() {
        return loc1;
    }

    public void setLoc1(QCLocation loc1) {
        this.loc1 = loc1;
    }

    public QCLocation getLoc2() {
        return loc2;
    }

    public void setLoc2(QCLocation loc2) {
        this.loc2 = loc2;
    }
}
