package de.bukkitnews.hotpotato.module.player.handler;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaytimeHandler extends BukkitRunnable {

    private final PlayerModule playerModule;

    public PlaytimeHandler(@NonNull PlayerModule playerModule) {
        this.playerModule = playerModule;
    }

    @Override
    public void run() {
        PlayerModule.gamePlayerManager.getPlayerCache().values().forEach(this::increasePlaytimeForPlayer);
    }

    public void startHandler(){
        this.runTaskTimer(this.playerModule.getHotPotato(), 0L, 1200L);
    }

    private void increasePlaytimeForPlayer(@NonNull GamePlayer gamePlayer) {
        gamePlayer.increasePlaytime(1L);
    }
}
