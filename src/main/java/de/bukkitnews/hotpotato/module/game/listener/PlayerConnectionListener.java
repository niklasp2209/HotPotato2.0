package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

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
     * Handles player join events and applies logic based on the current game state.
     *
     * @param event The PlayerJoinEvent triggered when a player joins.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void handleJoin(@NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null); // Disable default join message

        this.gameModule.getCurrentState().ifPresent(state -> {
            if (state instanceof LobbyState) {
                handleLobbyJoin(player);
            } else if (state instanceof IngameState) {
                handleIngameJoin(player);
            } else if (state instanceof EndingState) {
                player.kickPlayer("Game has ended");
            }
        });
    }

    /**
     * Handles player quit events and applies logic based on the current game state.
     *
     * @param event The PlayerQuitEvent triggered when a player quits.
     */
    @EventHandler
    public void handleQuit(@NonNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null); // Disable default quit message

        this.gameModule.getCurrentState().ifPresent(state -> {
            if (state instanceof LobbyState) {
                handleLobbyQuit(player);
            } else if (state instanceof IngameState) {
                handleIngameQuit(player);
            }
        });
    }

    /**
     * Handles logic for a player joining during the lobby state.
     * Resets the player's inventory and game mode, and ensures proper visibility with other players.
     *
     * @param player The player who joined.
     */
    private void handleLobbyJoin(@NonNull Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        showPlayersToEachOther(player);

        Optional<LobbyState> lobbyState = getCurrentLobbyState();
        lobbyState.ifPresent(state -> {
            if (Bukkit.getOnlinePlayers().size() >= 2 && !state.getLobbyTask().isRunning()) {
                state.getLobbyTask().start();
            }
        });
    }

    /**
     * Handles logic for a player joining during the in-game state.
     * Marks the player as eliminated and sets their game mode to spectator.
     *
     * @param player The player who joined.
     */
    private void handleIngameJoin(@NonNull Player player) {
        GamePlayer gamePlayer = PlayerModule.gamePlayerManager.getCachedPlayer(player.getUniqueId().toString());
        if (gamePlayer != null) {
            gamePlayer.setAlive(false);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Handles logic for a player quitting during the lobby state.
     * Adjusts the lobby task based on the remaining players.
     *
     * @param player The player who quit.
     */
    private void handleLobbyQuit(@NonNull Player player) {
        Optional<LobbyState> lobbyState = getCurrentLobbyState();
        lobbyState.ifPresent(state -> {
            if (Bukkit.getOnlinePlayers().size() < 2 && state.getLobbyTask().isRunning()) {
                state.getLobbyTask().startIdle();
            }
        });
    }

    /**
     * Handles logic for a player quitting during the in-game state.
     * (Currently not implemented but prepared for future logic).
     *
     * @param player The player who quit.
     */
    private void handleIngameQuit(@NonNull Player player) {
        // Placeholder for future in-game quit logic
    }

    /**
     * Ensures all players can see each other and updates visibility settings.
     *
     * @param player The player who joined or needs visibility updated.
     */
    private void showPlayersToEachOther(@NonNull Player player) {
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(player);
            player.showPlayer(current);
        }
    }

    /**
     * Retrieves the current game state as a LobbyState, if applicable.
     *
     * @return An Optional containing the LobbyState, or empty if not in the lobby state.
     */
    private Optional<LobbyState> getCurrentLobbyState() {
        return this.gameModule.getCurrentState()
                .filter(state -> state instanceof LobbyState)
                .map(state -> (LobbyState) state);
    }
}