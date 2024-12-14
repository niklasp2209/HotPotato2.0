package de.bukkitnews.hotpotato.module.player.handler;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handler for managing the playtime of players.
 * This class runs periodically and increases the playtime for each player in the game.
 */
public class PlaytimeHandler extends BukkitRunnable {

    private final PlayerModule playerModule;

    /**
     * Constructs a PlaytimeHandler for the given PlayerModule.
     *
     * @param playerModule The PlayerModule to interact with.
     */
    public PlaytimeHandler(@NonNull PlayerModule playerModule) {
        this.playerModule = playerModule;
    }

    /**
     * Periodically increases the playtime for each player in the game.
     * This method is executed every time the task runs.
     */
    @Override
    public void run() {
        this.playerModule.getGamePlayerManager().getPlayerCache().values().forEach(this::increasePlaytimeForPlayer);
    }

    /**
     * Starts the playtime handler, which will run periodically to update playtime.
     * The task runs every 60 seconds (1200 ticks).
     */
    public void startHandler() {
        this.runTaskTimer(this.playerModule.getHotPotato(), 0L, 1200L);
    }

    /**
     * Increases the playtime for a specific player by 1.
     *
     * @param gamePlayer The player whose playtime will be increased.
     */
    private void increasePlaytimeForPlayer(@NonNull GamePlayer gamePlayer) {
        gamePlayer.increasePlaytime(1L);
    }
}