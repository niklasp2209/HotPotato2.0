package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.commands.HotPotatoCommand;
import de.bukkitnews.hotpotato.module.game.commands.StartCommand;
import de.bukkitnews.hotpotato.module.game.commands.StatsCommand;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.game.listener.AsyncPlayerChatListener;
import de.bukkitnews.hotpotato.module.game.listener.BlockedListener;
import de.bukkitnews.hotpotato.module.game.listener.PlayerConnectionListener;
import de.bukkitnews.hotpotato.module.game.listener.PlayerInteractListener;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * GameModule manages the overall game state and player interactions.
 * It transitions between game states, handles core game logic,
 * and interacts with game-related listeners and players.
 */
@Getter
public class GameModule extends CustomModule {

    @NonNull private Optional<CustomGameStates> currentState;

    public GameModule(@NonNull HotPotato hotPotato) {
        super(hotPotato, "Game");
        this.currentState = Optional.empty();
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
                new BlockedListener(),
                new PlayerConnectionListener(this),
                new PlayerInteractListener(this)
        ));

        getCommandExecutors().put("hotpotato", new HotPotatoCommand(this));
        getCommandExecutors().put("start", new StartCommand(this));
        getCommandExecutors().put("stats", new StatsCommand(this));

        this.currentState.ifPresent(CustomGameStates::start);
    }

    /**
     * Deactivates the GameModule by stopping the current game state (if any).
     */
    @Override
    public void deactivate() {
        currentState.ifPresent(CustomGameStates::stop);
        this.currentState = Optional.empty();
    }

    /**
     * Transitions to a new game state, deactivating the current state if necessary.
     *
     * @param newState The new state to transition to.
     */
    public void setCurrentState(@NonNull CustomGameStates newState) {
        this.currentState.ifPresent(CustomGameStates::deactivate);
        this.currentState = Optional.of(newState);
        newState.start();
    }

    /**
     * Eliminates the player currently holding the "potato."
     * Sets the player's status to dead and switches their game mode to spectator.
     */
    public void eliminatePlayer() {
        for(GamePlayer gamePlayer : getHotPotato().getModuleManager().getModule(PlayerModule.class).get()
                .getGamePlayerManager().getPlayerCache().values()){
            if(gamePlayer.isPotato()){
                gamePlayer.eliminatePlayer();
            }
        }
    }

    /**
     * Checks the remaining active players in the game and determines if the game
     * should proceed, end, or transition to a different state.
     */
    private void checkRemainingPlayers() {
        long aliveCount = getHotPotato().getModuleManager().getModule(PlayerModule.class).get()
                .getGamePlayerManager().getPlayerCache().values().stream()
                .filter(GamePlayer::isAlive)
                .count();

        if (aliveCount <= 1) {
            this.setCurrentState(new EndingState(this));
        }
    }

}