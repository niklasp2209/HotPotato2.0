package de.bukkitnews.hotpotato.module.game.gamestate.ingame.countdown;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.game.gamestate.countdown.Countdown;
import de.bukkitnews.hotpotato.module.game.util.GameUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Represents the countdown for the Hot Potato game.
 * This countdown handles the game mechanics such as selecting the Hot Potato player,
 * setting up visual effects, managing the countdown timer, and updating player states.
 */
@Getter
@Setter
public class PotatoCountdown extends Countdown {

    private final @NotNull HotPotato hotPotato;
    private int seconds = 20;
    private Player potato;

    public PotatoCountdown(@NotNull HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.initialDuration = seconds;
    }

    /**
     * Starts the countdown task.
     * This method schedules a repeating task that ticks every second (20 ticks) to handle countdown actions.
     */
    @Override
    public void start() {
        this.seconds = initialDuration;

        Bukkit.getScheduler().runTaskTimer(hotPotato, this, 0, 20);
    }

    /**
     * Executes the countdown logic every tick (every second).
     * It handles selecting the Hot Potato player, playing sounds, triggering effects, etc.
     */
    @Override
    public void run() {
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
    }

    /**
     * Selects the Hot Potato player randomly from the player list.
     * This is called when the countdown starts.
     */
    private void selectPotato() {
        List<Player> playerList = (List<Player>) Bukkit.getOnlinePlayers();
        Optional<Player> selectedPotato = GameUtil.selectRandomPlayer(playerList);
        selectedPotato.ifPresent(selected -> potato = selected);
    }

    /**
     * Stops the countdown task.
     * Since there's no taskId anymore, we only stop the countdown by setting the flag.
     */
    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    protected @NotNull String getRemainingTimeMessage() {
        return null;
    }

    @Override
    protected boolean shouldSendRemainingTime(int seconds) {
        return false;
    }

    @Override
    protected @NotNull Plugin getPlugin() {
        return hotPotato;
    }

}