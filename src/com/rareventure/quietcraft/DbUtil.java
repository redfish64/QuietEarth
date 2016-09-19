package com.rareventure.quietcraft;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


    public static World getWorldFromQCWorldId(int vwId) {
        QCWorld vw = getObject(QCWorld.class, vwId);
        return Bukkit.getWorld(vw.getName());
    }

    /**
     * Finds the number of times a player has died outside of the nether in the
     * given time period
     */
    public static int findNumberOfDeathsInTimePeriodForPlayer(String playerId, long timeMs) {
        SqlQuery q = QuietCraftPlugin.db.
                createSqlQuery(
                        " select count(*) as c from qc_player_log where timestamp >= :time and " +
                                "player_id = :pid and action = 'X' and visited_world_id != "+
                                Config.NETHER_WORLD_ID);

        q.setParameter(1,System.currentTimeMillis() - timeMs);
        q.setParameter(2,playerId);
        SqlRow sr = q.findUnique();
        return sr.getInteger("c");
    }

    /**
     * Runs the given sql statement
     */
    public static void runSql(String stmt) throws SQLException {
        Bukkit.getLogger().info("Runnig sql: "+stmt);
        Transaction t = QuietCraftPlugin.db.createTransaction();
        try {
            Connection c = t.getConnection();
            PreparedStatement pstmt = c.prepareStatement(stmt);
            pstmt.execute();
            t.commit();
        }
        finally {
            t.end();
        }
    }

    public static void createIndex(String table, String column) throws SQLException {
        runSql(String.format("create index %s_%s on %s (%s);",table,column,table,column));
    }
}
