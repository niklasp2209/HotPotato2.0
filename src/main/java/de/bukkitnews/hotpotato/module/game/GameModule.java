package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.commands.HotPotatoCommand;
import de.bukkitnews.hotpotato.module.game.commands.StartCommand;
import de.bukkitnews.hotpotato.module.game.gamestate.AbstractGameState;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.game.listener.player.AsyncPlayerChatListener;
import de.bukkitnews.hotpotato.module.game.listener.player.PlayerInteractListener;
import de.bukkitnews.hotpotato.module.game.listener.player.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.game.listener.player.PlayerQuitListener;
import de.bukkitnews.hotpotato.module.game.listener.world.*;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * GameModule manages the overall game state and player interactions.
 * It transitions between game states, handles core game logic,
 * and interacts with game-related listeners and players.
 */
@Getter
public class GameModule extends CustomModule {

    private @NotNull Optional<AbstractGameState> currentState = Optional.empty();

    public GameModule(@NotNull HotPotato hotPotato) {
        super(hotPotato, "Game");
    }

    /**
     * Activates the GameModule by initializing the default state (LobbyState)
     * and registering all relevant event listeners.
     */
    @Override
    public void activate() {
        this.currentState = Optional.of(new LobbyState(this));
        setListeners(Arrays.asList(
                new AsyncPlayerChatListener(this),
                new BlockBreakListener(),
                new BlockPlaceListener(),
                new EntityDamageListener(),
                new EntitySpawnListener(),
                new FoodLevelChangeListener(),
                new PlayerJoinListener(this),
                new PlayerQuitListener(this),
                new PlayerInteractListener(this)
        ));

        getCommandExecutors().put("hotpotato", new HotPotatoCommand(this));
        getCommandExecutors().put("start", new StartCommand(this));

        currentState.ifPresent(AbstractGameState::activate);
    }

    /**
     * Deactivates the GameModule by stopping the current game state (if any).
     */
    @Override
    public void deactivate() {
        currentState.ifPresent(AbstractGameState::deactivate);
        this.currentState = Optional.empty();
    }

    /**
     * Transitions to a new game state, deactivating the current state if necessary.
     *
     * @param newState The new state to transition to.
     */
    public void setCurrentState(@NotNull AbstractGameState newState) {
        currentState.ifPresent(AbstractGameState::deactivate);
        this.currentState = Optional.of(newState);
        newState.activate();
    }

    /**
     * Eliminates the player currently holding the "potato."
     * Sets the player's status to dead and switches their game mode to spectator.
     */
    public void eliminatePlayer() {
        getHotPotato().getModuleManager()
                .getModule(PlayerModule.class)
                .map(PlayerModule::getGamePlayerManager)
                .map(manager -> manager.getPlayerCache().values())
                .ifPresent(players -> players.forEach(gamePlayer -> {
                    if (gamePlayer.isPotato()) {
                        gamePlayer.eliminatePlayer();
                    }
                }));
    }

    /**
     * Checks the remaining active players in the game and determines if the game
     * should proceed, end, or transition to a different state.
     */
    private void checkRemainingPlayers() {
        getHotPotato().getModuleManager()
                .getModule(PlayerModule.class)
                .map(PlayerModule::getGamePlayerManager)
                .map(manager -> manager.getPlayerCache().values().stream()
                        .filter(GamePlayer::isAlive)
                        .count())
                .ifPresent(aliveCount -> {
                    if (aliveCount <= 1) {
                        setCurrentState(new EndingState(this));
                    }
                });
    }

}