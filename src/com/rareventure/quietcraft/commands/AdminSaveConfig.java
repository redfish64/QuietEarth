package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.Config;
import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class AdminSaveConfig implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public AdminSaveConfig(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            try {
                Config.saveConfig(qcp);
            } catch (IOException e) {
                sender.sendMessage("Failed to save config file, got: "+e);
                return true;
            }
            sender.sendMessage("Saved.");
           return true;
        }

        return true;
    }
}
