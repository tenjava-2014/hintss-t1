package com.tenjava.entries.hintss.t1;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Henry on 7/11/2014.
 */
public class GravityGunTracker {
    // hashmap with <player's uuid, entitie's uuid>
    private HashMap<UUID, UUID> playerGrabbedEntities = new HashMap<UUID, UUID>();

    public GravityGunTracker(TenJava plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID u : playerGrabbedEntities.keySet()) {
                    Player p = Bukkit.getPlayer(u);
                    Entity e = Util.getNearbyEntityByUUID(p, playerGrabbedEntities.get(p.getUniqueId()), 10);


                    if (e != null) {
                        Util.teleportIgnoreAngle(e, grabLocation(p));
                        e.setVelocity(new Vector(0, 0, 0));
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
        loc.add(inFront.multiply(5));

        return loc;
    }

    /**
     * grabs a block
     * @param p player grabbing the block
     * @param b the block being grabbed
     */
    public void grab(Player p, Block b) {
        if (Util.canBreak(p, b)) {
            Entity e = p.getWorld().spawnFallingBlock(grabLocation(p), b.getType(), b.getData());
            grab(p, e);
        }
    }

    public void grab(Player p, Entity e) {
        Util.teleportIgnoreAngle(e, grabLocation(p));
        playerGrabbedEntities.put(p.getUniqueId(), e.getUniqueId());
    }

    public void throwEntity(Player p) {
        Util.getNearbyEntityByUUID(p, playerGrabbedEntities.get(p.getUniqueId()), 10);
        playerGrabbedEntities.remove(p.getUniqueId());
    }
}
