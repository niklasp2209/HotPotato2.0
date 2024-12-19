package de.bukkitnews.hotpotato.module.game.gamestate.task;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@Getter @Setter
public abstract class Countdown {

    /**
     * The task ID associated with the scheduled countdown.
     * Used to manage and cancel the task in the Bukkit scheduler.
     */
    protected int taskId;

    /**
     * The number of seconds remaining in the countdown.
     */
    protected int seconds;

    /**
     * The initial duration of the countdown in seconds.
     * This value resets the countdown timer when stopped or restarted.
     */
    protected int initialDuration;

    /**
     * Indicates whether the countdown is currently running.
     */
    protected boolean isRunning;

    /**
     * Constructor for the Countdown class.
     * Sets the default initial duration to 60 seconds.
     */
    public Countdown() {
        this.initialDuration = 60;
    }

    /**
     * Starts the countdown.
     * Schedules a repeating task in the Bukkit scheduler and initializes the timer.
     */
    public void start() {
        this.isRunning = true;
        this.seconds = initialDuration;
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                getPlugin(),
                this::onTick,
                0L,
                20L
        );
    }

    /**
     * Stops the countdown.
     * Cancels the scheduled task in the Bukkit scheduler and resets the timer state.
     */
    public void stop() {
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.taskId);
        this.seconds = initialDuration;
    }

    /**
     * Executes each tick of the countdown.
     * Decrements the timer and triggers appropriate actions when the timer ends
     * or when specific time intervals are reached.
     */
    protected void onTick() {
        if (seconds <= 0) {
            onFinish();
        } else {
            if (shouldSendRemainingTime(seconds)) {
                sendRemainingTimeMessage();
            }
            seconds--;
        }
    }

    /**
     * Sends a message to all players about the remaining time.
     * This method uses the message defined in the subclass implementation.
     */
    protected void sendRemainingTimeMessage() {
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(getRemainingTimeMessage())
        );
    }

    /**
     * Retrieves the message to display to players about the remaining time.
     * Subclasses must implement this to provide the specific message.
     *
     * @return The message to send to players.
     */
    protected abstract String getRemainingTimeMessage();

    /**
     * Determines whether a message about the remaining time should be sent.
     * Subclasses must define the logic for deciding at which intervals messages should be displayed.
     *
     * @param seconds The number of seconds remaining.
     * @return True if a message should be sent; otherwise, false.
     */
    protected abstract boolean shouldSendRemainingTime(int seconds);

    /**
     * Executes when the countdown reaches zero.
     * Subclasses must implement this to define what happens when the countdown ends.
     */
    protected abstract void onFinish();

    /**
     * Retrieves the plugin instance required for scheduling tasks.
     * Subclasses must provide their specific plugin instance.
     *
     * @return The plugin instance.
     */
    protected abstract Plugin getPlugin();
}