package de.bukkitnews.hotpotato.module.game.gamestate.ingame;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;
import lombok.NonNull;

public class IngameState extends CustomGameStates {

    public IngameState(@NonNull GameModule gameModule) {
        super(gameModule, "Ingame");
    }

    @Override
    public void activate() {
        logToConsole("Ingame state activated. Players can join.");
    }

    @Override
    public void deactivate() {
        logToConsole("Ingame state deactivated.");
    }
}