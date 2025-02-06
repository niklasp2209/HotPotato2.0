package de.bukkitnews.hotpotato.module.game.gamestate.ingame.countdown;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.countdown.Countdown;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.util.PacketUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents the in-game task that controls the countdown and particle effects during gameplay.
 * This task is responsible for sending particle effects and eliminating players once the countdown reaches zero.
 */
public class IngameCountdown extends Countdown {

    private final @NotNull GameModule gameModule;

    public IngameCountdown(@NotNull GameModule gameModule) {
        this.gameModule = gameModule;
        this.initialDuration = 25;
        start();
    }

    /**
     * This is the method that will be called every tick (20 times per second).
     * It will handle the particle effects and eliminate players when the countdown reaches zero.
     */
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
                .map(player -> gameModule.getHotPotato().getModuleManager()
                        .getModule(PlayerModule.class)
                        .flatMap(playerModule -> Optional.ofNullable(playerModule.getGamePlayerManager()
                                .getPlayerCache().get(player.getUniqueId().toString())))
                )
                .filter(optionalGamePlayer -> optionalGamePlayer.isPresent() && optionalGamePlayer.get().isPotato())
                .map(Optional::get)
                .forEach(gamePlayer -> {
                    Player potato = Bukkit.getPlayer(UUID.fromString(gamePlayer.getUuid()));
                    if (potato != null) {
                        Location location = potato.getLocation();
                        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                                ParticleTypes.EXPLOSION, true, location.getX(), location.getY(), location.getZ(),
                                0, 0, 0, 0, 0
                        );
                        Bukkit.getOnlinePlayers().forEach(player -> PacketUtil.sendPacket(player, packet));
                    }
                });

        if (seconds == 0) {
            gameModule.eliminatePlayer();
        }

        seconds--;
    }


    @Override
    protected @NotNull String getRemainingTimeMessage() {
        return null;
    }

    @Override
    protected boolean shouldSendRemainingTime(int seconds) {
        return false;
    }

    @Override
    protected @NotNull Plugin getPlugin() {
        return gameModule.getHotPotato();
    }
}
