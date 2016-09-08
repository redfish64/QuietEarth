
package com.rareventure.quietcraft;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "qc_player")
public class QCPlayer {

    @Id
    private String uuid;

    @NotNull
    @ManyToOne
    private long worldId;

    /**
     * The number of souls kept while dead. When a player dies
     * their inventory is gone, so we need to store the number of
     * souls they had somewhere.
     */
    @NotNull
    private int soulsKeptDuringDeath;

    public QCPlayer() {
    }

    public QCPlayer(String uuid, long worldId) {
        this.uuid = uuid;
        this.worldId = worldId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getWorldId() {
        return worldId;
    }

    public void setWorldId(long worldId) {
        this.worldId = worldId;
    }

    public int getSoulsKeptDuringDeath() {
        return soulsKeptDuringDeath;
    }

    public void setSoulsKeptDuringDeath(int soulsKeptDuringDeath) {
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

}
