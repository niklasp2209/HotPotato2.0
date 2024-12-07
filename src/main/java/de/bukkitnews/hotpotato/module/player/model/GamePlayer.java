package de.bukkitnews.hotpotato.module.player.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a player with a UUID and dynamic player data stored in a key-value map.
 */
@Getter
public class GamePlayer {

    private final String uuid;
    private final Map<String, Object> data;

    public GamePlayer(String uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
    }

    /**
     * Stores or updates a player's data.
     *
     * @param key   the data key (e.g., "wins")
     * @param value the value to store
     */
    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * Retrieves the player's data for a given key.
     *
     * @param key the data key
     * @return an Optional containing the value, or an empty Optional if not found
     */
    public Optional<Object> getData(String key) {
        return Optional.ofNullable(this.data.get(key));
    }

    /**
     * Increments the player's win count by 1.
     */
    public void increaseWins() {
        int currentWins = getData("wins").map(value -> (int) value).orElse(0);
        setData("wins", currentWins + 1);
    }

    /**
     * Increments the player's games played count by 1.
     */
    public void increaseGamesPlayed() {
        int currentGames = getData("gamesPlayed").map(value -> (int) value).orElse(0);
        setData("gamesPlayed", currentGames + 1);
    }

    /**
     * Increases the player's playtime by a specified amount.
     *
     * @param amount the playtime to add
     */
    public void increasePlaytime(long amount) {
        long currentPlaytime = getData("playtime").map(value -> (long) value).orElse(0L);
        setData("playtime", currentPlaytime + amount);
    }
}