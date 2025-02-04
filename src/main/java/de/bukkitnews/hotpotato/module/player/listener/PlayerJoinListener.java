package de.bukkitnews.hotpotato.module.player.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles player join events in the game. This listener is responsible for
 * initializing player data when they join the server. It ensures that
 * the player's information is loaded from persistent storage and updated
 * with the latest details such as their current in-game name.
 */
@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final @NotNull PlayerModule playerModule;

    /**
     * Handles the PlayerJoinEvent triggered when a player joins the server.
     * It loads the player's data from persistent storage (Redis/SQL) and ensures
     * their name is updated to match their current in-game name.
     *
     * @param event The PlayerJoinEvent triggered by the server.
     */
    @EventHandler
    public void handleJoin(@NotNull PlayerJoinEvent event) {
        GamePlayerManager gamePlayerManager = playerModule.getGamePlayerManager();
        String uuid = event.getPlayer().getUniqueId().toString();

        gamePlayerManager.loadPlayer(uuid);
    }
}