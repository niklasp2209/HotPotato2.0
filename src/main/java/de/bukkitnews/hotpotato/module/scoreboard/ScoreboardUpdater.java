package de.bukkitnews.hotpotato.module.scoreboard;

import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.scoreboard.model.ScoreboardController;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class ScoreboardUpdater {

    private final ScoreboardModule scoreboardModule;
    private final ScoreboardController scoreboardController;

    public void startUpdating(GamePlayer gamePlayer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboardController.updateScoreboard(gamePlayer);
            }
        }.runTaskTimer(scoreboardModule.getHotPotato(), 0, 20L);
    }
}
