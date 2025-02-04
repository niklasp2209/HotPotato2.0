package de.bukkitnews.hotpotato.module.scoreboard.model.element;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardElement;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

/**
 * The TimeElement represents a scoreboard element that displays the remaining time
 * for the game on the scoreboard. It extends the ScoreboardElement class and
 * provides implementation for how to apply the element to the scoreboard and
 * update its value.
 */
public class TimeElement extends ScoreboardElement {

    public TimeElement() {
        super("time", "Verbleibende Zeit", "0");
    }

    @Override
    public void applyToScoreboard(@NotNull Objective objective, int scoreIndex) {
        String displayName = getLabel() + ": " + getDefaultValue();
        Score score = objective.getScore(displayName);
        score.setScore(scoreIndex);
    }

    @Override
    public void update(@NotNull GamePlayer gamePlayer) {
        int time = 0;
        String displayName = getLabel() + ": " + time;
    }
}
