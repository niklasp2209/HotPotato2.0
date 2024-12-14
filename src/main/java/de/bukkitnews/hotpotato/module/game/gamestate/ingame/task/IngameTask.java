package de.bukkitnews.hotpotato.module.game.gamestate.ingame.task;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.task.Countdown;
import de.bukkitnews.hotpotato.util.PacketUtil;
import lombok.NonNull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents the in-game task that controls the countdown and particle effects during gameplay.
 * This task is responsible for sending particle effects and eliminating players once the countdown reaches zero.
 */
public class IngameTask extends Countdown {

    private final GameModule gameModule;
    private final int COUNT_DURATION = 25;

    /**
     * Constructor to initialize the in-game task with the provided game module.
     * This starts the countdown immediately upon instantiation.
     *
     * @param gameModule The game module this task belongs to.
     */
    public IngameTask(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;
        start();
    }

    /**
     * Starts the countdown and handles sending particle effects to players.
     * This method also eliminates the player holding the potato when the countdown reaches zero.
     */
    @Override
    public void start() {
        this.seconds = COUNT_DURATION;

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), () -> {
            if (gameModule.getPotato().isPresent()) {
                Location location = gameModule.getPotato().get().getLocation();
                ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                        ParticleTypes.EXPLOSION, true, location.getX(), location.getY(), location.getZ(),
                        0, 0, 0, 0, 0);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    PacketUtil.sendPacket(player, packet);
                }
            }

            if (seconds == 0) {
                gameModule.eliminatePlayer();
            }

            seconds--;
        }, 0L, 20L);
    }

    /**
     * Stops the countdown. Currently, this method does not implement any specific behavior.
     * If needed, additional behavior can be added here to handle stopping the task.
     */
    @Override
    public void stop() {

    }
}