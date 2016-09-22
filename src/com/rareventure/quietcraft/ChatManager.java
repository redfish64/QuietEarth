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

    private QuietCraftPlugin qcp;

    public ChatManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        speak(qcp.pm.getQCPlayer(event.getPlayer()).defaultSpeakStyle,
                event.getPlayer(),
                event.getMessage());
        event.setCancelled(true);
    }

    /**
     * Says something to players around a specific location.
     *
     * @param self if not null, then message won't be sent to this player
     * @param name name of whatever is speaking.
     * @param loc   location of the thing
     * @param maxDistSqr  the maximum distance a player can hear
     * @param message  the message to be sent
     */
    public void speak(Player self, String name, String action, Location loc, double maxDistSqr, String message)
    {
        List<Player> allPlayers = loc.getWorld().getPlayers();
        
        String normalMessage = name + " "+action+" \"" +message+"\"";
        String inaudibleMessage = name+" "+action+" *in audible*";
        String inaudibleMessage2 = "<*in audible*> "+action+" *in audible*";

        //in the nether, sound travels infinitely far
        boolean isNetherWorld = WorldUtil.isNetherWorld(loc.getWorld());

        for (Player toPlayer : allPlayers) {
            if(self != null && toPlayer.equals(self))
            {
                Bukkit.getLogger().info(toPlayer.getName()+" is self");
                continue;
            }

            Location toPlayerLoc = toPlayer.getLocation();
            Vector vecToSoundSource = loc.subtract(toPlayerLoc).toVector();

            double distSqr = vecToSoundSource.lengthSquared();
            String fullMessage;

            //in the nether, sound travels infinitely far
            if (distSqr < maxDistSqr || isNetherWorld)
                fullMessage = normalMessage;
            else if (distSqr < maxDistSqr * Config.INAUDIBLE_DIST_RATIO_SQR)
                fullMessage = inaudibleMessage;
            else if (distSqr < maxDistSqr * Config.INAUDIBLE_DIST_RATIO2_SQR)
                fullMessage = inaudibleMessage2;
            else {
                Bukkit.getLogger().info(toPlayer.getName()+" is out of range");
                continue; // don't send a message past inaudible range
            }
            Bukkit.getLogger().info(toPlayer.getName()+" received msg2: "+fullMessage);
            toPlayer.sendMessage(String.format("\u00A7d%s\u00A7f %s",
                        WorldUtil.
                        getTextDirectionFromSource(vecToSoundSource, toPlayerLoc.getYaw())
                        ,fullMessage));
        }

        Bukkit.getLogger().info("Message: "+name+" "+message+" at "+loc+" maxDistSqr "+maxDistSqr);
    }

    public void speak(SpeakStyle speakStyle, Player player, String message) {
        player.sendMessage("You " + speakStyle.selfAction + " \"" + message + "\"");
        speak(player, "<" + player.getName() + ">", speakStyle.action, player.getLocation(),
                speakStyle.getDistSqr(),
                message);

    }

    public enum SpeakStyle { SHOUT("shouts","shout","shout_speech_dist"),
                             SAY("says","say","normal_speech_dist"),
                             WHISPER("whispers","whisper","whisper_speech_dist");

        public final String action;
        public final String selfAction;
        private final String configPath ;

        SpeakStyle(String action, String selfAction, String configPath)
        {
            this.action = action;
            this.selfAction = selfAction;
            this.configPath = configPath;
        }

        public double getDistSqr() {
            return MathUtil.sqr(Config.getConfigDouble(configPath));
        }
    }

    //TODO 2.5 test voices with another player
}
