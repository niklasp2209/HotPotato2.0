package de.bukkitnews.hotpotato.module.game.gamestate.lobby.task;

import de.bukkitnews.hotpotato.module.arena.events.VotingFinishedEvent;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.task.Countdown;
import de.bukkitnews.hotpotato.utils.MessageUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter @Setter
public class LobbyTask extends Countdown {

    private final GameModule gameModule;
    private int idleId;
    private boolean isIdling;
    private boolean isRunning;
    private final int COUNT_DURATION = 60;
    private final int IDLE_TIME = 15;

    public LobbyTask(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;

        startIdle();
    }

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
                        Bukkit.getPluginManager().callEvent(new VotingFinishedEvent());
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

    private void sendRemainingTimeMessage() {
        sendRemainingTimeMessage(false);
    }

    private void sendRemainingTimeMessage(boolean isLastSecond) {
        String messageKey = isLastSecond ? "lobbytask_remaining1" : "lobbytask_remaining";
        String message = MessageUtil.getMessage(messageKey, String.valueOf(seconds));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    @Override
    public void stop() {
        if(this.isRunning){
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.isRunning = false;
            this.seconds = COUNT_DURATION;
        }
    }

    public void startIdle(){
        stop();
        this.isIdling = true;

        this.idleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player ->
                        player.sendMessage(MessageUtil.getMessage("idling_message")));
            }
        }, 0L, 20L * IDLE_TIME);
    }

    public void stopIdle(){
        if(this.isIdling){
            Bukkit.getScheduler().cancelTask(this.idleId);
            this.isIdling = false;
        }
    }
}
