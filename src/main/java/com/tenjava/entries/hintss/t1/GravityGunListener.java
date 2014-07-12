package com.tenjava.entries.hintss.t1;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

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
        if (event.getItem() != null && event.getItem().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && event.getPlayer().hasPermission("gravitygun.gun")) {

            // if they're holding a block using the gravity gun
            if (plugin.getGravityGunTracker().isHoldingEntity(event.getPlayer())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    plugin.getGravityGunTracker().throwEntity(event.getPlayer());
                    event.setCancelled(true);
                }
            } else {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    // grab block
                    if (Util.canBreak(event.getPlayer(), event.getClickedBlock())) {
                        plugin.getGravityGunTracker().grab(event.getPlayer(), event.getClickedBlock());
                    }

                    event.setCancelled(true);
                }/* else if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    // propel stuff away
                    double pushRange = plugin.getConfig().getDouble("gravity_gun.push_range");
                    double pushAngle = plugin.getConfig().getDouble("gravity_gun.push_angle");
                    List<Entity> nearbyEntities = Util.getEntitiesInFront(event.getPlayer(), pushRange, pushAngle);

                    // push away the entities in front of the player
                    for (Entity e : nearbyEntities) {
                        e.setVelocity(getPushVector(event.getPlayer(), e, plugin.getConfig().getDouble("gravity_gun.push_strength")));
                    }

                    event.setCancelled(true);
                }*/
            }
        }
    }

    @EventHandler
    public void onHit(PlayerInteractEntityEvent event) {
        // if player is holding the item set in the config and has the permission node to use it
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && event.getPlayer().hasPermission("gravitygun.gun")) {
            if (!plugin.getGravityGunTracker().isHoldingEntity(event.getPlayer())) {
                plugin.getGravityGunTracker().grab(event.getPlayer(), event.getRightClicked());
                plugin.getGravityGunTracker().throwEntity(event.getPlayer());
            }

            event.setCancelled(true);
        }
    }

    /*
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        // push entities away from the player
        if (event.getDamager() instanceof Player) {
            // if they're not already holding an entity using the gravity gun
            if (!plugin.getGravityGunTracker().isHoldingEntity((Player) event.getDamager())) {
                if (((Player) event.getDamager()).getItemInHand() != null && ((Player) event.getDamager()).getItemInHand().getType().name().equals(plugin.getConfig().getString("gravity_gun.item")) && ((Player) event.getDamager()).hasPermission("gravitygun.gun")) {
                    double pushRange = plugin.getConfig().getDouble("gravity_gun.push_range");
                    double pushAngle = plugin.getConfig().getDouble("gravity_gun.push_angle");
                    List<Entity> nearbyEntities = Util.getEntitiesInFront((Player) event.getDamager(), pushRange, pushAngle);

                    // push away the entities in front of the player
                    for (Entity e : nearbyEntities) {
                        e.setVelocity(getPushVector((Player) event.getDamager(), e, plugin.getConfig().getDouble("gravity_gun.push_strength")));
                    }

                    event.setCancelled(true);
                }
            }
        }
    }*/

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent event) {
        if (plugin.getGravityGunTracker().isHoldingEntity(event.getPlayer())) {
            plugin.getGravityGunTracker().throwEntity(event.getPlayer());
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
