package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.StringUtil;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AdminGive implements CommandExecutor {

    private QuietCraftPlugin qcp;

    public AdminGive(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            Player player = (Player) sender;

            if(args.length < 2)
            {
                player.sendMessage(command.getUsage());
                return true;
            }

            String otherPlayer = StringUtil.findSingleRegex(args[0],
                    Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList()));
            if(otherPlayer == null)
                sender.sendMessage("Couldn't find player: "+otherPlayer);

            String type = StringUtil.findSingleRegex(args[1],
                    Arrays.asList("portal_key","soul"));

            int count;
            if(args.length > 2) {
                count = Integer.parseInt(args[2]);
            }
            else count = 1;

            if(type.equals("portal_key"))
            {
                qcp.pm.addPortalKeysToInventory(Bukkit.getPlayer(otherPlayer),count);
                sender.sendMessage(otherPlayer+" just got "+count+" portal keys");
            }
            else if(type.equals("soul"))
            {
                qcp.pm.addSoulsToInventory(Bukkit.getPlayer(otherPlayer),count);
                sender.sendMessage(otherPlayer+" just got "+count+" souls");
            }
        }

        return true;
    }
}
