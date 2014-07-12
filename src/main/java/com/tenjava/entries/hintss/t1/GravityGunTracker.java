package com.tenjava.entries.hintss.t1;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Henry on 7/11/2014.
 */
public class GravityGunTracker {
    // hashmap with <player's uuid, entitie's uuid>
    private HashMap<UUID, UUID> playerGrabbedEntities = new HashMap<UUID, UUID>();

    /**
     * gets if the player is holding a entity with their gravity gun
     * @param p the player
     * @return whether they're holding an entity
     */
    public boolean isHoldingEntity(Player p) {
        return playerGrabbedEntities.containsKey(p.getUniqueId());
    }


}
