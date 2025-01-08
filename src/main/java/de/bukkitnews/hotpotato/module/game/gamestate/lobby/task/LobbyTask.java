package de.bukkitnews.hotpotato.module.game.gamestate.lobby.task;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.events.VotingFinishedEvent;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.task.Countdown;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents the lobby countdown logic for the game.
 * Handles transitioning from the lobby phase to the in-game phase
 * and manages idle behavior when there aren't enough players.
 */
@Getter @Setter
public class LobbyTask extends Countdown {

    @NonNull private final GameModule gameModule;
    private int idleId;
    private boolean isIdling;

    public LobbyTask(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;
        setInitialDuration(60);
        startIdle();
    }

    /**
     * Handles each tick of the countdown.
     * If idle mode is active, it stops the idle task.
     * Triggers a voting event when the countdown reaches 5 seconds.
     */
    @Override
    protected void onTick() {
        if (this.isIdling) {
            stopIdle();
        }

        super.onTick();

        if (this.seconds == 5) {
            Bukkit.getPluginManager().callEvent(new VotingFinishedEvent(
                    this.gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class).get()
                            .getVoting().getVotingWinner()));
        }
    }

    /**
     * Handles actions when the countdown finishes.
     * Transitions the game to the in-game state when the countdown reaches 0.
     */
    @Override
    protected void onFinish() {
        this.gameModule.setCurrentState(new IngameState(this.gameModule));
    }

    /**
     * Returns the message to be displayed to players based on the remaining time.
     * Displays different messages depending on the time remaining.
     *
     * @return The message to display to players.
     */
    @Override
    protected String getRemainingTimeMessage() {
        String messageKey = (this.seconds == 1) ? "lobbytask_remaining1" : "lobbytask_remaining";
        return MessageUtil.getMessage(messageKey, String.valueOf(this.seconds));
    }

    /**
     * Determines whether a message about the remaining time should be sent.
     * Sends messages at specific time intervals: 60, 30, 15, 10, and every second after 10.
     *
     * @param seconds The remaining seconds of the countdown.
     * @return True if a message should be sent; otherwise, false.
     */
    @Override
    protected boolean shouldSendRemainingTime(int seconds) {
        return seconds == 60 || seconds == 30 || seconds == 15 || seconds <= 10;
    }

    @Override
    protected Plugin getPlugin() {
        return this.gameModule.getHotPotato();
    }

    /**
     * Starts the idle mode.
     * Sends idle messages to players when there aren't enough players to start the game.
     */
    public void startIdle() {
        stop();
        this.isIdling = true;

        this.idleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), () -> {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendMessage(MessageUtil.getMessage("idling_message"))
            );
        }, 0L, 20L * 15);
    }

    /**
     * Stops the idle mode.
     * Cancels the idle message task and resets the idle state.
     */
    public void stopIdle() {
        if (this.isIdling) {
            Bukkit.getScheduler().cancelTask(this.idleId);
            this.isIdling = false;
        }
    }
}