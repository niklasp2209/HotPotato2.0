package de.bukkitnews.hotpotato.module.player.model;

import de.bukkitnews.hotpotato.module.stats.model.PlayerStats;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a player with a UUID and dynamic player data stored in a key-value map.
 */
@Getter
@Setter
public class GamePlayer {

    private final @NotNull String uuid;
    private PlayerStats stats;
    private boolean alive;
    private boolean isPotato;

    public GamePlayer(@NotNull String uuid) {
        this.uuid = uuid;
        this.alive = true;
        this.isPotato = false;
        this.stats = new PlayerStats(0, 0, 0);
    }

    public void eliminatePlayer() {
        if (!isPotato) {
            return;
        }

        setAlive(false);
        setPotato(false);
        Player player = Bukkit.getPlayer(UUID.fromString(getUuid()));
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
    }
}