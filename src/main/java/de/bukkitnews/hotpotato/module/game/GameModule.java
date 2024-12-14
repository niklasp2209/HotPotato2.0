package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.game.listener.AsyncPlayerChatListener;
import de.bukkitnews.hotpotato.module.game.listener.BlockedListener;
import de.bukkitnews.hotpotato.module.game.listener.PlayerConnectionListener;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.listener.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerQuitListener;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GameModule extends CustomModule {

    private Optional<CustomGameStates> currentState;
    private Player potato;

    public GameModule(@NonNull HotPotato hotPotato) {
        super(hotPotato, "Game");
    }

    @Override
    public void activate() {
        this.currentState = Optional.of(new LobbyState(this));

        setListeners(Arrays.asList(new AsyncPlayerChatListener(), new BlockedListener(),
                new PlayerConnectionListener(this)));

        this.currentState.get().start();
    }

    @Override
    public void deactivate() {
        currentState.ifPresent(CustomGameStates::stop);
    }

    public void setCurrentState(@NonNull CustomGameStates newState) {
        this.currentState.ifPresent(CustomGameStates::deactivate);
        this.currentState = Optional.of(newState);
    }

    public void eliminatePlayer(){
        GamePlayer gamePlayer = PlayerModule.gamePlayerManager.getCachedPlayer(this.potato.getUniqueId().toString());
        gamePlayer.setAlive(false);
        this.potato.getInventory().clear();
        this.potato.setGameMode(GameMode.SPECTATOR);
    }

    private void checkRemainingPlayers(){

    }
}