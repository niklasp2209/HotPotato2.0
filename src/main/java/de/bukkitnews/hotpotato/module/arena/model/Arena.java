package de.bukkitnews.hotpotato.module.arena.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
public class Arena {

    private final String name;
    private Location spawnLocation;
    private Location spectatorLocation;
    @Setter private int votes;

    public Arena(String name){
        this.name = name.toUpperCase();
    }


    public void create(String builder) {

    }


    public void load() {

    }

    public void setSpawnLocation(){

    }

    public void setSpectatorLocation(){

    }

    public boolean playable() {
        return spawnLocation != null && spectatorLocation != null;
    }
}
