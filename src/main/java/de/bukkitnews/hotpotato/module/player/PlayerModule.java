package de.bukkitnews.hotpotato.module.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.database.SQLManager;
import de.bukkitnews.hotpotato.module.player.listener.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerQuitListener;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.Getter;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * The PlayerModule class handles all player-related functionality in the HotPotato plugin.
 * It integrates player data management, event listeners, and Redis/SQL storage handling.
 *
 * This module is part of a modular plugin system, extending the CustomModule base class.
 * It utilizes a Redis-backed caching mechanism and a fallback SQL database to manage player data.
 */
@Getter
public class PlayerModule extends CustomModule {

    private final SQLManager sqlManager;  // SQLManager instance for database operations
    private final GamePlayerManager gamePlayerManager;  // Manages player data and caching

    /**
     * Constructs the PlayerModule with dependencies provided by the HotPotato plugin.
     *
     * @param hotPotato The main HotPotato plugin instance
     */
    public PlayerModule(HotPotato hotPotato) {
        super(hotPotato, "Player"); // Initializes the module with the name "Player"

        // Retrieves the SQL manager and initializes the GamePlayerManager with Redis and logging
        this.sqlManager = this.getHotPotato().getSqlManager();
        this.gamePlayerManager = new GamePlayerManager(
                sqlManager, new JedisPool("localhost", 6379), this.getHotPotato().getLogger());
    }

    /**
     * Activates the module by registering event listeners and starting its functionality.
     * This method is called when the plugin is enabled.
     */
    @Override
    public void activate() {
        // Registers event listeners for handling player join and quit events
        setListeners(Arrays.asList(new PlayerJoinListener(this), new PlayerQuitListener(this)));
        start(); // Starts additional module-specific tasks (if any)
    }

    /**
     * Deactivates the module by shutting down any ongoing processes and saving player data.
     * This method is called when the plugin is disabled.
     */
    @Override
    public void deactivate() {
        // Ensures that all cached player data is saved and the Redis pool is closed
        gamePlayerManager.shutdown();
    }
}