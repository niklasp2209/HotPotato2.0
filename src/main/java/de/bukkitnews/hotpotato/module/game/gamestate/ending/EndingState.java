package de.bukkitnews.hotpotato.module.game.gamestate.ending;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Represents the Ending state in the game. This state is triggered when the game ends
 * and the players are no longer able to interact with the game.
 */
public class EndingState extends CustomGameStates {

    /**
     * Constructor to initialize the Ending state.
     *
     * @param gameModule The game module this state belongs to.
     */
    public EndingState(@NonNull GameModule gameModule) {
        super(gameModule, "Ending");
    }

    /**
     * Activates the Ending state. This method logs to the console when the Ending state
     * has been activated, indicating that the game has finished.
     */
    @Override
    public void activate() {
        logToConsole("Ending state activated. Game has finished.");
    }

    /**
     * Deactivates the Ending state. This method logs to the console when the Ending state
     * has been deactivated, indicating that the game is over.
     */
    @Override
    public void deactivate() {
        logToConsole("Ending state deactivated.");
    }

    @Override
    public void onJoin(@NonNull Player player) {
        player.kickPlayer("Game has ended");
    }

    @Override
    public void onQuit(@NonNull Player player) {

    }
}