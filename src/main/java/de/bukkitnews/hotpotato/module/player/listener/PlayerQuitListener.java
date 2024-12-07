package de.bukkitnews.hotpotato.module.player.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
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
public class PlayerQuitListener implements Listener {

    private final PlayerModule playerModule;

    /**
     * Constructor to initialize the PlayerQuitListener with a reference to the PlayerModule.
     *
     * @param playerModule The instance of the PlayerModule containing the GamePlayerManager.
     */
    public PlayerQuitListener(PlayerModule playerModule) {
        this.playerModule = playerModule;
    }

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
    public void handleQuit(PlayerQuitEvent event) {
        // Retrieve the GamePlayerManager instance
        GamePlayerManager gamePlayerManager = this.playerModule.getGamePlayerManager();

        // Get the UUID of the player who quit
        String uuid = event.getPlayer().getUniqueId().toString();

        // Check if the player exists in the cache
        GamePlayer gamePlayer = gamePlayerManager.getCachedPlayer(uuid);
        if (gamePlayer != null) {
            // Save the player's data and remove them from the cache
            gamePlayerManager.savePlayer(gamePlayer);
        }
    }
}