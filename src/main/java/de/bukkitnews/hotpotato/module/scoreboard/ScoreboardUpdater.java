package de.bukkitnews.hotpotato.module.scoreboard;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardController;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * The ScoreboardUpdater class is responsible for periodically updating the player's scoreboard.
 * It uses a BukkitRunnable to schedule repeated updates of the scoreboard at fixed intervals.
 */
@RequiredArgsConstructor
public class ScoreboardUpdater {

     private final @NotNull ScoreboardModule scoreboardModule;
     private final @NotNull ScoreboardController scoreboardController;

    public void startUpdating(GamePlayer gamePlayer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboardController.updateScoreboard(gamePlayer);
            }
        }.runTaskTimer(scoreboardModule.getHotPotato(), 0, 20L);
    }
}
