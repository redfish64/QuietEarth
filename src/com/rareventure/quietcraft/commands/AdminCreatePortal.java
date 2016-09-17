package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCreatePortal implements CommandExecutor {

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Location l = player.getLocation();
            WorldUtil.constructPortal(l.clone().add(4,0,0),5,6, true, false,false);
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
