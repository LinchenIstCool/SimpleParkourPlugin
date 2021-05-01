package com.linchen.parkour.command;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CreateParkour implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        ArrayList<String> startPlateLore = new ArrayList();

        ArrayList<String> checkpointPlateLore = new ArrayList();

        ArrayList<String> finishPlateLore = new ArrayList();

        if (sender instanceof Player) {
            ItemStack startPlate = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
            ItemMeta meta_startPlate = startPlate.getItemMeta();

            meta_startPlate.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Start Plate");

            startPlateLore.add(" ");
            startPlateLore.add(ChatColor.GREEN + "Place the start plate only once!");
            meta_startPlate.setLore(startPlateLore);

            startPlate.setItemMeta(meta_startPlate);

            ItemStack checkpointPlate = new ItemStack(Material.STONE_PRESSURE_PLATE);
            ItemMeta meta_checkpointPlate = checkpointPlate.getItemMeta();

            meta_checkpointPlate.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + "Checkpoint Plate");

            checkpointPlateLore.add(" ");
            checkpointPlateLore.add(ChatColor.GRAY + "Place the checkpoint plate");
            checkpointPlateLore.add(ChatColor.GRAY + "at every checkpoint!");
            meta_checkpointPlate.setLore(checkpointPlateLore);

            checkpointPlate.setItemMeta(meta_checkpointPlate);

            ItemStack finishPlate = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            ItemMeta meta_finishPlate = finishPlate.getItemMeta();

            meta_finishPlate.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Finish Plate");

            finishPlateLore.add(" ");
            finishPlateLore.add(ChatColor.RED + "Place the finish plate only once!");
            meta_finishPlate.setLore(finishPlateLore);

            finishPlate.setItemMeta(meta_finishPlate);

            player.getInventory().addItem(startPlate, checkpointPlate, finishPlate);
            player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GREEN + "Please look at the descriptions of the plates!");
        }
        return true;
    }

}
