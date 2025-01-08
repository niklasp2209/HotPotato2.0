package de.bukkitnews.hotpotato.module.player.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player with a UUID and dynamic player data stored in a key-value map.
 */
@Getter @Setter
public class GamePlayer {

    @NonNull private final String uuid;
    private final Map<String, Object> data;
    private boolean alive;
    private boolean voted;
    private boolean isPotato;

    public GamePlayer(@NonNull String uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
        this.alive = true;
        this.voted = false;
        this.isPotato = false;
    }

    /**
     * Stores or updates a player's data.
     *
     * @param key   the data key (e.g., "wins")
     * @param value the value to store
     */
    public void setData(@NonNull String key, @NonNull Object value) {
        this.data.put(key, value);
    }

    /**
     * Retrieves the player's data for a given key.
     *
     * @param key the data key
     * @return an Optional containing the value, or an empty Optional if not found
     */
    public Optional<Object> getData(@NonNull String key) {
        return Optional.ofNullable(this.data.get(key));
    }

    public void eliminatePlayer(){
        if(!isPotato){
            return;
        }

        setAlive(false);
        setPotato(false);
        Player player = Bukkit.getPlayer(UUID.fromString(getUuid()));
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
    }

    /**
     * Increments the player's win count by 1.
     */
    public void increaseWins() {
        getData("wins").map(value -> (int) value).ifPresentOrElse(
                currentWins -> setData("wins", currentWins + 1),
                () -> setData("wins", 1)
        );
    }

    /**
     * Increments the player's games played count by 1.
     */
    public void increaseGamesPlayed() {
        getData("gamesPlayed").map(value -> (int) value).ifPresentOrElse(
                currentGames -> setData("gamesPlayed", currentGames + 1),
                () -> setData("gamesPlayed", 1)
        );
    }

    /**
     * Increases the player's playtime by a specified amount.
     * Playtime is in minutes
     *
     * @param amount the playtime to add
     */
    public void increasePlaytime(long amount) {
        getData("playtime").map(value -> (long) value).ifPresentOrElse(
                currentPlaytime -> setData("playtime", currentPlaytime + amount),
                () -> setData("playtime", amount)
        );
    }

    /**
     * Retrieves the number of wins.
     *
     * @return the number of wins, or 0 if not set
     */
    public int getWins() {
        return (int) getData("wins").orElse(0);
    }

    /**
     * Retrieves the number of games played.
     *
     * @return the number of games played, or 0 if not set
     */
    public int getGamesPlayed() {
        return (int) getData("gamesPlayed").orElse(0);
    }

    /**
     * Retrieves the total playtime.
     *
     * @return the total playtime in minutes, or 0 if not set
     */
    public long getPlaytime() {
        return (long) getData("playtime").orElse(0L);
    }
}