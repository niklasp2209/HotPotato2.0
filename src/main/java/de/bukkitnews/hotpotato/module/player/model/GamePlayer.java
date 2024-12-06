package de.bukkitnews.hotpotato.module.player.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in the game with a unique identifier (UUID) and associated player data.
 * This class provides methods to store, retrieve, and manage custom data related to the player.
 * It allows for the flexible storage of various types of player-specific information.
 *
 * The data is stored in a Map where the key is a String and the value can be any Object,
 * allowing for dynamic and diverse types of data to be stored.
 *
 * The 'saveData' method is currently a placeholder for future implementation of a mechanism
 * to persist the player's data.
 */
@Getter
public class GamePlayer {

    private final String uuid;  // The unique identifier (UUID) of the player
    private final Map<String, Object> data;  // A map to store key-value pairs of player data

    /**
     * Constructor to initialize a GamePlayer instance with a unique UUID.
     *
     * @param uuid The unique identifier of the player
     */
    public GamePlayer(String uuid){
        this.uuid = uuid;
        this.data = new HashMap<>();
    }

    /**
     * Adds or updates a piece of player data with a specified key and value.
     *
     * @param key The key associated with the data
     * @param value The value to store for the given key
     */
    public void setData(String key, Object value){
        data.put(key, value);
    }

    /**
     * Retrieves a piece of player data based on the given key.
     *
     * @param key The key associated with the data
     * @return The value stored for the specified key, or null if not found
     */
    public Object getData(String key){
        return data.get(key);
    }

    /**
     * Placeholder method to save the player’s data. Currently not implemented.
     * In a future version, this method may include logic to persist data to a database
     * or other storage solutions.
     */
    public void saveData(){
        // Method for saving player data (currently not implemented)
    }
}