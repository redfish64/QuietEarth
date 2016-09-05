
package com.rareventure.quietcraft;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name="qc_player")
public class QCPlayer {

    @Id
    private String uuid;

    @NotNull
    @ManyToOne
    private long worldId;

    public QCPlayer()
    {
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

}
