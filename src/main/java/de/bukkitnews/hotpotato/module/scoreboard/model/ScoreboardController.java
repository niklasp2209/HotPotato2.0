package de.bukkitnews.hotpotato.module.scoreboard.model;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The ScoreboardController is responsible for managing the scoreboard for players.
 * It handles the creation, updating, and clearing of scoreboards, as well as the registration
 * of scoreboard elements. It allows scoreboards to be customized by adding various elements
 * like the time or remaining players.
 */
public class ScoreboardController {

    @NonNull private final HotPotato hotPotato;
    @NonNull private final List<ScoreboardElement> elements;
    @NonNull private Optional<Scoreboard> scoreboard = Optional.empty();

    public ScoreboardController(@NonNull HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.elements = new ArrayList<>();
    }

    /**
     * Registers a new scoreboard element to be displayed on the scoreboard.
     *
     * @param scoreboardElement The element to add to the scoreboard
     */
    public void registerElement(@NonNull ScoreboardElement scoreboardElement) {
        elements.add(scoreboardElement);
    }

    /**
     * Applies a new scoreboard to the specified player. This involves creating a new scoreboard,
     * registering an objective, and applying all registered scoreboard elements to it.
     * The player is then assigned this newly created scoreboard.
     *
     * @param gamePlayer The player to apply the scoreboard to
     */
    public void applyScoreboard(@NonNull GamePlayer gamePlayer) {
        Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = newScoreboard.registerNewObjective("hotpotato", Criteria.DUMMY, "§lHot Potato");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int scoreIndex = elements.size() * 2 + (elements.size() - 1);
        for (ScoreboardElement element : elements) {
            element.applyToScoreboard(objective, scoreIndex);
            scoreIndex--;
        }

        scoreboard = Optional.of(newScoreboard);
        Bukkit.getPlayer(UUID.fromString(gamePlayer.getUuid())).setScoreboard(newScoreboard);
    }

    /**
     * Updates the scoreboard for the given player by updating all registered scoreboard elements.
     *
     * @param gamePlayer The player whose scoreboard elements should be updated
     */
    public void updateScoreboard(@NonNull GamePlayer gamePlayer) {
        elements.forEach(element -> element.update(gamePlayer));
    }

    /**
     * Clears all scoreboards, resetting any active scoreboards for all online players to the default scoreboard.
     * This method is typically called when the game ends or when the module is deactivated.
     */
    public void clearScoreboards() {
        scoreboard.ifPresent(scoreboard -> {
            Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
        });
        scoreboard = Optional.empty();
    }
}
