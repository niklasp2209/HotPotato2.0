package de.bukkitnews.hotpotato.module.player.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Listener for handling player quit events in the game.
 * <p>
 * This class listens for the {@link PlayerQuitEvent} and ensures that
 * a player's data is saved before they leave the server. It interacts
 * with the {@link GamePlayerManager} to manage and persist player data
 * to Redis and SQL.
 */
@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

    private final @NotNull PlayerModule playerModule;

    /**
     * Handles the {@link PlayerQuitEvent} when a player leaves the server.
     * <p>
     * The method retrieves the player's data from the cache and saves it
     * asynchronously using the {@link GamePlayerManager}. If the player
     * is not found in the cache, no action is taken.
     *
     * @param event The {@link PlayerQuitEvent} triggered when a player disconnects.
     */
    @EventHandler
    public void handleQuit(@NotNull PlayerQuitEvent event) {
        GamePlayerManager gamePlayerManager = playerModule.getGamePlayerManager();

        Optional.of(gamePlayerManager.getCachedPlayer(event.getPlayer().getUniqueId().toString()))
                .ifPresent(gamePlayerManager::savePlayer);
    }
}