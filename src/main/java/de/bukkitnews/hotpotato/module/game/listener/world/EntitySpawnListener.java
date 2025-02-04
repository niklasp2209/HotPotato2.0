package de.bukkitnews.hotpotato.module.game.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void handleEntitySpawn(@NotNull EntitySpawnEvent event) {
        event.setCancelled(true);
    }
}
