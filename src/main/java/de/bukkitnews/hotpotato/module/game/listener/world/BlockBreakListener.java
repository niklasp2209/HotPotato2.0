package de.bukkitnews.hotpotato.module.game.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void handleBlockBreak(@NotNull BlockBreakEvent event) {
        event.setCancelled(true);
    }
}
