package de.bukkitnews.hotpotato.module.packet;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class PacketUtil {

    public static void sendPacket(@NonNull Player player, @NonNull Object packet){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send((Packet<?>) packet);
    }
}
