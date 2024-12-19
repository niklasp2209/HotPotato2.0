package de.bukkitnews.hotpotato.module.game.gamestate.ingame;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Represents the Ingame state in the game. This is the state where the actual game happens
 * after players have joined and the countdown has finished.
 */
public class IngameState extends CustomGameStates {

    /**
     * Constructor to initialize the Ingame state.
     *
     * @param gameModule The game module this state belongs to.
     */
    public IngameState(@NonNull GameModule gameModule) {
        super(gameModule, "Ingame");
    }

    /**
     * Activates the Ingame state. This method logs to the console when the Ingame state
     * has been activated, indicating that players can now participate in the game.
     */
    @Override
    public void activate() {
        logToConsole("Ingame state activated. Players can join.");
    }

    /**
     * Deactivates the Ingame state. This method logs to the console when the Ingame state
     * has been deactivated.
     */
    @Override
    public void deactivate() {
        logToConsole("Ingame state deactivated.");
    }

    @Override
    public void onJoin(@NonNull Player player) {
        GamePlayer gamePlayer = this.getGameModule().getHotPotato().getModuleManager().getModule(PlayerModule.class).get()
                .getGamePlayerManager().getCachedPlayer(player.getUniqueId().toString());
        if (gamePlayer != null) {
            gamePlayer.setAlive(false);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }

    @Override
    public void onQuit(@NonNull Player player) {

    }
}
