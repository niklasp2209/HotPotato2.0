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

/**
 * Represents the lobby countdown logic for the game.
 * Handles transitioning from the lobby phase to the in-game phase
 * and manages idle behavior when there aren't enough players.
 */
@Getter
@Setter
public class LobbyTask extends Countdown {

    private final GameModule gameModule;

    private int idleId;
    private boolean isIdling;
    private boolean isRunning;

    private final int COUNT_DURATION = 60;
    private final int IDLE_TIME = 15;

    /**
     * Constructor to initialize the LobbyTask with its associated GameModule.
     * Automatically starts idle mode upon creation.
     *
     * @param gameModule The game module this task belongs to.
     */
    public LobbyTask(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;
        startIdle();
    }

    /**
     * Starts the countdown task for the lobby phase.
     * Sends periodic messages and transitions to the in-game state when the countdown ends.
     */
    @Override
    public void start() {
        stopIdle();

        this.seconds = COUNT_DURATION;
        this.isRunning = true;

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), () -> {
            switch (seconds) {
                case 60, 30, 15, 10, 5, 4, 3, 2:
                    sendRemainingTimeMessage();
                    if (seconds == 5) {
                        Bukkit.getPluginManager().callEvent(new VotingFinishedEvent(
                                this.gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class).get()
                                        .getVoting().getVotingWinner()));
                    }
                    break;

                case 1:
                    sendRemainingTimeMessage(true);
                    break;

                case 0:
                    gameModule.setCurrentState(new IngameState(gameModule));
                    break;

                default:
                    break;
            }
            seconds--;
        }, 0L, 20L);
    }

    /**
     * Sends a message to all players about the remaining time.
     */
    private void sendRemainingTimeMessage() {
        sendRemainingTimeMessage(false);
    }

    /**
     * Sends a message to all players about the remaining time.
     * Uses a specific message format for the last second.
     *
     * @param isLastSecond Whether the message is for the last second.
     */
    private void sendRemainingTimeMessage(boolean isLastSecond) {
        String messageKey = isLastSecond ? "lobbytask_remaining1" : "lobbytask_remaining";
        String message = MessageUtil.getMessage(messageKey, String.valueOf(seconds));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    /**
     * Stops the countdown task and resets its state.
     */
    @Override
    public void stop() {
        if (this.isRunning) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.isRunning = false;
            this.seconds = COUNT_DURATION;
        }
    }

    /**
     * Starts the idle mode, which sends periodic messages to players.
     * Used when there aren't enough players to start the game.
     */
    public void startIdle() {
        stop();
        this.isIdling = true;

        this.idleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), () -> {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendMessage(MessageUtil.getMessage("idling_message")));
        }, 0L, 20L * IDLE_TIME);
    }

    /**
     * Stops the idle mode and cancels its task.
     */
    public void stopIdle() {
        if (this.isIdling) {
            Bukkit.getScheduler().cancelTask(this.idleId);
            this.isIdling = false;
        }
    }
}