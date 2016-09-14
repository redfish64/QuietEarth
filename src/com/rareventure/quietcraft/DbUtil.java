package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

/**
 * database related calls
 */
public class DbUtil {
    /**
     * Gets a database object given an id
     * @param clazz
     * @param id
     * @param <T>
     * @return database object matching the id
     */
    public static <T> T getObject(Class<T> clazz, int id) {
        return QuietCraftPlugin.db.find(clazz).where().
                eq("id", String.valueOf(id)).findUnique();
    }


    public static World getWorldFromQCVisitedWorldId(int vwId) {
        QCVisitedWorld vw = getObject(QCVisitedWorld.class, vwId);
        return Bukkit.getWorld(vw.getName());
    }
}
