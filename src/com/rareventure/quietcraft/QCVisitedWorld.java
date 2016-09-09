
package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

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

    @ManyToOne
    private QCWorld world;

    /**
     * Where new users spawn. Nullable for the "nether" visited world
     */
    @ManyToOne
    private QCLocation spawnLocation;

    /**
     * Where portals go to in the nether. We have a static location for this, so that nether
     * people can monitor their borders, too, just like overworld people.
     * <p>
     * Nullable for the "nether" visited world.
     * </p>
     */
    @ManyToOne
    private QCLocation netherLocation;

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

    public QCVisitedWorld(int id, QCWorld world, QCLocation spawnLocation, QCLocation netherLocation, boolean active) {
        this.id = id;
        this.world = world;
        this.spawnLocation = spawnLocation;
        this.netherLocation = netherLocation;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public QCWorld getWorld() {
        return world;
    }

    public void setWorld(QCWorld world) {
        this.world = world;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QCLocation getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(QCLocation spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public QCLocation getNetherLocation() {
        return netherLocation;
    }

    public void setNetherLocation(QCLocation netherLocation) {
        this.netherLocation = netherLocation;
    }

    public String getName() {
        return world.getName();
    }

    public QCVisitedWorld createCopy(QCLocation spawnLocation, QCLocation netherSpawnLocation) {
        return new QCVisitedWorld(0,this.world,spawnLocation,netherSpawnLocation, active
        );
    }
}

