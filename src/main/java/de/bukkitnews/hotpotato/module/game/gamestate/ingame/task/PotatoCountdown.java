package de.bukkitnews.hotpotato.module.game.gamestate.ingame.task;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.game.gamestate.task.Countdown;
import de.bukkitnews.hotpotato.module.game.util.GameUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Represents the countdown for the Hot Potato game.
 * This countdown handles the game mechanics such as selecting the Hot Potato player,
 * setting up visual effects, managing the countdown timer, and updating player states.
 */
@Getter @Setter
public class PotatoCountdown extends Countdown {

    private int seconds = 20;
    private Player potato;
    @NonNull private final HotPotato hotPotato;

    /**
     * Constructor for PotatoCountdown.
     * Initializes the HotPotato instance and EndingCountdown.
     *
     * @param hotPotato The HotPotato instance to interact with.
     */
    public PotatoCountdown(@NonNull HotPotato hotPotato) {
        this.hotPotato = hotPotato;
    }

    /**
     * Starts the countdown task.
     * This method schedules a repeating task that ticks every second (20 ticks) to handle countdown actions.
     */
    @Override
    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.hotPotato, () -> {
            switch (seconds) {
                case 20:
                    selectPotato();
                    break;

                case 0:
                    for (Player current : Bukkit.getOnlinePlayers()) {
                        current.playSound(potato.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                    }

                    potato = null;
                    seconds = 23;
                    break;

                default:
                    break;
            }

            if (potato != null) {
                GameUtil.triggerFireworkAndExplosionEffects(potato);
            }

            GameUtil.updatePlayerLevels((List<Player>) Bukkit.getOnlinePlayers(), seconds);

            if (potato != null) {
                GameUtil.setHotPotatoArmor(potato);
            }

            seconds--;
        }, 0, 20);
    }

    /**
     * Selects the Hot Potato player randomly from the player list.
     * This is called when the countdown starts.
     */
    private void selectPotato() {
        /*
        List<Player> playerList = PotatoConstants.playerList;
        GameUtil.selectRandomPlayer(playerList).ifPresent(selectedPotato -> {
            this.potato = selectedPotato;
        });

         */
    }

    /**
     * Stops the countdown task.
     * Cancels the repeating task when the game round ends or when stopping the countdown manually.
     */
    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    @Override
    protected String getRemainingTimeMessage() {
        return null;
    }

    @Override
    protected boolean shouldSendRemainingTime(int seconds) {
        return false;
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected Plugin getPlugin() {
        return null;
    }

}
