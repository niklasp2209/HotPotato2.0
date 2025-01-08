package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.task.LobbyTask;
import de.bukkitnews.hotpotato.module.game.util.GameItems;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Represents the lobby state of the game.
 * This state is active when players are waiting for the game to start.
 * Manages the lobby countdown and handles player interactions during this phase.
 */
@Getter
public class LobbyState extends CustomGameStates {

    @NonNull private final LobbyTask lobbyTask;

    /**
     * Constructor to initialize the lobby state with the associated game module.
     * Creates a new {@link LobbyTask} to manage the countdown and idle behavior.
     *
     * @param gameModule The game module this state belongs to.
     */
    public LobbyState(@NonNull GameModule gameModule) {
        super(gameModule, "Lobby");
        this.lobbyTask = new LobbyTask(this.getGameModule());
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
    public void onJoin(@NonNull Player player) {
        giveLobbyItems(player);
        player.setGameMode(GameMode.SURVIVAL);
        showPlayersToEachOther(player);

        if (Bukkit.getOnlinePlayers().size() >= 2 && !getLobbyTask().isRunning()) {
            getLobbyTask().start();
        }
    }

    /**
     * Handles the player quit event for the lobby state.
     * If there are fewer than 2 players remaining in the lobby, the countdown task will switch to idle mode.
     *
     * @param player The player who left the game.
     */
    @Override
    public void onQuit(@NonNull Player player) {
        if (Bukkit.getOnlinePlayers().size() < 2 && getLobbyTask().isRunning()) {
            getLobbyTask().startIdle();
        }
    }

    /**
     * Gives the player the required items for the lobby state, such as the voting item.
     * Clears the player's inventory and sets the voting item in the first slot.
     *
     * @param player The player to whom the lobby items will be given.
     */
    private void giveLobbyItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, GameItems.ITEM_LOBBY_VOTING);
    }

    /**
     * Ensures all players can see each other by updating their visibility settings.
     * This method is used when a player joins the game and allows them to see other players already in the lobby.
     *
     * @param player The player who joined the game and needs to be visible to others.
     */
    private void showPlayersToEachOther(Player player) {
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(player);
            player.showPlayer(current);
        }
    }

}