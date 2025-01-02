package de.bukkitnews.hotpotato.module.scoreboard.model.elements;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardElement;
import lombok.NonNull;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class TimeElement extends ScoreboardElement {

    public TimeElement() {
        super("time", "Verbleibende Zeit", "0");
    }

    @Override
    public void applyToScoreboard(@NonNull Objective objective, int scoreIndex) {
        String displayName = getLabel() + ": " + getDefaultValue();
        Score score = objective.getScore(displayName);
        score.setScore(scoreIndex);
    }

    @Override
    public void update(@NonNull GamePlayer gamePlayer) {
        int time = 0;
        String displayName = getLabel() + ": " + time;
    }
}
