package de.bukkitnews.hotpotato.module.player.handler;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for managing the playtime of players.
 * This class runs periodically and increases the playtime for each player in the game.
 */
@RequiredArgsConstructor
public class PlaytimeHandler extends BukkitRunnable {

    private final @NotNull PlayerModule playerModule;

    /**
     * Periodically increases the playtime for each player in the game.
     * This method is executed every time the task runs.
     */
    @Override
    public void run() {
        playerModule.getGamePlayerManager().getPlayerCache().values().forEach(this::increasePlaytimeForPlayer);
    }

    /**
     * Starts the playtime handler, which will run periodically to update playtime.
     * The task runs every 60 seconds (1200 ticks).
     */
    public void startHandler() {
        runTaskTimer(playerModule.getHotPotato(), 0L, 60 * 20L);
    }

    /**
     * Increases the playtime for a specific player by 1.
     *
     * @param gamePlayer The player whose playtime will be increased.
     */
    private void increasePlaytimeForPlayer(@NotNull GamePlayer gamePlayer) {
        gamePlayer.getStats().incrementPlaytime(1L);
    }
}