package com.rareventure.quietcraft.commands;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.google.common.collect.ImmutableList;

public class KillCommandExecutor implements CommandExecutor {
    public KillCommandExecutor() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(ede);
            if (ede.isCancelled()) return true;

            ede.getEntity().setLastDamageCause(ede);
            player.setHealth(0);
            //sender.sendMessage("Ouch. That look like it hurt.");
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
