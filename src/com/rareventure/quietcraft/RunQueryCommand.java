
package com.rareventure.quietcraft;

import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RunQueryCommand implements CommandExecutor {
    private final QuietCraftPlugin plugin;

    public RunQueryCommand(QuietCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String query = String.join(" ",args);
        sender.sendMessage("Running: "+query);

        RawSqlBuilder rsb = RawSqlBuilder.parse(query);
        RawSql rs = rsb.create();

        //TODO 3 finish, maybe

        return true;
    }
}
