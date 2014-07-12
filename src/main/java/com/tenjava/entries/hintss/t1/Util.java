package com.tenjava.entries.hintss.t1;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Henry on 7/11/2014.
 */
public class Util {
    // stupid utils class full of statics

    /**
     * checks if a palyre can build at a location
     * @param p the player
     * @param b the block they're trying to break
     * @return whether they can build there
     */
    public static boolean canBreak(Player p, Block b) {
        BlockBreakEvent event = new BlockBreakEvent(b, p);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    /**
     * gets the entities within a certain angle in front of the player
     * @param p the player we're checking from
     * @param distance the max distance that we want to look
     * @param angle the max angle from center that we want (in degrees)
     * @return a list of entities within that distance from the player, and less than that angle from center of fov
     */
    public static List<Entity> getEntitiesInFront(Player p, double distance, double angle) {
        // get the entities near the player (add the eyeheight to height to make sure we catch things that are just barely in range above us
        List<Entity> potentialEntities = p.getNearbyEntities(distance, distance + p.getEyeHeight(), distance);

        double distanceSquared = distance * distance;
        // idk how to use iterators lol
        for (Iterator<Entity> iter = potentialEntities.iterator(); iter.hasNext(); ) {
            Entity entity = iter.next();
            if (entity instanceof Hanging) {
                iter.remove();
            } else if (p.getLocation().distanceSquared(entity.getLocation()) > distanceSquared) {
                iter.remove();
            } else {
                // TODO - check angles
            }
        }

        return potentialEntities;
    }

    /**
     * teleports an entity to a location, ignoring yaw/pitch
     * @param e the entity
     * @param loc the location
     */
    public static void teleportIgnoreAngle(Entity e, Location loc) {
        Location location = loc.clone();
        location.setPitch(e.getLocation().getPitch());
        location.setYaw(e.getLocation().getYaw());

        e.teleport(location);
    }

    /**
     * looks within a certain distance of an entity for an entity with the given uuid
     * @param e the entity we're looking near
     * @param u the uuid of the entity we're looking for
     * @param range the range in which we're looking for the entity
     * @return
     */
    public static Entity getNearbyEntityByUUID(Entity e, UUID u, double range) {
        if (e == null) {
            return null;
        }

        for (Entity possible : e.getNearbyEntities(range, range, range)) {
            if (possible.getUniqueId().equals(u)) {
                return possible;
            }
        }

        return null;
    }
}
