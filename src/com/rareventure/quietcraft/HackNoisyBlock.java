package com.rareventure.quietcraft;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HackNoisyBlock implements CommandExecutor {

    private Block lastCakeBlock;

    private static final double MAX_DIST_SQR = 20*20;
    private static final MathUtil.RandomNormalParams RNP =
            new MathUtil.RandomNormalParams(5,3,1,1000);

    private QuietCraftPlugin qcp;

    public HackNoisyBlock(QuietCraftPlugin qcp) {
        this.qcp = qcp;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(lastCakeBlock != null)
            {
                qcp.chatManager.speak("!!NOISY BLOCK!!",lastCakeBlock.getLocation(),MAX_DIST_SQR,"Bye!");
                lastCakeBlock.setType(Material.AIR);
                lastCakeBlock.getRelative(0,1,0).setType(Material.AIR);
            }

            Location l = player.getLocation();

            int x = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);
            int y = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);
            int z = MathUtil.normalRandomInt(RNP) * (Math.random() < .5 ? 1 : -1);

            Block b2 = l.getBlock().getRelative(x,y,z);

            b2.setType(Material.DIAMOND_BLOCK);
            b2.getRelative(0,1,0).setType(Material.TORCH);

            lastCakeBlock = b2;

            Bukkit.getLogger().info("CAKE_BLOCK appeared at "+b2);

            qcp.chatManager.speak("!!NOISY BLOCK!!",b2.getLocation(),MAX_DIST_SQR,"Eat me!");
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
