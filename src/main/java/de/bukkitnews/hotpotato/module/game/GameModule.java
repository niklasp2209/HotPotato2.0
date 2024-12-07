package de.bukkitnews.hotpotato.module.game;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class GameModule extends CustomModule {

    private CustomGameStates currentState;
    private final Collection<CustomGameStates> loadedGameStates;

    public GameModule(HotPotato hotPotato) {
        super(hotPotato, "Game");
        this.loadedGameStates = new ArrayList<>();

        // Initialisieren und Hinzufügen der GameStates
        this.loadedGameStates.add(new LobbyState(this));
        this.loadedGameStates.add(new IngameState(this));
        this.loadedGameStates.add(new EndingState(this));
    }

    @Override
    public void activate() {
        this.currentState = this.loadedGameStates.stream()
                .filter(state -> state.getName().equals("Lobby"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Lobby state not found"));

        this.currentState.start();
    }

    @Override
    public void deactivate() {
        if (this.currentState != null) {
            this.currentState.stop();
        }
    }

    public void addGameState(CustomGameStates gameState) {
        this.loadedGameStates.add(gameState);
    }
}