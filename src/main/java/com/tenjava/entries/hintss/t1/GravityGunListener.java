package com.tenjava.entries.hintss.t1;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by Henry on 7/11/2014.
 */
public class GravityGunListener implements Listener {
    TenJava plugin;

    public GravityGunListener(TenJava plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHit(PlayerInteractEvent event) {
        // checks if the player is holding the item set in the config and has the permission node to use it
        // TODO - come up with a plugin name to put in the permission
        if (event.getItem() != null && event.getItem().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && event.getPlayer().hasPermission("tenjava.gravitygun")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // grab block
                if (Util.canBreak(event.getPlayer(), event.getClickedBlock())) {
                    // TODO - grab block
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                // grab block from distance

                // TODO - get block

                // TODO - check break perms

                // TODO - grab block
            } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
                // propel stuff away
                double pushRange = plugin.getConfig().getDouble("gravity_gun.push_range");
                double pushAngle = plugin.getConfig().getDouble("gravity_gun.push_angle");
                List<Entity> nearbyEntities = Util.getEntitiesInFront(event.getPlayer(), pushRange, pushAngle);

                for (Entity e : nearbyEntities) {
                    e.setVelocity(getPushVector(event.getPlayer(), e, plugin.getConfig().getDouble("gravity_gun.push_strength")));
                }
                // TODO - push those entities
            }
        }
    }

    @EventHandler
    public void onHit(PlayerInteractEntityEvent event) {
        // if player is holding the item set in the config and has the permission node to use it
        // TODO - come up with plugin name to put in permission node
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && event.getPlayer().hasPermission("tenjava.gravitygun")) {
            // TODO - pick up entity
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            // TODO - plugin name for permission node (again)
            if (((Player) event.getDamager()).getItemInHand() != null && ((Player) event.getDamager()).getItemInHand().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && ((Player) event.getDamager()).hasPermission("tenjava.gravitygun")) {
                event.getEntity().setVelocity(getPushVector((Player) event.getDamager(), event.getEntity(), plugin.getConfig().getDouble("gravity_gun.push_strength")));
            }
        }
    }

    /**
     * Calculates the vector you'd need to push an entity away from a player
     * @param p player being pushed away from
     * @param e entity being pushed
     * @param magnitude the magnitude of the returned vector
     * @return vector to apply to the entity
     */
    private Vector getPushVector(Player p, Entity e, double magnitude) {
        Location entityLoc = e.getLocation();
        entityLoc.subtract(p.getLocation());

        Vector velocity = entityLoc.toVector();
        velocity.normalize();
        velocity.multiply(magnitude);

        return velocity;
    }
}
