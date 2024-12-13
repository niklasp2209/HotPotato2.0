package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.game.listener.AsyncPlayerChatListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerQuitListener;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GameModule extends CustomModule {

    private Optional<CustomGameStates> currentState;

    public GameModule(HotPotato hotPotato) {
        super(hotPotato, "Game");
        this.currentState = Optional.empty();
    }

    @Override
    public void activate() {
        this.currentState = Optional.of(new LobbyState(this));

        setListeners(Arrays.asList(new AsyncPlayerChatListener()));

        this.currentState.get().start();
    }

    @Override
    public void deactivate() {
        currentState.ifPresent(CustomGameStates::stop);
    }

    public void setCurrentState(CustomGameStates newState) {
        this.currentState.ifPresent(CustomGameStates::deactivate);
        this.currentState = Optional.of(newState);
    }
}