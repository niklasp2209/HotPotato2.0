package de.bukkitnews.hotpotato.util;

import lombok.experimental.UtilityClass;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for sending packets to players.
 * This class handles the sending of Minecraft packets to players.
 */
@UtilityClass
public class PacketUtil {

    /**
     * Sends a packet to a specified player.
     *
     * @param player The player to whom the packet will be sent.
     * @param packet The packet to be sent.
     */
    public static void sendPacket(@NotNull Player player, @NotNull Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        if (craftPlayer.getHandle() != null && craftPlayer.getHandle().connection != null) {
            craftPlayer.getHandle().connection.send(packet);
        }
    }
}