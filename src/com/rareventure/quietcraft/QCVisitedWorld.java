
package com.rareventure.quietcraft;

import com.avaje.ebean.event.BulkTableEvent;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;

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
     * Where new users spawn. Nullable for the "nether" visited world
     */
    private int spawnLocationId;

    /**
     * Where portals go to in the nether. We have a static location for this, so that nether
     * people can monitor their borders, too, just like overworld people.
     * <p>
     * Nullable for the "nether" visited world.
     * </p>
     */
    private int netherLocationId;

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

    public QCVisitedWorld(int id, int worldId, int spawnLocationId, int netherLocationId, boolean active) {
        this.id = id;
        this.worldId = worldId;
        this.spawnLocationId = spawnLocationId;
        this.netherLocationId = netherLocationId;
        this.active = active;
    }

    public QCVisitedWorld(int id, QCWorld world, QCLocation spawnLocation, QCLocation netherLocation, boolean active) {
        this.id = id;
        this.worldId = world.getId();
        if(spawnLocation != null)
            this.spawnLocationId = spawnLocation.getId();
        if(netherLocation != null)
            this.netherLocationId = netherLocation.getId();
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

    public QCLocation getSpawnLocation() {
        return QuietCraftPlugin.db.find(QCLocation.class).where().
                eq("id", String.valueOf(spawnLocationId)).findUnique();
    }

    public void setSpawnLocation(QCLocation spawnLocation) {
        setSpawnLocationId(spawnLocation.getId());
    }

    public QCLocation getNetherLocation() {
        return QuietCraftPlugin.db.find(QCLocation.class).where().
                eq("id", String.valueOf(netherLocationId)).findUnique();
    }

    public void setNetherLocation(QCLocation netherLocation) {
        setNetherLocationId(netherLocation.getId());
    }

    public String getName() {
        return getWorld().getName();
    }

    public QCVisitedWorld createCopy(QCLocation spawnLocation, QCLocation netherSpawnLocation) {
        return new QCVisitedWorld(0,this.worldId,spawnLocation.getId(),netherSpawnLocation.getId(), active
        );
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getSpawnLocationId() {
        return spawnLocationId;
    }

    public void setSpawnLocationId(int spawnLocationId) {
        this.spawnLocationId = spawnLocationId;
    }

    public int getNetherLocationId() {
        return netherLocationId;
    }

    public void setNetherLocationId(int netherLocationId) {
        this.netherLocationId = netherLocationId;
    }

    @Override
    public String toString() {
        return "QCVisitedWorld{" +
                "id=" + id +
                ", worldId=" + worldId +
                ", spawnLocationId=" + spawnLocationId +
                ", netherLocationId=" + netherLocationId +
                ", active=" + active +
                '}';
    }
}

