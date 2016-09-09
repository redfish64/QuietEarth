
package com.rareventure.quietcraft;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotNull;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//TODO 2 when leaving the server, the portal key should drop from the players inventory... and always be there forever
//.. is this possible? I heard items automatically disappear after 5 minutes.

@Entity()
@Table(name = "qc_player")
public class QCPlayer {

    @Id
    private String uuid;

    @NotNull
    @ManyToOne
    private QCVisitedWorld visitedWorld;

    /**
     * The number of souls kept while dead. When a player dies
     * their inventory is gone, so we need to store the number of
     * souls they had somewhere.
     */
    @NotNull
    private int soulsKeptDuringDeath;

    @ManyToOne
    private QCLocation bedLocation;

    public QCPlayer() {
    }

    public QCPlayer(String uuid, QCVisitedWorld visitedWorld, int soulsKeptDuringDeath) {
        this.uuid = uuid;
        this.visitedWorld = visitedWorld;
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

    public QCLocation getBedLocation() {
        return bedLocation;
    }

    public void setBedLocation(QCLocation bedLocation) {
        this.bedLocation = bedLocation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public QCVisitedWorld getVisitedWorld() {
        return visitedWorld;
    }

    public void setVisitedWorld(QCVisitedWorld visitedWorld) {
        this.visitedWorld = visitedWorld;
    }

    public int getSoulsKeptDuringDeath() {
        return soulsKeptDuringDeath;
    }

    public void setSoulsKeptDuringDeath(int soulsKeptDuringDeath) {
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }
}
