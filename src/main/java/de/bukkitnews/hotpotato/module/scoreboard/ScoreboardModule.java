package de.bukkitnews.hotpotato.module.scoreboard;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardController;
import de.bukkitnews.hotpotato.module.scoreboard.model.element.RemainingPlayerElement;
import de.bukkitnews.hotpotato.module.scoreboard.model.element.TimeElement;
import lombok.NonNull;

import java.util.Optional;

/**
 * The ScoreboardModule manages the creation and updating of scoreboards for players in different game phases.
 * It supports both the lobby phase and the ingame phase, providing different scoreboard configurations.
 */
public class ScoreboardModule extends CustomModule {

    private Optional<ScoreboardController> lobbyBoard = Optional.empty();
    private Optional<ScoreboardController> ingameBoard = Optional.empty();

    public ScoreboardModule(@NonNull HotPotato hotPotato) {
        super(hotPotato, "Game");
    }

    /**
     * Activates the scoreboard module. This method sets up the scoreboards for the lobby and ingame phases,
     * registering relevant elements (e.g., TimeElement, RemainingPlayerElement) for each phase.
     */
    @Override
    public void activate() {
        this.lobbyBoard = Optional.of(new ScoreboardController(getHotPotato()));
        this.lobbyBoard.ifPresent(controller -> {
            controller.registerElement(new TimeElement());
        });

        ingameBoard = Optional.of(new ScoreboardController(getHotPotato()));
        ingameBoard.ifPresent(controller -> {
            controller.registerElement(new TimeElement());
            controller.registerElement(new RemainingPlayerElement());
        });
    }

    /**
     * Deactivates the scoreboard module by clearing any active scoreboards for both the lobby and ingame phases.
     */
    @Override
    public void deactivate() {
        lobbyBoard.ifPresent(ScoreboardController::clearScoreboards);
        ingameBoard.ifPresent(ScoreboardController::clearScoreboards);
    }

    public void applyLobbyScoreboard(GamePlayer gamePlayer){
        lobbyBoard.ifPresent(controller -> controller.applyScoreboard(gamePlayer));
    }

    public void applyIngameScoreboard(GamePlayer gamePlayer){
        ingameBoard.ifPresent(controller -> controller.applyScoreboard(gamePlayer));
    }
}
