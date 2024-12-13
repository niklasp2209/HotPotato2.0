package de.bukkitnews.hotpotato.module.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class BlockedListener implements Listener {

    /**
     * Handles block break events. Cancels the event to prevent blocks from being broken.
     * @param event The BlockBreakEvent that was triggered.
     */
    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event){
        event.setCancelled(true);
    }

    /**
     * Handles block place events. Cancels the event to prevent blocks from being placed.
     * @param event The BlockPlaceEvent that was triggered.
     */
    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event){
        event.setCancelled(true);
    }

    /**
     * Handles entity spawn events. Cancels the event to prevent entities from spawning.
     * @param event The EntitySpawnEvent that was triggered.
     */
    @EventHandler
    public void handleEntitySpawn(EntitySpawnEvent event){
        event.setCancelled(true);
    }

    /**
     * Handles food level change events. Cancels the event to prevent the player's food level from changing.
     * @param event The FoodLevelChangeEvent that was triggered.
     */
    @EventHandler
    public void handleFoodChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    /**
     * Handles entity damage events. Cancels the event to prevent entities from taking damage.
     * @param event The EntityDamageEvent that was triggered.
     */
    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }
}