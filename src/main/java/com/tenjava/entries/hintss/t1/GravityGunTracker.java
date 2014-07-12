package com.tenjava.entries.hintss.t1;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Henry on 7/11/2014.
 */
public class GravityGunTracker {
    private TenJava plugin;

    // hashmap with <player's uuid, entitie's uuid>
    private HashMap<UUID, UUID> playerGrabbedEntities = new HashMap<UUID, UUID>();
    // how far the thing is from the player gramming it
    // too lazy to oop this
    private HashMap<UUID, Double> grabDistance = new HashMap<UUID, Double>();

    public GravityGunTracker(TenJava plugin) {
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Iterator<UUID> iter = playerGrabbedEntities.keySet().iterator(); iter.hasNext(); ) {
                    UUID uuid = iter.next();
                    Player p = Bukkit.getPlayer(uuid);

                    if (p == null) {
                        iter.remove();
                    } else {
                        Entity e = Util.getNearbyEntityByUUID(p, playerGrabbedEntities.get(p.getUniqueId()), 10);

                        if (e == null) {
                            iter.remove();
                        } else {
                            Util.teleportIgnoreAngle(e, grabLocation(p));
                            e.setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    /**
     * gets if the player is holding a entity with their gravity gun
     * @param p the player
     * @return whether they're holding an entity
     */
    public boolean isHoldingEntity(Player p) {
        return playerGrabbedEntities.containsKey(p.getUniqueId());
    }

    /**
     * gets if an entity is being grabbed by a player's gravity gun
     * @param e the entity
     * @return if the entity is being grabbed
     */
    public boolean isBeingGrabbed(Entity e) {
        return playerGrabbedEntities.containsValue(e.getUniqueId());
    }

    /**
     * gets where an entity would be held if it was being held by this player
     * @param p the player
     * @return the location where they would be holding something
     */
    private Location grabLocation(Player p) {
        Location loc = p.getLocation();
        Vector inFront = p.getLocation().getDirection().normalize();
        loc.add(inFront.multiply(grabDistance.get(p.getUniqueId())));

        return loc;
    }

    /**
     * grabs a block
     * @param p player grabbing the block
     * @param b the block being grabbed
     */
    public void grab(Player p, Block b) {
        if (Util.canBreak(p, b) && b.getType().isSolid()) {
            Entity e = p.getWorld().spawnFallingBlock(grabLocation(p), b.getType(), b.getData());
            b.setType(Material.AIR);
            grab(p, e);
        }
    }

    public void grab(Player p, Entity e) {
        Util.teleportIgnoreAngle(e, grabLocation(p));
        playerGrabbedEntities.put(p.getUniqueId(), e.getUniqueId());
        grabDistance.put(p.getUniqueId(), p.getEyeLocation().distance(e.getLocation()));
    }

    public void throwEntity(Player p) {
        Util.getNearbyEntityByUUID(p, playerGrabbedEntities.get(p.getUniqueId()), 10).setVelocity(p.getLocation().getDirection().normalize().multiply(plugin.getConfig().getDouble("gravity_gun.throw_strength")));
        playerGrabbedEntities.remove(p.getUniqueId());
        grabDistance.remove(p.getUniqueId());
    }
}
