package com.linchen.parkour.event;

import com.linchen.parkour.Jumpandrun;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StartJumpAndRun implements Listener {

    static HashMap<Player,Location> coords =  new HashMap();

    static HashMap<Player,Integer> jumpState = new HashMap();

    static Integer jumpStateInt = 0;

    static Integer jumpStateIntTop = 1;

    static HashMap<Player, Integer> cooldown = new HashMap();

    private final CooldownManager cooldownManager = new CooldownManager();

    static HashMap<Player, GameMode> playerGameModeHashMap = new HashMap();

    @EventHandler
    public void onPressue(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        World world = player.getWorld();

        ArrayList<String> backToCheckpointLore = new ArrayList();
        backToCheckpointLore.add(ChatColor.GREEN + "Teleport back to your last checkpoint");

        ArrayList<String> quitJumpAndRunLore = new ArrayList();
        backToCheckpointLore.add(ChatColor.GREEN + "Quit the jump and run");

        PlayerInventory inventory = player.getInventory();

        int timeLeft = cooldownManager.getCooldown(player);

        if (event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {

            player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GREEN + "You started the parkour!");


            ItemStack backToCheckpoint = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta meta_backToCheckpoint = backToCheckpoint.getItemMeta();

            ItemStack quitJumpAndRun = new ItemStack(Material.RED_CONCRETE);
            ItemMeta meta_quitJumpAndRun = quitJumpAndRun.getItemMeta();

            meta_backToCheckpoint.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Back to Checkpoint");
            meta_backToCheckpoint.setLore(backToCheckpointLore);
            backToCheckpoint.setItemMeta(meta_backToCheckpoint);

            inventory.setItem(3, backToCheckpoint);

            meta_quitJumpAndRun.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Quit");
            meta_quitJumpAndRun.setLore(quitJumpAndRunLore);
            quitJumpAndRun.setItemMeta(meta_quitJumpAndRun);

            inventory.setItem(5, quitJumpAndRun);

            coords.put(player, player.getLocation());
            jumpState.put(player, jumpStateInt+1);
            jumpStateIntTop++;

            playerGameModeHashMap.put(player, player.getGameMode());
            player.setGameMode(GameMode.ADVENTURE);
            player.setFoodLevel(20);

            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.FALL_DAMAGE, false);

            cooldown.put(player, ((int) System.currentTimeMillis() / 1000));
            System.out.println(cooldown);
        } else if (event.getAction().equals(Action.PHYSICAL) && Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.STONE_PRESSURE_PLATE)) {

            if (Math.ceil(coords.get(player).getX()) != Math.ceil(player.getLocation().getX())) {
                jumpStateIntTop++;
            }
            if (jumpState.get(player) < jumpStateIntTop) {
                player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GREEN + "New checkpoint has been set!");

                jumpState.replace(player, jumpState.get(player), jumpState.get(player)+1);
                coords.replace(player, player.getLocation());
                player.setFoodLevel(20);
            }
        } else if (event.getAction().equals(Action.PHYSICAL) && Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
            if (coords.containsKey(player)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inventory.remove(Material.RED_CONCRETE);
                        inventory.remove(Material.GREEN_CONCRETE);
                        player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GOLD + "Finished parkour!");
                        jumpState.replace(player,0);
                        player.setGameMode(playerGameModeHashMap.get(player));
                    }
                }.runTaskLater(Jumpandrun.getInstance(), 20);
            }

        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (inventory.getItemInMainHand().getType().equals(Material.GREEN_CONCRETE) && inventory.getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN.toString() + ChatColor.BOLD + "Back to Checkpoint")) {
                if (coords.containsKey(player)) {
                    if (timeLeft == 0) {
                        player.teleport(coords.get(player));
                        player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GREEN + "Teleported to last checkpoint!");

                        cooldownManager.setCooldown(player, CooldownManager.DEFAULT_COOLDOWN);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int timeLeft = cooldownManager.getCooldown(player);
                                cooldownManager.setCooldown(player, --timeLeft);
                                if(timeLeft == 0){
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Jumpandrun.getInstance(), 20, 20);
                    } else {
                        //Hasn't expired yet, shows how many seconds left until it does
                        player.sendMessage(ChatColor.RED.toString() + timeLeft + " seconds before you can use this feature again.");
                    }
                }
            } else if (inventory.getItemInMainHand().getType().equals(Material.RED_CONCRETE)) {
                new BukkitRunnable() {
                        @Override
                        public void run() {
                            inventory.remove(Material.RED_CONCRETE);
                            inventory.remove(Material.GREEN_CONCRETE);
                            player.sendMessage(ChatColor.BLUE + "Parkour" + ChatColor.GOLD + " > " + ChatColor.GREEN + "Quit parkour!");
                            jumpState.replace(player,0);
                            player.setGameMode(playerGameModeHashMap.get(player));
                        }
                }.runTaskLater(Jumpandrun.getInstance(), 20);
            }
        }
    }

}
