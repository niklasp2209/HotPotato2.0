package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.task.LobbyTask;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the lobby state of the game.
 * This state is active when players are waiting for the game to start.
 * Manages the lobby countdown and handles player interactions during this phase.
 */
@Getter
public class LobbyState extends CustomGameStates {

    private final LobbyTask lobbyTask; // The countdown task for the lobby phase

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
}