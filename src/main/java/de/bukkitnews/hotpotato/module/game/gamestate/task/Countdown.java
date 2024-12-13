package de.bukkitnews.hotpotato.module.game.gamestate.task;

public abstract class Countdown {

    protected int taskId;
    protected int seconds;

    public abstract void start();
    public abstract void stop();
}
