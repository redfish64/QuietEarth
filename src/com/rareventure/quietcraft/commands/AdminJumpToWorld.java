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

public class AdminJumpToWorld implements CommandExecutor {
    private QuietCraftPlugin qcp;

    public AdminJumpToWorld(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            Player player = (Player) sender;

            if(args.length != 1)
            {
                sender.sendMessage(command.getUsage()+": need a world id");
                return true;
            }

            int id = Integer.parseInt(args[0]);

            QCWorld qcw = qcp.wm.getQCWorld(id);

            World w = qcw.getWorld();

            Location l = qcw.getSpawnLocation(w);

            WorldUtil.makeTeleportLocationSafe(l);
            player.teleport(l);

            sender.sendMessage("Jumped to world "+w.getName());
        }

        return true;
    }
}
