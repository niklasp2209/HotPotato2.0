package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * PlayerConnectionListener handles player connection events (join/quit)
 * and manages state-specific logic for players in the game.
 */
public class PlayerConnectionListener implements Listener {

    private final GameModule gameModule;

    /**
     * Constructor to initialize the PlayerConnectionListener.
     *
     * @param gameModule The game module managing the game states and logic.
     */
    public PlayerConnectionListener(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;
    }

    /**
     * Handles player join events and delegates to the state-specific logic.
     *
     * @param event The PlayerJoinEvent triggered when a player joins.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void handleJoin(@NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        this.gameModule.getCurrentState().ifPresent(state -> state.onJoin(player));
    }

    /**
     * Handles player quit events and applies logic based on the current game state.
     *
     * @param event The PlayerQuitEvent triggered when a player quits.
     */
    @EventHandler
    public void handleQuit(@NonNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        this.gameModule.getCurrentState().ifPresent(state -> state.onQuit(player));
    }
}