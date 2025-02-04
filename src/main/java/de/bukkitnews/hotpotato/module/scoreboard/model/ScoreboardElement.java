package de.bukkitnews.hotpotato.module.scoreboard.model;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

/**
 * The ScoreboardElement class is an abstract class that represents an element on the scoreboard.
 * Each element can be applied to a scoreboard with a specific format and updated based on the game's state.
 * Derived classes must provide their own implementation for applying and updating scoreboard elements.
 */
@RequiredArgsConstructor
@Data
public abstract class ScoreboardElement {

    private final @NotNull String identifier;
    private final @NotNull String label;
    private final @NotNull String defaultValue;

    /**
     * Applies this scoreboard element to the provided Objective at a specific score index.
     * This method defines how the element is displayed on the scoreboard.
     *
     * @param objective  The scoreboard Objective to which this element will be added
     * @param scoreIndex The score index at which the element will be displayed on the scoreboard
     */
    public abstract void applyToScoreboard(@NotNull Objective objective, int scoreIndex);

    /**
     * Updates this scoreboard element based on the current state of the given GamePlayer.
     * This method is called periodically to update the element's value (e.g., remaining time or players).
     *
     * @param gamePlayer The player whose information will be used to update the scoreboard element
     */
    public abstract void update(@NotNull GamePlayer gamePlayer);
}