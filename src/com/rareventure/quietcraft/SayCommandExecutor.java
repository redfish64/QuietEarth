package com.rareventure.quietcraft;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class SayCommandExecutor implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public SayCommandExecutor(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            qcp.chatManager.say(player,String.join(" ",args));

            //sender.sendMessage("Ouch. That look like it hurt.");
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
