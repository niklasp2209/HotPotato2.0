package de.bukkitnews.hotpotato.module.game.gamestate.ingame.task;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.task.Countdown;
import de.bukkitnews.hotpotato.module.packet.PacketUtil;
import lombok.NonNull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IngameTask extends Countdown {

    private final GameModule gameModule;
    private final int COUNT_DURATION = 25;

    public IngameTask(@NonNull GameModule gameModule) {
        this.gameModule = gameModule;

        start();
    }

    @Override
    public void start() {
        this.seconds = COUNT_DURATION;

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.gameModule.getHotPotato(), () -> {

            if(gameModule.getPotato() != null){
                Location location = gameModule.getPotato().getLocation();
                ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                        ParticleTypes.EXPLOSION,true, location.getX(), location.getY(), location.getZ(),
                        0,0,0,0,0);

                for(Player player : Bukkit.getOnlinePlayers()){
                    PacketUtil.sendPacket(player, packet);
                }
            }

            switch (seconds){
                case 0: {
                    gameModule.eliminatePlayer();
                }
            }

            seconds--;

        }, 0L, 20L);
    }

    @Override
    public void stop() {

    }
}
