package com.rareventure.quietcraft.commands;

import com.rareventure.quietcraft.MathUtil;
import com.rareventure.quietcraft.QuietCraftPlugin;
import com.rareventure.quietcraft.WorldUtil;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AdminNoisyBlock implements CommandExecutor {
    //TODO 2.3 figure out a way people can change the spawn point of the world. Maybe the portal key and
    // a special block or something?
    private Block lastCakeBlock;

    private static final double MAX_DIST_SQR = 20*20;
    private static final MathUtil.RandomNormalParams RNP =
            new MathUtil.RandomNormalParams(5,3,1,1000);

    private QuietCraftPlugin qcp;

    public AdminNoisyBlock(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (WorldUtil.testAdminPlayer(sender)) {
            Player player = (Player) sender;

            if(lastCakeBlock != null)
            {
                qcp.chatManager.speak(null,"!!NOISY BLOCK!!","says",lastCakeBlock.getLocation(),MAX_DIST_SQR,"Bye!");
                lastCakeBlock.setType(Material.AIR);
                lastCakeBlock.getRelative(0,1,0).setType(Material.AIR);
            }

            Location l = player.getLocation();

            int x = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);
            int y = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);
            int z = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);

            if(args.length > 0)
            {
                float dist = Float.parseFloat(args[0]);
                Vector v = new Vector(x,y,z);
                v.normalize();
                v.multiply(dist);
                x = v.getBlockX();
                y = v.getBlockY();
                z = v.getBlockZ();
            }

            Block b2 = l.getBlock().getRelative(x,y,z);

            b2.setType(Material.DIAMOND_BLOCK);
            b2.getRelative(0,1,0).setType(Material.TORCH);

            lastCakeBlock = b2;

            Bukkit.getLogger().info("CAKE_BLOCK appeared at "+b2);

            qcp.chatManager.speak(null,"!!NOISY BLOCK!!","says",b2.getLocation(),MAX_DIST_SQR,"Hi there!");
        }

        return true;
    }
}
