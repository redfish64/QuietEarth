
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

    @NotNull
    private int visitedWorldId;

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

    public QCPlayer(String uuid, QCVisitedWorld visitedWorld, int soulsKeptDuringDeath) {
        this.uuid = uuid;
        this.visitedWorldId = visitedWorld.getId();
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public QCVisitedWorld getVisitedWorld() {
        return QuietCraftPlugin.db.find(QCVisitedWorld.class).where().
                eq("id", String.valueOf(visitedWorldId)).findUnique();
    }

    public void setVisitedWorld(QCVisitedWorld visitedWorld) {
        setVisitedWorldId(visitedWorld.getId());
    }

    public int getSoulsKeptDuringDeath() {
        return soulsKeptDuringDeath;
    }

    public void setSoulsKeptDuringDeath(int soulsKeptDuringDeath) {
        this.soulsKeptDuringDeath = soulsKeptDuringDeath;
    }

    public int getVisitedWorldId() {
        return visitedWorldId;
    }

    public void setVisitedWorldId(int visitedWorldId) {
        this.visitedWorldId = visitedWorldId;
    }

    @Override
    public String toString() {
        return "QCPlayer{" +
                "uuid='" + uuid + '\'' +
                ", visitedWorldId=" + visitedWorldId +
                ", soulsKeptDuringDeath=" + soulsKeptDuringDeath +
                '}';
    }
}
