package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents a link between two portals in two different worlds.
 */
@Entity()
@Table(name = "qc_portal_link")
public class QCPortalLink {
    @NotNull
    private long world1Idl;

    @NotNull
    private long world1Id2;

    @NotNull
    private double loc1X;
    @NotNull
    private double loc1Y;
    @NotNull
    private double loc1Z;
    @NotNull
    private double loc1Yaw;
    @NotNull
    private double loc1Pitch;

    @NotNull
    private double loc2X;
    @NotNull
    private double loc2Y;
    @NotNull
    private double loc2Z;
    @NotNull
    private double loc2Yaw;
    @NotNull
    private double loc2Pitch;

    public QCPortalLink() {
    }

    public QCPortalLink(long world1Idl, long world1Id2, double loc1X, double loc1Y, double loc1Z, double loc1Yaw, double loc1Pitch, double loc2X, double loc2Y, double loc2Z, double loc2Yaw, double loc2Pitch) {
        this.world1Idl = world1Idl;
        this.world1Id2 = world1Id2;
        this.loc1X = loc1X;
        this.loc1Y = loc1Y;
        this.loc1Z = loc1Z;
        this.loc1Yaw = loc1Yaw;
        this.loc1Pitch = loc1Pitch;
        this.loc2X = loc2X;
        this.loc2Y = loc2Y;
        this.loc2Z = loc2Z;
        this.loc2Yaw = loc2Yaw;
        this.loc2Pitch = loc2Pitch;
    }

    public long getWorld1Idl() {
        return world1Idl;
    }

    public void setWorld1Idl(long world1Idl) {
        this.world1Idl = world1Idl;
    }

    public long getWorld1Id2() {
        return world1Id2;
    }

    public void setWorld1Id2(long world1Id2) {
        this.world1Id2 = world1Id2;
    }

    public double getLoc1X() {
        return loc1X;
    }

    public void setLoc1X(double loc1X) {
        this.loc1X = loc1X;
    }

    public double getLoc1Y() {
        return loc1Y;
    }

    public void setLoc1Y(double loc1Y) {
        this.loc1Y = loc1Y;
    }

    public double getLoc1Z() {
        return loc1Z;
    }

    public void setLoc1Z(double loc1Z) {
        this.loc1Z = loc1Z;
    }

    public double getLoc1Yaw() {
        return loc1Yaw;
    }

    public void setLoc1Yaw(double loc1Yaw) {
        this.loc1Yaw = loc1Yaw;
    }

    public double getLoc1Pitch() {
        return loc1Pitch;
    }

    public void setLoc1Pitch(double loc1Pitch) {
        this.loc1Pitch = loc1Pitch;
    }

    public double getLoc2X() {
        return loc2X;
    }

    public void setLoc2X(double loc2X) {
        this.loc2X = loc2X;
    }

    public double getLoc2Y() {
        return loc2Y;
    }

    public void setLoc2Y(double loc2Y) {
        this.loc2Y = loc2Y;
    }

    public double getLoc2Z() {
        return loc2Z;
    }

    public void setLoc2Z(double loc2Z) {
        this.loc2Z = loc2Z;
    }

    public double getLoc2Yaw() {
        return loc2Yaw;
    }

    public void setLoc2Yaw(double loc2Yaw) {
        this.loc2Yaw = loc2Yaw;
    }

    public double getLoc2Pitch() {
        return loc2Pitch;
    }

    public void setLoc2Pitch(double loc2Pitch) {
        this.loc2Pitch = loc2Pitch;
    }
}
