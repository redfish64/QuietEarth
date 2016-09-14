package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Handles player chat. We try to make it more realistic in that you can only talk to people
 * nearby
 */
public class ChatManager {
    /**
     * The perctange distance after max dist where the speaker can be identified, but the
     * message not heard
     */
    private static final double INAUDIBLE_DIST_PERC_SQR = 1.5*1.5;

    /**
     * The perctange distance after max dist where the speaker can't be identified, nor the
     * message not heard
     */
    private static final double INAUDIBLE_DIST_PERC2_SQR = 2*2;

    /**
     * The distance in order to hear someone speaking at a normal level
     */
    private static final double NORMAL_SPEECH_DIST_SQR = 30;

    /**
     * The distance in order to hear someone whispering
     */
    private static final double WHISPER_SPEECH_DIST_SQR = 5;

    /**
     * The distance in order to hear someone shouting
     */
    private static final double SHOUT_SPEECH_DIST_SQR = 100;


    public void onPlayerChat(AsyncPlayerChatEvent event) {
        shout(event.getPlayer(),event.getMessage());
        event.setCancelled(true);
    }

    /**
     * Says something to players around a specific location.
     *
     * @param name name of whatever is speaking.
     * @param loc   location of the thing
     * @param maxDistSqr  the maximum distance a player can hear
     * @param message  the message to be sent
     */
    public void speak(String name, String action, Location loc, double maxDistSqr, String message)
    {
        String normalMessage = name + " "+action+" " +message;
        String inaudibleMessage = name+" "+action+" *in audible*";
        String inaudibleMessage2 = "<*in audible*> "+action+" *in audible*";
        List<Player> allPlayers = loc.getWorld().getPlayers();
        for (Player toPlayer : allPlayers) {
            Location toPlayerLoc = toPlayer.getLocation();
            Vector vecToSoundSource = loc.subtract(toPlayerLoc).toVector();

            double distSqr = vecToSoundSource.lengthSquared();
            String fullMessage;

            if (distSqr < maxDistSqr)
                fullMessage = normalMessage;
            else if (distSqr < maxDistSqr * INAUDIBLE_DIST_PERC_SQR)
                fullMessage = inaudibleMessage;
            else if (distSqr < maxDistSqr * INAUDIBLE_DIST_PERC2_SQR)
                fullMessage = inaudibleMessage2;
            else
                return; // don't send a message past inaudible range

            toPlayer.sendMessage(String.format("\u00A7d%s\u00A7f %s",
                        WorldUtil.
                        getTextDirectionFromSource(vecToSoundSource, toPlayerLoc.getYaw())
                        ,fullMessage));
        }

        Bukkit.getLogger().info("Message: "+name+" "+message+" at "+loc);
    }

    public void shout(Player player, String message) {
        speak("<"+player.getName()+">","shouts",player.getLocation(),SHOUT_SPEECH_DIST_SQR,
                message);
    }

    public void say(Player player, String message) {
        speak("<"+player.getName()+">","says",player.getLocation(),NORMAL_SPEECH_DIST_SQR,
                message);
    }

    public void whisper(Player player, String message) {
        speak("<"+player.getName()+">","whispers",player.getLocation(),WHISPER_SPEECH_DIST_SQR,
                message);
    }

    //TODO 2 test voices with another player
}
