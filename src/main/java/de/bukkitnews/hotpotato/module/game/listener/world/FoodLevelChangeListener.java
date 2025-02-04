package de.bukkitnews.hotpotato.module.game.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class FoodLevelChangeListener implements Listener {

    @EventHandler
    public void handleFoodChange(@NotNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
