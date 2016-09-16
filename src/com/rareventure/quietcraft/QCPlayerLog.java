
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

        /**
         * Player left a world using a portal
         */
        @EnumValue("L")
        MOVED_FROM_WORLD,

    }

    @NotNull
    private String playerId;

    @NotNull
    private Date timestamp;

    @NotNull
    private int worldId;

    /**
     * The recycle counter of the world for the log. This changes in the qc_world table
     * everytime the world is recycled (ie a new spawn and nether portal is generated for it)
     */
    @NotNull
    private int worldRecycleCounter;

    @NotNull
    private Action action;

    @Id
    private int id;

    public QCPlayerLog() {}

    public QCPlayerLog(String playerId, Date timestamp, int worldId, int worldRecycleCounter, Action action) {
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.worldId = worldId;
        this.worldRecycleCounter = worldRecycleCounter;
        this.action = action;
    }

    public QCPlayer getPlayer() {
        return QuietCraftPlugin.db.find(QCPlayer.class).where().
                eq("id", String.valueOf(playerId)).findUnique();
    }

    public void setPlayer(QCPlayer player) {
        setPlayerId(player.getUuid());
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public QCWorld getQCWorld() {
        return QuietCraftPlugin.db.find(QCWorld.class).where().
                eq("id", String.valueOf(worldId)).findUnique();
    }

    public void setQCWorld(QCWorld world) {
        //warning! WE MUST use the bean method setVisitedWorldId(), or ebean doesn't
        // see the object as changed
        // and won't save it (and comedy will ensue)
        setWorldId(world.getId());
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getWorldRecycleCounter() {
        return worldRecycleCounter;
    }

    public void setWorldRecycleCounter(int worldRecycleCounter) {
        this.worldRecycleCounter = worldRecycleCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
