package de.bukkitnews.hotpotato.module.player.model;

import de.bukkitnews.hotpotato.database.SQLManager;
import de.bukkitnews.hotpotato.module.stats.model.PlayerStats;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Manages player data by integrating Redis for fast in-memory caching and MySQL for persistent storage.
 * This class provides methods to load, save, and manage player data, with Redis acting as a primary data store
 * and SQL as a fallback for missing or older data. It also maintains an internal cache for active players
 * to optimize performance and minimize database interactions.
 */
public class GamePlayerManager {

    private final @NotNull SQLManager sqlManager;
    private final @NotNull JedisPool jedisPool;
    private final @NotNull Logger logger;
    @Getter
    private final Map<String, GamePlayer> playerCache;

    public GamePlayerManager(@NotNull SQLManager sqlManager, @NotNull JedisPool jedisPool, @NotNull Logger logger) {
        this.sqlManager = sqlManager;
        this.jedisPool = jedisPool;
        this.logger = logger;
        this.playerCache = new ConcurrentHashMap<>();
    }

    /**
     * Loads a player's data asynchronously, attempting to retrieve it from Redis first.
     * If the data is not found in Redis, it falls back to loading it from SQL.
     *
     * @param uuid The UUID of the player.
     */
    public void loadPlayer(@NotNull String uuid) {
        CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                if (jedis.exists(uuid)) {
                    int wins = Integer.parseInt(jedis.hget(uuid, "wins"));
                    long playtime = Long.parseLong(jedis.hget(uuid, "playtime"));
                    int gamesPlayed = Integer.parseInt(jedis.hget(uuid, "gamesPlayed"));

                    PlayerStats stats = new PlayerStats(gamesPlayed, wins, playtime);

                    GamePlayer player = new GamePlayer(uuid);
                    player.setStats(stats);

                    playerCache.put(uuid, player);

                    logger.info("Loaded player " + uuid + " from Redis.");
                    return player;
                }
            } catch (Exception e) {
                logger.severe("Error loading player from Redis: " + e.getMessage());
            }

            return loadPlayerFromSQL(uuid).join();
        });
    }

    /**
     * Saves a player's data asynchronously. The data is first saved to Redis,
     * then scheduled to be saved to SQL for persistent storage. Once saved,
     * the player is removed from the in-memory cache.
     *
     * @param player The GamePlayer instance to save.
     */
    public void savePlayer(@NotNull GamePlayer player) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String uuid = player.getUuid();
                PlayerStats stats = player.getStats();

                jedis.hset(uuid, "wins", String.valueOf(stats.getWins()));
                jedis.hset(uuid, "playtime", String.valueOf(stats.getPlaytime()));
                jedis.hset(uuid, "gamesPlayed", String.valueOf(stats.getGamesPlayed()));

                logger.info("Saved player " + uuid + " to Redis with updated stats.");
            } catch (Exception exception) {
                logger.severe("Error saving player to Redis: " + exception.getMessage());
            }

            savePlayerToSQL(player);
            playerCache.remove(player.getUuid());
        });
    }

    /**
     * Loads a player's data from SQL asynchronously. If the data is successfully retrieved,
     * the player is added to the in-memory cache for future use.
     *
     * @param uuid The UUID of the player.
     * @return A CompletableFuture containing the loaded GamePlayer object, or null if no data is found.
     */
    private @NotNull CompletableFuture<GamePlayer> loadPlayerFromSQL(@NotNull String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = sqlManager.getConnection();
                 var statement = connection.prepareStatement("SELECT * FROM hotpotato WHERE uuid = ?")) {

                statement.setString(1, uuid);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    GamePlayer player = new GamePlayer(uuid);
                    PlayerStats stats = new PlayerStats(
                            resultSet.getInt("gamesPlayed"),
                            resultSet.getInt("wins"),
                            resultSet.getLong("playtime")
                    );

                    player.setStats(stats);

                    logger.info("Loaded player " + uuid + " from SQL.");
                    playerCache.put(uuid, player);
                    return player;
                } else {
                    logger.warning("No data found for player " + uuid + " in SQL.");
                }
            } catch (Exception e) {
                logger.severe("Error loading player from SQL: " + e.getMessage());
            }
            return null;
        });
    }

    /**
     * Saves a player's data to SQL asynchronously. This operation is performed in a
     * separate thread to avoid blocking the main thread.
     *
     * @param player The GamePlayer instance to save.
     */
    private void savePlayerToSQL(@NotNull GamePlayer player) {
        CompletableFuture.runAsync(() -> {
            try (var connection = sqlManager.getConnection();
                 var statement = connection.prepareStatement(
                         "INSERT INTO hotpotato (uuid, wins, playtime, gamesPlayed) VALUES (?, ?, ?, ?) " +
                                 "ON DUPLICATE KEY UPDATE wins = VALUES(wins), " +
                                 "playtime = VALUES(playtime), gamesPlayed = VALUES(gamesPlayed)")) {

                statement.setString(1, player.getUuid());
                PlayerStats stats = player.getStats();
                statement.setInt(2, stats.getWins());
                statement.setLong(3, stats.getPlaytime());
                statement.setInt(4, stats.getGamesPlayed());

                statement.executeUpdate();
                logger.info("Saved player " + player.getUuid() + " to SQL.");
            } catch (Exception e) {
                logger.severe("Error saving player to SQL: " + e.getMessage());
            }
        });
    }

    /**
     * Retrieves a player's data from the in-memory cache, if available.
     *
     * @param uuid The UUID of the player.
     * @return The GamePlayer instance from the cache, or null if not found.
     */
    public @NotNull GamePlayer getCachedPlayer(@NotNull String uuid) {
        return playerCache.get(uuid);
    }

    /**
     * Saves all players currently in the cache and closes the Redis connection pool.
     * This method should be called during application shutdown to ensure all data is persisted.
     */
    public void shutdown() {
        for (GamePlayer gamePlayer : playerCache.values()) {
            if (gamePlayer != null) {
                savePlayer(gamePlayer);
                return;
            }

            logger.warning("Tried to save player but they were not in the cache.");
        }

        jedisPool.close();
        logger.info("Redis connection pool closed.");
    }
}