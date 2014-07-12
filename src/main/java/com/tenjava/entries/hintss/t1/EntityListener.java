package com.tenjava.entries.hintss.t1;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Created by Henry on 7/11/2014.
 */
public class EntityListener implements Listener {
    private TenJava plugin;

    public EntityListener(TenJava plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        // cancel entities hitting stuff if stuff

        if (plugin.getGravityGunTracker().isBeingGrabbed(event.getDamager())) {
            if (!(event.getDamager() instanceof Player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        // cancel entities shooting stuff if stuff

        if (plugin.getGravityGunTracker().isBeingGrabbed(event.getEntity())) {
            if (!(event.getEntity() instanceof Player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        // cancel things getting hurt by suffocating

    }
}
