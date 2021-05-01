package com.linchen.parkour;

import com.linchen.parkour.command.CreateParkour;
import com.linchen.parkour.event.StartJumpAndRun;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Jumpandrun extends JavaPlugin {

    private static Jumpandrun instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new StartJumpAndRun(), this);

        this.getCommand("create").setExecutor(new CreateParkour());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Jumpandrun getInstance() {
        return instance;
    }
}
