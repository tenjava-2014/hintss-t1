package com.tenjava.entries.hintss.t1;

import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {
    private GravityGunTracker gravityGunTracker = new GravityGunTracker();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new GravityGunListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    public GravityGunTracker getGravityGunTracker() {
        return gravityGunTracker;
    }
}
