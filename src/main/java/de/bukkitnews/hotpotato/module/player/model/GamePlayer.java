package de.bukkitnews.hotpotato.module.player.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in the game, uniquely identified by a UUID.
 * This class encapsulates player-specific data, providing methods to store, retrieve,
 * and manipulate this information dynamically.
 *
 * <p>The player's data is stored in a flexible key-value mapping using a Map.
 * Each key is a String representing the name of the data field, while the value
 * can be any type (Object), allowing the storage of diverse player-related data.
 *
 * <p>This class is designed to integrate seamlessly with other systems such as
 * caching or persistent storage for efficient player management.
 */
@Getter
public class GamePlayer {

    /**
     * The unique identifier (UUID) of the player.
     */
    private final String uuid;

    /**
     * A map to store the player's data as key-value pairs.
     * Keys are strings representing data fields (e.g., "wins"), and values can be of any type.
     */
    private final Map<String, Object> data;

    /**
     * Constructs a new GamePlayer instance associated with a unique UUID.
     *
     * @param uuid the unique identifier of the player
     */
    public GamePlayer(String uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
    }

    /**
     * Stores or updates a specific piece of data for the player.
     *
     * @param key   the unique key associated with the data (e.g., "wins", "name")
     * @param value the value to store under the given key
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Retrieves the value associated with a given key from the player's data.
     *
     * @param key the unique key of the data to retrieve
     * @return the value stored under the given key, or {@code null} if the key does not exist
     */
    public Object getData(String key) {
        return data.get(key);
    }

    /**
     * Increments the player's win count by 1.
     * Assumes that the "wins" field is an integer.
     *
     * <p>If the "wins" field is missing or not an integer, this method may throw a
     * {@link ClassCastException} or a {@link NullPointerException}.
     */
    public void increaseWins() {
        int currentWins = (int) getData("wins");
        setData("wins", currentWins + 1);
    }

    /**
     * Increments the player's games played count by 1.
     * Assumes that the "gamesPlayed" field is an integer.
     *
     * <p>If the "gamesPlayed" field is missing or not an integer, this method may throw a
     * {@link ClassCastException} or a {@link NullPointerException}.
     */
    public void increaseGamesPlayed() {
        int currentGames = (int) getData("gamesPlayed");
        setData("gamesPlayed", currentGames + 1);
    }

    /**
     * Increases the player's total playtime by a specified amount.
     * Assumes that the "playtime" field is a long.
     *
     * @param amount the amount of playtime to add, in the appropriate units (e.g., milliseconds or seconds)
     * @throws ClassCastException if the "playtime" field is not a long
     * @throws NullPointerException if the "playtime" field is missing
     */
    public void increasePlaytime(long amount) {
        long currentPlaytime = (long) getData("playtime");
        setData("playtime", currentPlaytime + amount);
    }
}