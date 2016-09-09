
package com.rareventure.quietcraft;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotNull;
import org.apache.logging.log4j.core.net.JMSQueueManager;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    private QCPlayer player;

    @NotNull
    private Date timestamp;

    @ManyToOne
    private QCVisitedWorld visitedWorld;

    @NotNull
    private Action action;

    public QCPlayerLog() {}

    public QCPlayerLog(QCPlayer player, Date timestamp, QCVisitedWorld visitedWorld, Action action) {
        this.player = player;
        this.timestamp = timestamp;
        this.visitedWorld = visitedWorld;
        this.action = action;
    }

    public QCPlayer getPlayer() {
        return player;
    }

    public void setPlayer(QCPlayer player) {
        this.player = player;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public QCVisitedWorld getVisitedWorld() {
        return visitedWorld;
    }

    public void setVisitedWorld(QCVisitedWorld visitedWorld) {
        this.visitedWorld = visitedWorld;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
