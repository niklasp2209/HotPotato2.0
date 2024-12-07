package de.bukkitnews.hotpotato.module.player.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Handles player join events in the game. This listener is responsible for
 * initializing player data when they join the server. It ensures that
 * the player's information is loaded from persistent storage and updated
 * with the latest details such as their current in-game name.
 */
public class PlayerJoinListener implements Listener {

    private final PlayerModule playerModule; // Reference to the parent player module.

    /**
     * Constructor for the PlayerJoinListener.
     *
     * @param playerModule The player module instance providing access to the GamePlayerManager.
     */
    public PlayerJoinListener(PlayerModule playerModule) {
        this.playerModule = playerModule;
    }

    /**
     * Handles the PlayerJoinEvent triggered when a player joins the server.
     * It loads the player's data from persistent storage (Redis/SQL) and ensures
     * their name is updated to match their current in-game name.
     *
     * @param event The PlayerJoinEvent triggered by the server.
     */
    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        // Access the GamePlayerManager to handle player data.
        GamePlayerManager gamePlayerManager = this.playerModule.getGamePlayerManager();
        // Retrieve the player's unique identifier (UUID).
        String uuid = event.getPlayer().getUniqueId().toString();

        // Load the player's data asynchronously.
        gamePlayerManager.loadPlayer(uuid).thenAccept(gamePlayer -> {
            if (gamePlayer != null) {
                // Update the player's name with their current in-game name.
                gamePlayer.setData("name", event.getPlayer().getName());
            }
        });
    }
}