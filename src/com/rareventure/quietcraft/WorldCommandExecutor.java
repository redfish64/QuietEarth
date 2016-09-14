package com.rareventure.quietcraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WorldCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Location l = player.getLocation();
            sender.sendMessage(
                    String.format("W: %s Lx%8.3f y%8.3f z%8.3f y%8.3f p%8.3f"
                    ,player.getWorld().getName(),l.getX(),l.getY(),l.getZ(),
                            l.getYaw(),l.getPitch()));
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
