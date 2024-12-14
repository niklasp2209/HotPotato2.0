package de.bukkitnews.hotpotato.module.game.gamestate.task;

/**
 * Abstract Countdown class representing a countdown timer.
 * Subclasses must define the behavior for starting and stopping the countdown.
 */
public abstract class Countdown {

    /**
     * The task ID associated with the countdown.
     * This can be used to manage the scheduled task in Bukkit/Spigot.
     */
    protected int taskId;

    /**
     * The number of seconds remaining in the countdown.
     */
    protected int seconds;

    /**
     * Starts the countdown timer.
     * Subclasses should implement the logic for scheduling and running the countdown.
     */
    public abstract void start();

    /**
     * Stops the countdown timer.
     * Subclasses should implement the logic for canceling the scheduled task
     * and resetting any relevant state.
     */
    public abstract void stop();
}