
package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

//TODO 3 when leaving the server, the portal key should drop from the players inventory... and always be there forever
//.. is this possible? I heard items automatically disappear after 5 minutes.

@Entity()
@Table(name = "qc_player")
public class QCPlayer {

    @Id
    private String uuid;

    //TODO 2 make sure that when teleporting to another world, the player will respawn
    // in their bed in the first world (or the spawn of the first world if their bed was
    //  destroyed ... if we change our mind and want the player to respawn in the current
    //  world, we still need to update their worldid and recyclecount)


    /**
     * The home world of the player
     */
    @NotNull
    private int worldId;


    /**
     * The number of souls kept while dead. When a player dies
     * their inventory is gone, so we need to store the number of
     * souls they had somewhere.
     */
    @NotNull
    private int soulsKeptDuringDeath;

    @Transient
    public ChatManager.SpeakStyle defaultSpeakStyle = ChatManager.SpeakStyle.SHOUT;

    public QCPlayer() {
    }

    public QCPlayer(String uuid, QCWorld world, int soulsKeptDuringDeath) {
        this.uuid = uuid;
        this.worldId = world.getId();
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public QCWorld getWorld() {
        return QuietCraftPlugin.db.find(QCWorld.class).where().
                eq("id", String.valueOf(worldId)).findUnique();
    }

    public void setVisitedWorld(QCWorld world) {
        setWorldId(world.getId());
    }

    public int getSoulsKeptDuringDeath() {
        return soulsKeptDuringDeath;
    }

    public void setSoulsKeptDuringDeath(int soulsKeptDuringDeath) {
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    @Override
    public String toString() {
        return "QCPlayer{" +
                "uuid='" + uuid + '\'' +
                ", worldId=" + worldId +
                ", soulsKeptDuringDeath=" + soulsKeptDuringDeath +
                ", defaultSpeakStyle=" + defaultSpeakStyle +
                '}';
    }

    public void setWorld(QCWorld w) {
        setWorldId(w.getId());
    }
}
