package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.AbstractGameState;
import de.bukkitnews.hotpotato.module.game.util.GameItems;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the lobby state of the game.
 * This state is active when players are waiting for the game to start.
 * Manages the lobby countdown and handles player interactions during this phase.
 */
@Getter
public class LobbyState extends AbstractGameState {

    private final @NotNull LobbyCountdown lobbyCountdown;

    /**
     * Constructor to initialize the lobby state with the associated game module.
     * Creates a new {@link LobbyCountdown} to manage the countdown and idle behavior.
     *
     * @param gameModule The game module this state belongs to.
     */
    public LobbyState(@NotNull GameModule gameModule) {
        super(gameModule);
        this.lobbyCountdown = new LobbyCountdown(getGameModule());
    }

    /**
     * Activates the lobby state.
     * Typically called when the game transitions to the lobby phase.
     */
    @Override
    public void activate() {
        logToConsole("Lobby state activated. Players can join.");
    }

    /**
     * Deactivates the lobby state.
     * Typically called when the game transitions to a different state.
     */
    @Override
    public void deactivate() {
        logToConsole("Lobby state deactivated.");
    }

    /**
     * Handles the player join event for the lobby state.
     * Gives the player the appropriate lobby items, sets their game mode to survival,
     * and ensures that all players can see each other.
     * Starts the lobby countdown if there are enough players.
     *
     * @param player The player who joined the game.
     */
    @Override
    public void onJoin(@NotNull Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        giveLobbyItems(player);
        showPlayersToEachOther(player);

        if (Bukkit.getOnlinePlayers().size() >= 2 && !getLobbyCountdown().isRunning()) {
            getLobbyCountdown().start();
        }
    }

    /**
     * Handles the player quit event for the lobby state.
     * If there are fewer than 2 players remaining in the lobby, the countdown task will switch to idle mode.
     *
     * @param player The player who left the game.
     */
    @Override
    public void onQuit(@NotNull Player player) {
        if (Bukkit.getOnlinePlayers().size() < 2 && getLobbyCountdown().isRunning()) {
            getLobbyCountdown().startIdle();
        }
    }

    /**
     * Gives the player the required items for the lobby state, such as the voting item.
     * Clears the player's inventory and sets the voting item in the first slot.
     *
     * @param player The player to whom the lobby items will be given.
     */
    private void giveLobbyItems(@NotNull Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, GameItems.ITEM_LOBBY_VOTING);
    }

    /**
     * Ensures all players can see each other by updating their visibility settings.
     * This method is used when a player joins the game and allows them to see other players already in the lobby.
     *
     * @param player The player who joined the game and needs to be visible to others.
     */
    private void showPlayersToEachOther(@NotNull Player player) {
        Bukkit.getOnlinePlayers()
                .forEach(current -> {
                    current.showPlayer(player);
                    player.showPlayer(current);
                });
    }
}