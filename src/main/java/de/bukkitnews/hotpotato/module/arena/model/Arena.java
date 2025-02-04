package de.bukkitnews.hotpotato.module.arena.model;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an arena in the game.
 * The arena has properties like name, spawn location, min/max players, and votes.
 */
@Getter
@Setter
public class Arena {

    private final @NotNull String name;
    private final @NotNull ArenaModule arenaModule;
    private @NotNull Optional<Location> spawnLocation = Optional.empty();
    private final @NotNull Set<UUID> votedPlayers = new HashSet<>();

    private int minPlayers;
    private int maxPlayers;
    private int votes;

    public Arena(@NotNull String name, @NotNull ArenaModule arenaModule) {
        this.name = name;
        this.arenaModule = arenaModule;
        this.votes = 0;
        load();
    }


    /**
     * Loads the arena configuration from the arena module configuration file.
     */
    private void load() {
        String basePath = ".Arenas." + name;

        this.spawnLocation = Optional.ofNullable(arenaModule
                .getArenaConfig()
                .getConfig()
                .getLocation(basePath + ".Spawn"));

        this.minPlayers = arenaModule
                .getArenaConfig()
                .getConfig()
                .getInt(basePath + ".MinPlayers", 0);

        this.maxPlayers = arenaModule
                .getArenaConfig()
                .getConfig()
                .getInt(basePath + ".MaxPlayers", 0);
    }

    /**
     * Checks if the arena is playable based on its configuration.
     *
     * @return true if the arena has a spawn location and valid player count limits; false otherwise.
     */
    public boolean isPlayable() {
        return spawnLocation.isPresent()
                && minPlayers > 0
                && maxPlayers > 0
                && !name.isEmpty();
    }

    /**
     * Checks if an arena with the given name already exists in the configuration.
     *
     * @return true if the arena exists; false otherwise.
     */
    public boolean alreadyExists() {
        return Optional.ofNullable(arenaModule
                .getArenaConfig()
                .getConfig()
                .getString(".Arenas." + name)).isPresent();
    }

    /**
     * Teleports all online players to the arena's spawn location if it exists.
     */
    public void teleportPlayers() {
        spawnLocation.ifPresent(location -> Bukkit.getOnlinePlayers().forEach(player -> player.teleport(location)));
    }

    public boolean addVote(@NotNull UUID playerUUID) {
        if (votedPlayers.contains(playerUUID)) {
            return false;
        }
        votedPlayers.add(playerUUID);
        votes++;
        return true;
    }

}