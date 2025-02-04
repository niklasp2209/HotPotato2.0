package de.bukkitnews.hotpotato.module.game.gamestate.ending;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.AbstractGameState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Ending state in the game. This state is triggered when the game ends
 * and the players are no longer able to interact with the game.
 */
public class EndingState extends AbstractGameState {

    /**
     * Constructor to initialize the Ending state.
     *
     * @param gameModule The game module this state belongs to.
     */
    public EndingState(@NotNull GameModule gameModule) {
        super(gameModule);
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
    public void onJoin(@NotNull Player player) {
        player.kickPlayer("Game has ended");
    }

    @Override
    public void onQuit(@NotNull Player player) {

    }
}