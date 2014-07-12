package com.tenjava.entries.hintss.t1;

import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {
    private GravityGunTracker gravityGunTracker;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.gravityGunTracker = new GravityGunTracker(this);

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new GravityGunListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    public GravityGunTracker getGravityGunTracker() {
        return gravityGunTracker;
    }
}
