package de.bukkitnews.hotpotato.module.arena.model;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.Optional;

@Getter @Setter
public class Arena {

    private final String name;
    private int minPlayers;
    private int maxPlayers;
    private Optional<Location> spawnLocation = Optional.empty();
    private int votes;

    public Arena(String name){
        this.name = name;

        load();
    }

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

    public boolean isPlayable() {
        return spawnLocation.isPresent()
                && minPlayers > 0
                && maxPlayers > 0
                && name != null && !name.isEmpty();
    }
}
