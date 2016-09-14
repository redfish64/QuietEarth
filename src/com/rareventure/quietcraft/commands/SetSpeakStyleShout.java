package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.ChatManager;
import com.rareventure.quietcraft.QuietCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class SetSpeakStyleShout implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public SetSpeakStyleShout(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            sender.sendMessage("Setting speak style to shout");

            qcp.pm.getQCPlayer(player).defaultSpeakStyle = ChatManager.SpeakStyle.SHOUT;
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
