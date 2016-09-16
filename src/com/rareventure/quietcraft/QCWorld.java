
package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.*;

@Entity()
@Table(name="qc_world")
public class QCWorld {
    @Id
    private int id;

    @NotNull
    private String name;

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
     * Worlds are reused when all players have left (or at least active players)
     * In this case, we just change the spawn and nether portal locations, and
     * update the recycle counter, so that when we reborn a player, we can treat
     * the world as brand new.
     */
    private int recycleCounter;

    public QCWorld()
    {}


    public QCWorld(String name, int spawnLocX, int spawnLocY, int spawnLocZ, int netherLocX, int netherLocY, int netherLocZ) {
        this.name = name;
        this.spawnLocX = spawnLocX;
        this.spawnLocY = spawnLocY;
        this.spawnLocZ = spawnLocZ;
        this.netherLocX = netherLocX;
        this.netherLocY = netherLocY;
        this.netherLocZ = netherLocZ;
    }

    public QCWorld(String name, Location spawnLocation, Location netherPortalLocation) {
        this(name,spawnLocation.getBlockX(),spawnLocation.getBlockY(),spawnLocation.getBlockZ(),
                netherPortalLocation.getBlockX(),netherPortalLocation.getBlockY(),
                netherPortalLocation.getBlockZ());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getRecycleCounter() {
        return recycleCounter;
    }

    public void setRecycleCounter(int recycleCounter) {
        this.recycleCounter = recycleCounter;
    }
}

