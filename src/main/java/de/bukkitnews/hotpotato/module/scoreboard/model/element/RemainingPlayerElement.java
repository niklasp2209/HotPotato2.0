package de.bukkitnews.hotpotato.module.scoreboard.model.element;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardElement;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

/**
 * The RemainingPlayerElement represents a scoreboard element that displays the number of remaining players
 * in the game. It extends the ScoreboardElement class and provides implementation for how to apply the element
 * to the scoreboard and update its value.
 */
public class RemainingPlayerElement extends ScoreboardElement {

    public RemainingPlayerElement() {
        super("remaining_players", "Verbleibende Spieler", "0");
    }

    @Override
    public void applyToScoreboard(@NotNull Objective objective, int scoreIndex) {
        String displayName = getLabel() + ": " + getDefaultValue();
        Score score = objective.getScore(displayName);
        score.setScore(scoreIndex);
    }

    @Override
    public void update(@NotNull GamePlayer gamePlayer) {
        int remainingPlayers = 0;
        String displayName = getLabel() + ": " + remainingPlayers;
    }
}
