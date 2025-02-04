package de.bukkitnews.hotpotato.module.game.gamestate.lobby;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.events.VotingFinishedEvent;
import de.bukkitnews.hotpotato.module.arena.voting.Voting;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.countdown.Countdown;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class LobbyCountdown extends Countdown {

    private final @NotNull GameModule gameModule;
    private int idleId;
    private boolean isIdling;

    public LobbyCountdown(@NotNull GameModule gameModule) {
        this.gameModule = gameModule;
        setInitialDuration(60);
        startIdle();
    }

    /**
     * Handles each tick of the countdown.
     * If idle mode is active, it stops the idle task.
     * Triggers a voting event when the countdown reaches 5 seconds.
     */
    protected void onTick() {
        if (isIdling) {
            stopIdle();
        }

        if (seconds == 5) {
            gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class)
                    .map(ArenaModule::getVoting)
                    .map(Voting::getVotingWinner)
                    .ifPresent(votingWinner -> Bukkit.getPluginManager().callEvent(new VotingFinishedEvent(votingWinner)));
        }
    }


    /**
     * Handles actions when the countdown finishes.
     * Transitions the game to the in-game state when the countdown reaches 0.
     */
    protected void onFinish() {
        gameModule.setCurrentState(new IngameState(gameModule));
    }

    /**
     * Returns the message to be displayed to players based on the remaining time.
     * Displays different messages depending on the time remaining.
     *
     * @return The message to display to players.
     */
    @Override
    protected @NotNull String getRemainingTimeMessage() {
        String messageKey = (seconds == 1) ? "lobbytask_remaining1" : "lobbytask_remaining";
        return MessageUtil.getMessage(messageKey, String.valueOf(seconds));
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
    protected @NotNull Plugin getPlugin() {
        return gameModule.getHotPotato();
    }

    /**
     * Starts the idle mode.
     * Sends idle messages to players when there aren't enough players to start the game.
     */
    public void startIdle() {
        stop();
        this.isIdling = true;

        this.idleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameModule.getHotPotato(), () ->
                Bukkit.getOnlinePlayers().forEach(player ->
                        player.sendMessage(MessageUtil.getMessage("idling_message"))
                ), 0L, 20L * 15);
    }

    /**
     * Stops the idle mode.
     * Cancels the idle message task and resets the idle state.
     */
    public void stopIdle() {
        if (isIdling) {
            Bukkit.getScheduler().cancelTask(idleId);
            this.isIdling = false;
        }
    }

    /**
     * Starts the countdown task using Runnable.
     */
    @Override
    public void start() {
        this.isRunning = true;
        this.seconds = initialDuration;

        Bukkit.getScheduler().runTaskTimer(getPlugin(), this::onTick, 0L, 20L);
    }

    /**
     * Stops the countdown.
     * Cancels the countdown task and resets the timer state.
     */
    @Override
    public void stop() {
        this.isRunning = false;
        this.seconds = initialDuration;
    }
}