package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.gamestates.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestates.GameStates;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class GameModule extends CustomModule {

    private static GameStates gameState;
    private final Collection<CustomGameStates> loadedGameState;

    public GameModule(HotPotato hotPotato){
        super(hotPotato, "Game");
        gameState = GameStates.LOBBY;
        loadedGameState = new ArrayList<>();
    }

    @Override
    public void activate() {
        //setListeners(Arrays.asList());
        start();
    }

    @Override
    public void deactivate() {

    }
}
