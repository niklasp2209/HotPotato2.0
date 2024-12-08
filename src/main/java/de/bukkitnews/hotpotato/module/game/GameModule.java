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
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class GameModule extends CustomModule {

    private CustomGameStates currentState;
    private final Collection<CustomGameStates> loadedGameStates;

    private final List<Player> spectator;
    private final List<Player> alive;

    public GameModule(HotPotato hotPotato) {
        super(hotPotato, "Game");
        this.loadedGameStates = new ArrayList<>();
        this.spectator = new ArrayList<>();
        this.alive = new ArrayList<>();

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

        setListeners(Arrays.asList(new AsyncPlayerChatListener(this)));

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