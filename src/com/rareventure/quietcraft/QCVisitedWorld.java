
package com.rareventure.quietcraft;

import com.avaje.ebean.event.BulkTableEvent;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.*;

/**
 * A visited world is a world with unique values such as spawn location or nether location.
 * The idea is that after a world has been not visited in a long time, it should be recycled.
 * This allows people to see old abandoned structures from the past, which is interesting, and
 * also allows us to hold less worlds in the server (good for memory).
 * <p>
 *     Each visited world has its own spawn point and nether spawn point. The reason being that
 *     the old spawn point from the ancient world could have been booby trapped, and with no one
 *     there to "let the draw bridge down" so to speak, people spawning there would be trapped.
 * </p>
 */
@Entity()
@Table(name="qc_visited_world")
public class QCVisitedWorld {
    @Id @GeneratedValue
    private int id;

    @NotNull
    private int worldId;

    /**
     * Where new users spawn. 0,0,0 for the "nether" visited world
     */
    private int spawnLocX,spawnLocY,spawnLocZ;

    /**
     * Where portals go to in the nether. We have a static location for this, so that nether
     * people can monitor their borders, too, just like overworld people.
     */
    private int netherLocX,netherLocY,netherLocZ;

    /**
     * If true, the visited world is considered active. There may be only one active visited
     * world per world. (This should really be in world as  activeVisitedWorld, but that would
     * mean circular dependencies and I'd rather avoid that. Also in the future maybe we could
     * have more than one active visited world per world)
     */
    @NotNull
    private boolean active;

    public QCVisitedWorld() {
    }

    public QCVisitedWorld(int id, int worldId, int spawnLocX, int spawnLocY, int spawnLocZ, int netherLocX, int netherLocY, int netherLocZ, boolean active) {
        this.id = id;
        this.worldId = worldId;
        this.spawnLocX = spawnLocX;
        this.spawnLocY = spawnLocY;
        this.spawnLocZ = spawnLocZ;
        this.netherLocX = netherLocX;
        this.netherLocY = netherLocY;
        this.netherLocZ = netherLocZ;
        this.active = active;
    }

    public QCVisitedWorld(int id, QCWorld qcw, Location spawnLocation, Location netherSpawnQCLocation,
                            boolean active) {
        this.id = id;
        this.worldId = qcw.getId();
        this.spawnLocX = spawnLocation.getBlockX();
        this.spawnLocY = spawnLocation.getBlockY();
        this.spawnLocZ = spawnLocation.getBlockZ();
        this.netherLocX = netherSpawnQCLocation.getBlockX();
        this.netherLocY = netherSpawnQCLocation.getBlockY();
        this.netherLocZ = netherSpawnQCLocation.getBlockZ();
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public QCWorld getWorld() {
        return QuietCraftPlugin.db.find(QCWorld.class).where().
                eq("id", String.valueOf(worldId)).findUnique();
    }

    public void setWorld(QCWorld world) {

        setWorldId(world.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSpawnLocation(int x, int y, int z) {
        setSpawnLocX(x);
        setSpawnLocY(y);
        setSpawnLocZ(z);
    }

    /**
     * returns world's name
     * @return
     */
    public String getName() {
        return getWorld().getName();
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getSpawnLocX() {
        return spawnLocX;
    }

    public void setSpawnLocX(int spawnLocX) {
        this.spawnLocX = spawnLocX;
    }

    public int getSpawnLocY() {
        return spawnLocY;
    }

    public void setSpawnLocY(int spawnLocY) {
        this.spawnLocY = spawnLocY;
    }

    public int getSpawnLocZ() {
        return spawnLocZ;
    }

    public void setSpawnLocZ(int spawnLocZ) {
        this.spawnLocZ = spawnLocZ;
    }

    public int getNetherLocX() {
        return netherLocX;
    }

    public void setNetherLocX(int netherLocX) {
        this.netherLocX = netherLocX;
    }

    public int getNetherLocY() {
        return netherLocY;
    }

    public void setNetherLocY(int netherLocY) {
        this.netherLocY = netherLocY;
    }

    public int getNetherLocZ() {
        return netherLocZ;
    }

    public void setNetherLocZ(int netherLocZ) {
        this.netherLocZ = netherLocZ;
    }

    public Location getNetherLocation() {
        return new Location(WorldUtil.getNetherWorld(),
                getNetherLocX(),getNetherLocY(),getNetherLocZ());
    }

    /**
     * Returns the nether portal location for this visited world.
     * <p>Technically we can get world, but usually this is already
     * known, so to save the work of looking it up, we just ask for it.
     * </p>
     */
    public Location getSpawnLocation(World spawnedWorld) {
        return new Location(spawnedWorld,getSpawnLocX(),getSpawnLocY(),getSpawnLocZ());
    }
}

