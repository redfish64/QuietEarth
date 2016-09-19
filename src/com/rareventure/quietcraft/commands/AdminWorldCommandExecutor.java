package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.QCPlayer;
import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdminWorldCommandExecutor implements CommandExecutor {

    private QuietCraftPlugin qcp;

    public AdminWorldCommandExecutor(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            Player player = (Player) sender;

            Location l = player.getLocation();
            QCPlayer qcPlayer = qcp.pm.getQCPlayer(player);
            sender.sendMessage(
                    String.format("W: %s MW: %d %s Lx%8.3f y%8.3f z%8.3f yaw %8.3f pitch %8.3f"
                    ,player.getWorld().getName(),qcPlayer.getWorldId(),qcPlayer.getWorld().getName(),
                            l.getX(),l.getY(),l.getZ(),
                            l.getYaw(),l.getPitch()));
        }
        return true;
    }
}
