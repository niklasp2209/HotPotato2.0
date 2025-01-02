package de.bukkitnews.hotpotato.module.scoreboard.model;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.scoreboard.Objective;

@RequiredArgsConstructor
@Data
public abstract class ScoreboardElement {

    private final String identifier;
    private final String label;
    private final String defaultValue;

    public abstract void applyToScoreboard(@NonNull Objective objective, int scoreIndex);

    public abstract void update(@NonNull GamePlayer gamePlayer);
}
