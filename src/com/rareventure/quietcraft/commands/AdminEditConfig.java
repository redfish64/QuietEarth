package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.Config;
import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminEditConfig implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public AdminEditConfig(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            if(args.length != 2)
            {
                sender.sendMessage(command.getUsage());
            }

            String k = args[0];
            String v = args[1];
            FileConfiguration c = qcp.getConfig();

            if(!c.contains(k)) {
                //assume its a regex
                Pattern p = Pattern.compile(k);
                List<String> l = c.getConfigurationSection("").getKeys(true).stream().
                        filter(k2 -> p.matcher(k2).find()).collect(Collectors.toList());

                if(l.size() == 0) {
                    sender.sendMessage("Couldn't find key, " + k);
                    return true;
                }
                if (l.size() > 1) {
                    sender.sendMessage("Error, key matches multiple: " + l.stream().
                            reduce((s, s2) -> s + " " + s2));
                    return true;
                }

                k = l.get(0);
            }

            Object oldValue = c.get(k);

            c.set(k,v);

            try {
                Config.reloadConfig();
            }
            catch(Exception e) {
                Bukkit.getLogger().warning("Failed to set config variable " + k + " to " + v);
                c.set(k,oldValue);
                sender.sendMessage("Error processing config with new value, reverting, got: "
                        +e.getMessage());
                Config.reloadConfig();
                return true;
            }

            sender.sendMessage("Updated '"+k+"' to '"+v+"'");

            return true;
        }

        return true;
    }
}
