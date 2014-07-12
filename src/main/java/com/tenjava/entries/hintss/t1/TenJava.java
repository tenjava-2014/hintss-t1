package com.tenjava.entries.hintss.t1;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    /**
     * gets the instance of the gravityguntracker we're using
     * @return the instance of gravityguntracker
     */
    public GravityGunTracker getGravityGunTracker() {
        return gravityGunTracker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gravitygun")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("gravitygun.reload")) {
                    reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "reloaded config!");
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission for that!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "/gravitygun reload");
            }

            return true;
        } else {
            return false;
        }
    }
}
