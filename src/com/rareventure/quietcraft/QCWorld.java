
package com.rareventure.quietcraft;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

@Entity()
@Table(name="qc_world")
public class QCWorld {
    @Id
    private int id;

    @NotNull
    private String name;

    public QCWorld()
    {}

    public QCWorld(int id, String name) {
        this.id = id;
        this.name = name;
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

    public int getWorldVisitedCount(QuietCraftPlugin qcp) {
        return 0; //TODO 2 FIXME
    }
}

