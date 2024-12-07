package de.bukkitnews.hotpotato.module.game.gamestate.ending;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.CustomGameStates;

public class EndingState extends CustomGameStates {

    public EndingState(GameModule gameModule) {
        super(gameModule, "Ending");
    }

    @Override
    public void activate() {
        logToConsole("Ending state activated. Players can join.");
    }

    @Override
    public void deactivate() {
        logToConsole("Ending state deactivated.");
    }
}