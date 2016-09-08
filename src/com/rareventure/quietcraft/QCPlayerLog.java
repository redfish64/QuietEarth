
package com.rareventure.quietcraft;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity()
@Table(name="qc_player_log")
public class QCPlayerLog {
    public static enum Action {

        /**
         * Player joined the server
         */
        @EnumValue("J")
        JOIN,

        /**
         * Player quit the server
         */
        @EnumValue("Q")
        QUIT,

        /**
         * Player died
         */
        @EnumValue("X")
        PERMA_DEATH,

        /**
         * Player moved to a new world, either due to perma death,
         * or using a portal
         */
        @EnumValue("M")
        MOVED_TO_WORLD,

    }

    @NotNull
    private String playerUuid;

    @NotNull
    private Date timestamp;

    /**
     * The visitId of the world
     */
    @NotNull
    private long worldVisitId;

    /**
     * The id of the world
     */
    @NotNull
    private long worldId;

    @NotNull
    private Action action;

    public QCPlayerLog() {}

    public QCPlayerLog(String playerUuid, Date timestamp, long worldId, long worldVisitId, Action action) {
        this.playerUuid = playerUuid;
        this.timestamp = timestamp;
        this.worldId = worldId;
        this.worldVisitId = worldVisitId;
        this.action = action;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getWorldId() {
        return worldId;
    }

    public void setWorldId(long worldId) {
        this.worldId = worldId;
    }

    public long getWorldVisitId() {
        return worldVisitId;
    }

    public void setWorldVisitId(long worldVisitId) {
        this.worldVisitId = worldVisitId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
