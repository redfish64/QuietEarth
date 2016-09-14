package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.ChatManager;
import com.rareventure.quietcraft.QuietCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpeakStyleWhisper implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public SetSpeakStyleWhisper(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            sender.sendMessage("Setting speak style to whisper");

            qcp.pm.getQCPlayer(player).defaultSpeakStyle = ChatManager.SpeakStyle.WHISPER;
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
