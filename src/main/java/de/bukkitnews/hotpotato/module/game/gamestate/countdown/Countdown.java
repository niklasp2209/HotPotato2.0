package de.bukkitnews.hotpotato.module.game.gamestate.countdown;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
@Setter
public abstract class Countdown implements Runnable {

    protected int seconds;
    protected int initialDuration;
    protected boolean isRunning;

    public Countdown() {
        this.initialDuration = 60;
    }

    /**
     * Starts the countdown.
     * Schedules a repeating task using Bukkit's scheduler and initializes the timer.
     */
    public void start() {
        this.isRunning = true;
        this.seconds = initialDuration;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(getPlugin(), this, 0L, 20L);
    }

    /**
     * Stops the countdown.
     * Cancels the scheduled task and resets the timer state.
     */
    public void stop() {
        this.isRunning = false;
        this.seconds = this.initialDuration;
    }

    /**
     * Executes each tick of the countdown.
     * Decrements the timer and triggers appropriate actions when the timer ends
     * or when specific time intervals are reached.
     */
    @Override
    public void run() {
        if (seconds <= 0) {
            return;
        }

        if (shouldSendRemainingTime(seconds)) {
            sendRemainingTimeMessage();
        }
        seconds--;
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

    protected abstract Plugin getPlugin();
}