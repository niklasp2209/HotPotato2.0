package de.bukkitnews.hotpotato.module.arena.model;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Represents an arena in the game.
 * The arena has properties like name, spawn location, min/max players, and votes.
 */
@Getter @Setter
public class Arena {

    private final String name;
    private int minPlayers;
    private int maxPlayers;
    private Optional<Location> spawnLocation = Optional.empty();
    /**
     * -- GETTER --
     *  Gets the current vote count for this arena.
     *
     * @return the current vote count.
     */
    @Getter
    private int votes;

    /**
     * Constructs a new Arena with the specified name.
     * The arena configuration is loaded from the configuration file.
     *
     * @param name The name of the arena.
     */
    public Arena(@NonNull String name){
        this.name = name;
        load();
    }

    /**
     * Loads the arena configuration from the arena module configuration file.
     */
    private void load() {
        String basePath = ".Arenas." + name;

        this.spawnLocation = Optional.ofNullable(ArenaModule.instance
                .getArenaConfig()
                .getConfig()
                .getLocation(basePath + ".Spawn"));

        this.minPlayers = ArenaModule.instance
                .getArenaConfig()
                .getConfig()
                .getInt(basePath + ".MinPlayers", 0);

        this.maxPlayers = ArenaModule.instance
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
        return spawnLocation.isPresent() && minPlayers > 0 && maxPlayers > 0 && !name.isEmpty();
    }

    /**
     * Checks if an arena with the given name already exists in the configuration.
     *
     * @return true if the arena exists; false otherwise.
     */
    public boolean alreadyExists() {
        return Optional.ofNullable(ArenaModule.instance
                .getArenaConfig()
                .getConfig()
                .getString(".Arenas." + name)).isPresent();
    }

    /**
     * Teleports all online players to the arena's spawn location if it exists.
     */
    public void teleportPlayers() {
        spawnLocation.ifPresent(location ->
                Bukkit.getOnlinePlayers().forEach(player -> player.teleport(location)));
    }

    /**
     * Increments the vote count for this arena.
     */
    public void addVote() {
        this.votes++;
    }

    /**
     * Decrements the vote count for this arena.
     */
    public void removeVote() {
        this.votes--;
    }

    /**
     * Retrieves the spawn location of the arena.
     * If the spawn location is not set, it throws an exception.
     *
     * @return The spawn location.
     * @throws IllegalStateException If the spawn location is not present.
     */
    public Location getSpawnLocationOrThrow() {
        return spawnLocation.orElseThrow(() -> new IllegalStateException("Spawn location not set for arena: " + name));
    }
}