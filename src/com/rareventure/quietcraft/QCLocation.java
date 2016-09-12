package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Location;
import org.bukkit.World;

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

    public QCLocation() {
    }

    public QCLocation(double locX, double locY, double locZ) {

        this.x = locX;
        this.y = locY;
        this.z = locZ;
    }

    public QCLocation(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAt(Location l) {
        if(l.getX() == this.x
           && l.getY() == this.y
            && l.getZ() == this.z)
            return true;

        return false;
    }

    public Location toLocation(World w) {
        return new Location(w, this.x, this.y, this.z);
    }
}
