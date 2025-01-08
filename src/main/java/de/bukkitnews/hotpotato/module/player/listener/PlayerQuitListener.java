package de.bukkitnews.hotpotato.module.player.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for handling player quit events in the game.
 *
 * This class listens for the {@link PlayerQuitEvent} and ensures that
 * a player's data is saved before they leave the server. It interacts
 * with the {@link GamePlayerManager} to manage and persist player data
 * to Redis and SQL.
 */
@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

    @NonNull private final PlayerModule playerModule;

    /**
     * Handles the {@link PlayerQuitEvent} when a player leaves the server.
     *
     * The method retrieves the player's data from the cache and saves it
     * asynchronously using the {@link GamePlayerManager}. If the player
     * is not found in the cache, no action is taken.
     *
     * @param event The {@link PlayerQuitEvent} triggered when a player disconnects.
     */
    @EventHandler
    public void handleQuit(@NonNull PlayerQuitEvent event) {
        GamePlayerManager gamePlayerManager = this.playerModule.getGamePlayerManager();

        String uuid = event.getPlayer().getUniqueId().toString();

        GamePlayer gamePlayer = gamePlayerManager.getCachedPlayer(uuid);
        if (gamePlayer != null) {
            gamePlayerManager.savePlayer(gamePlayer);
        }
    }
}