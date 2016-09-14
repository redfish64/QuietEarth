package com.rareventure.quietcraft;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HackGiveStuffCommandExecutor implements CommandExecutor {

    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory i = player.getInventory();
            i.addItem(new ItemStack(Material.TORCH, 64));
            i.addItem(new ItemStack(Material.DIAMOND_PICKAXE, 1));
            i.addItem(new ItemStack(Material.COMPASS, 1));
            i.addItem(new ItemStack(Material.PAPER, 64));
            i.addItem(new ItemStack(Material.WORKBENCH, 1));
            i.addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
            i.addItem(new ItemStack(Material.DIAMOND_PICKAXE, 1));
            i.addItem(new ItemStack(Material.BED, 3));
            i.addItem(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
            i.addItem(new ItemStack(Material.DIAMOND_HELMET, 1));
            i.addItem(new ItemStack(Material.DIAMOND_BOOTS, 1));
            i.addItem(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
            i.addItem(new ItemStack(Material.OBSIDIAN, 64));
            i.addItem(new ItemStack(Material.COMPASS, 1));
            i.addItem(new ItemStack(Material.COMPASS, 1));
            i.addItem(new ItemStack(Material.COMPASS, 1));
            i.addItem(new ItemStack(Material.COMPASS, 1));
            i.addItem(new ItemStack(Material.FLINT_AND_STEEL, 1));

            sender.sendMessage("Here you go...");
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }
}
