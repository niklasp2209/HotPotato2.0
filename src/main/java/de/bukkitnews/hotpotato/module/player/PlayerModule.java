package de.bukkitnews.hotpotato.module.player;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.database.SQLManager;
import de.bukkitnews.hotpotato.module.player.handler.PlaytimeHandler;
import de.bukkitnews.hotpotato.module.player.listener.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerQuitListener;
import de.bukkitnews.hotpotato.module.player.model.GamePlayerManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * The PlayerModule class handles all player-related functionality in the HotPotato plugin.
 * It integrates player data management, event listeners, and Redis/SQL storage handling.
 * <p>
 * This module is part of a modular plugin system, extending the CustomModule base class.
 * It utilizes a Redis-backed caching mechanism and a fallback SQL database to manage player data.
 */
@Getter
public class PlayerModule extends CustomModule {

    private final @NotNull SQLManager sqlManager;
    private final @NotNull GamePlayerManager gamePlayerManager;

    public PlayerModule(@NotNull HotPotato hotPotato) {
        super(hotPotato, "Player");

        this.sqlManager = getHotPotato().getSqlManager();
        gamePlayerManager = new GamePlayerManager(
                sqlManager, new JedisPool("localhost", 6379), getHotPotato().getLogger());

        new PlaytimeHandler(this).startHandler();
    }

    /**
     * Activates the module by registering event listeners and starting its functionality.
     * This method is called when the plugin is enabled.
     */
    @Override
    public void activate() {
        setListeners(Arrays.asList(new PlayerJoinListener(this), new PlayerQuitListener(this)));
        start();
    }

    /**
     * Deactivates the module by shutting down any ongoing processes and saving player data.
     * This method is called when the plugin is disabled.
     */
    @Override
    public void deactivate() {
        gamePlayerManager.shutdown();
    }
}