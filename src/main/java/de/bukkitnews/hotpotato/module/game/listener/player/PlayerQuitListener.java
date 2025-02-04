package de.bukkitnews.hotpotato.module.game.listener.player;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

    private final @NotNull GameModule gameModule;

    /**
     * Handles player quit events and applies logic based on the current game state.
     *
     * @param event The PlayerQuitEvent triggered when a player quits.
     */
    @EventHandler
    public void handleQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        gameModule.getCurrentState().ifPresent(state -> state.onQuit(player));
    }
}
