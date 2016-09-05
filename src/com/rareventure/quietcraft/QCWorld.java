
package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

@Entity()
@Table(name="qc_world")
public class QCWorld {
    @Transient
    public int currentPlayerCount;

    @Transient
    public int timesVisited;

    @Id
    private long id;

    @NotNull
    private String name;

    public QCWorld() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorldVisitedCount(QuietCraftPlugin qcp) {
        //TODO 2 FIXME
        return 0;
    }
}

