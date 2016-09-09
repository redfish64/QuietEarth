package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a location in the world.
 */
@Entity()
@Table(name = "qc_location")
public class QCLocation {
    @Id @GeneratedValue
    private int id;

    @NotNull
    private double x;
    @NotNull
    private double y;
    @NotNull
    private double z;
    @NotNull
    private double yaw;
    @NotNull
    private double pitch;

    public QCLocation() {
    }

    public QCLocation(double locX, double locY, double locZ, double locYaw, double locPitch) {

        this.x = locX;
        this.y = locY;
        this.z = locZ;
        this.yaw = locYaw;
        this.pitch = locPitch;
    }

    public QCLocation(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
