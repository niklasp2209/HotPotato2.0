package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;

public class LobbyState extends CustomGameStates {

    public LobbyState(GameModule gameModule) {
        super(gameModule, "Lobby");
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