package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminListConfig implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public AdminListConfig(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            ConfigurationSection cs = qcp.getConfig().getConfigurationSection("");

            String regex;

            if(args.length > 0)
            {
                regex = args[0];
            }
            else regex = ".*";

            Pattern p = Pattern.compile(regex);

            List<String> matchingKeys = cs.getKeys(true).stream().sorted().
                    filter(k -> p.matcher(k).find()).collect(Collectors.toList());

            matchingKeys.forEach(k -> { sender.sendMessage(k+": "+cs.get(k)); });
            sender.sendMessage(matchingKeys.size()+" results found.");
        }

        return true;
    }
}
