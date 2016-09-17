package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.QCWorld;
import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HackJumpToWorld implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public HackJumpToWorld(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length != 1)
            {
                sender.sendMessage(command.getUsage()+": need a world id");
            }

            int id = Integer.parseInt(args[0]);

            QCWorld qcw = qcp.wm.getQCWorld(id);

            World w = qcw.getWorld();

            Location l = qcw.getSpawnLocation(w);

            player.teleport(l);
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
