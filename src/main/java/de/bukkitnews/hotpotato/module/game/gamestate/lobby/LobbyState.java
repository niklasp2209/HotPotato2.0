package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.task.LobbyTask;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class LobbyState extends CustomGameStates {

    private final LobbyTask lobbyTask;

    public LobbyState(@NonNull GameModule gameModule) {
        super(gameModule, "Lobby");

        this.lobbyTask = new LobbyTask(this.getGameModule());
    }

    @Override
    public void activate() {
        logToConsole("Lobby state activated. Players can join.");
    }

    @Override
    public void deactivate() {
        logToConsole("Lobby state deactivated.");
    }
}