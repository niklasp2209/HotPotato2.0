package de.bukkitnews.hotpotato.module.game.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void handleBlockPlace(@NotNull BlockPlaceEvent event) {
        event.setCancelled(true);
    }
}
