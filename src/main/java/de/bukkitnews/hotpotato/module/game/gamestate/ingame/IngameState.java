package de.bukkitnews.hotpotato.module.game.gamestate.ingame;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.AbstractGameState;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents the Ingame state in the game. This is the state where the actual game happens
 * after players have joined and the countdown has finished.
 */
public class IngameState extends AbstractGameState {

    /**
     * Constructor to initialize the Ingame state.
     *
     * @param gameModule The game module this state belongs to.
     */
    public IngameState(@NotNull GameModule gameModule) {
        super(gameModule);
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
    public void onJoin(@NotNull Player player) {
        Optional<PlayerModule> playerModuleOpt = getGameModule().getHotPotato().getModuleManager().getModule(PlayerModule.class);
        playerModuleOpt.ifPresent(playerModule -> {
            Optional<GamePlayer> gamePlayerOpt = Optional.of(playerModule.getGamePlayerManager()
                    .getCachedPlayer(player.getUniqueId().toString()));

            gamePlayerOpt.ifPresent(gamePlayer -> {
                gamePlayer.setAlive(false);

                player.getInventory().clear();
                player.setGameMode(GameMode.SPECTATOR);
            });
        });
    }


    @Override
    public void onQuit(@NotNull Player player) {

    }
}
