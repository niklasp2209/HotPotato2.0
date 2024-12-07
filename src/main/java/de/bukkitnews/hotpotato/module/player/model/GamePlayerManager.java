package de.bukkitnews.hotpotato.module.player.model;

import de.bukkitnews.hotpotato.module.database.SQLManager;
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

    private final SQLManager sqlManager; // Handles SQL connections and queries
    private final JedisPool jedisPool;  // Provides Redis connection pooling
    private final Logger logger;       // Used for logging information and errors
    private final Map<String, GamePlayer> playerCache; // In-memory cache for active player data

    /**
     * Initializes the GamePlayerManager with dependencies for SQL, Redis, and logging.
     *
     * @param sqlManager The SQLManager instance for database operations.
     * @param jedisPool  The JedisPool instance for Redis connections.
     * @param logger     The logger for logging operations.
     */
    public GamePlayerManager(SQLManager sqlManager, JedisPool jedisPool, Logger logger) {
        this.sqlManager = sqlManager;
        this.jedisPool = jedisPool;
        this.logger = logger;
        this.playerCache = new ConcurrentHashMap<>(); // Thread-safe cache for player data
    }

    /**
     * Loads a player's data asynchronously, attempting to retrieve it from Redis first.
     * If the data is not found in Redis, it falls back to loading it from SQL.
     *
     * @param uuid The UUID of the player.
     * @return A CompletableFuture containing the loaded GamePlayer object, or null if no data is found.
     */
    public CompletableFuture<GamePlayer> loadPlayer(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                // Attempt to retrieve player data from Redis
                if (jedis.exists(uuid)) {
                    String name = jedis.hget(uuid, "name");
                    int wins = Integer.parseInt(jedis.hget(uuid, "wins"));
                    long playtime = Long.parseLong(jedis.hget(uuid, "playtime"));
                    int gamesPlayed = Integer.parseInt(jedis.hget(uuid, "gamesPlayed"));

                    GamePlayer player = new GamePlayer(uuid);
                    player.setData("name", name);
                    player.setData("wins", wins);
                    player.setData("playtime", playtime);
                    player.setData("gamesPlayed", gamesPlayed);

                    playerCache.put(uuid, player); // Add player to in-memory cache
                    logger.info("Loaded player " + uuid + " from Redis.");
                    return player;
                }
            } catch (Exception e) {
                logger.severe("Error loading player from Redis: " + e.getMessage());
            }

            // Fallback to SQL if Redis data is not found
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
    public void savePlayer(GamePlayer player) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String uuid = player.getUuid();
                jedis.hset(uuid, "name", (String) player.getData("name"));
                jedis.hset(uuid, "wins", String.valueOf(player.getData("wins")));
                jedis.hset(uuid, "playtime", String.valueOf(player.getData("playtime")));
                jedis.hset(uuid, "gamesPlayed", String.valueOf(player.getData("gamesPlayed")));

                logger.info("Saved and removed player " + uuid + " to Redis.");
            } catch (Exception exception) {
                logger.severe("Error saving player to Redis: " + exception.getMessage());
            }

            savePlayerToSQL(player);
            playerCache.remove(player.getUuid()); // Remove player from cache after saving
        });
    }

    /**
     * Loads a player's data from SQL asynchronously. If the data is successfully retrieved,
     * the player is added to the in-memory cache for future use.
     *
     * @param uuid The UUID of the player.
     * @return A CompletableFuture containing the loaded GamePlayer object, or null if no data is found.
     */
    private CompletableFuture<GamePlayer> loadPlayerFromSQL(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (var connection = sqlManager.getConnection();
                 var statement = connection.prepareStatement("SELECT * FROM hotpotato WHERE uuid = ?")) {

                statement.setString(1, uuid);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    GamePlayer player = new GamePlayer(uuid);
                    player.setData("name", resultSet.getString("name"));
                    player.setData("wins", resultSet.getInt("wins"));
                    player.setData("playtime", resultSet.getLong("playtime"));
                    player.setData("gamesPlayed", resultSet.getInt("gamesPlayed"));

                    logger.info("Loaded player " + uuid + " from SQL.");
                    playerCache.put(uuid, player); // Add player to cache
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
    private void savePlayerToSQL(GamePlayer player) {
        CompletableFuture.runAsync(() -> {
            try (var connection = sqlManager.getConnection();
                 var statement = connection.prepareStatement(
                         "INSERT INTO hotpotato (uuid, name, wins, playtime, gamesPlayed) VALUES (?, ?, ?, ?, ?) " +
                                 "ON DUPLICATE KEY UPDATE name = VALUES(name), wins = VALUES(wins), " +
                                 "playtime = VALUES(playtime), gamesPlayed = VALUES(gamesPlayed)")) {

                statement.setString(1, player.getUuid());
                statement.setString(2, (String) player.getData("name"));
                statement.setInt(3, (int) player.getData("wins"));
                statement.setLong(4, (long) player.getData("playtime"));
                statement.setInt(5, (int) player.getData("gamesPlayed"));

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
    public GamePlayer getCachedPlayer(String uuid) {
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
            } else {
                logger.warning("Tried to save player but they were not in the cache.");
            }
        }

        jedisPool.close();
        logger.info("Redis connection pool closed.");
    }
}