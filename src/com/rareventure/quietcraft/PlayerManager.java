package com.rareventure.quietcraft;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

/**
 * Contains standard procedures for handling interaction with the system state and database
 */
public class PlayerManager {
    private final QuietCraftPlugin qcp;
    private final EbeanServer db;

    public PlayerManager(QuietCraftPlugin qcp)
    {
        this.db = qcp.getDatabase();
        this.qcp = qcp;
    }

    private void addToPlayerLog(Player player, QCPlayerLog.Action action)
    {
        db.insert(new QCPlayerLog(player.getUniqueId().toString(),new Date(),
                getPlayersWorld(player).getId(),
                action));
    }

    public QCPlayer getQCPlayer(UUID uniqueId) {
        return db.find(QCPlayer.class).where().
                eq("uuid", uniqueId.toString()).findUnique();
    }

    public QCWorld getPlayersWorld(Player player) {
        QCPlayer qcPlayer = getQCPlayer(player.getUniqueId());

        if(qcPlayer != null)
            return qcp.wm.getQCWorld(qcPlayer.getWorldId());

        return null;
    }

    public void onPlayerDeath(Player p) {
        EbeanServer db = qcp.getDatabase();

        Transaction transaction = db.beginTransaction();
        try {
            QCPlayer qcPlayer = getQCPlayer(p.getUniqueId());

            QCWorld w = qcp.wm.findBestWorldForDeadPlayer(p);

            if (qcPlayer == null) {
                qcp.getLogger().warning("Player "+p.getUniqueId()+" died, but doesn't exist, adding....");
                qcPlayer = new QCPlayer(p.getUniqueId().toString(), w.getId());
                db.insert(qcPlayer);
            } else {
                qcPlayer.setWorldId(w.getId());
                db.update(qcPlayer);
            }

            addToPlayerLog(p, QCPlayerLog.Action.PERMA_DEATH);
            transaction.commit();
        }
        finally
        {
            transaction.end();
        }
    }

    public QCPlayer getQCPlayer(Player player) {
        return getQCPlayer(player.getUniqueId());
    }

    public QCPlayer createQCPlayer(Player player, QCWorld w) {
        QCPlayer qcPlayer = new QCPlayer(player.getUniqueId().toString(), w.getId());
        db.insert(qcPlayer);

        return qcPlayer;
    }


    public void onPlayerJoin(Player player) {
        QCPlayer qcPlayer = getQCPlayer(player);

        Transaction transaction = db.beginTransaction();
        try {
            //new player
            if(qcPlayer == null)
            {
                QCWorld w = qcp.wm.findFirstJoinWorld();
                createQCPlayer(player,w);

                player.sendMessage("You have been born in world '"+w.getName()+"'");
                player.teleport(Bukkit.createWorld(new WorldCreator(w.getName())).getSpawnLocation());

                addToPlayerLog(player, QCPlayerLog.Action.JOIN);
                transaction.commit();
                return;
            }

            //we assume the player is in the right place.
            //TODO 2 verify this

            addToPlayerLog(player, QCPlayerLog.Action.JOIN);
            transaction.commit();

            player.sendMessage("You are in world '"+
                    qcp.pm.getPlayersWorld(player.getPlayer()).getName()+"'");
            return;

        }
        finally {
            transaction.end();
        }
    }

    public void onPlayerQuit(Player player) {
        addToPlayerLog(player, QCPlayerLog.Action.QUIT);
    }
}
