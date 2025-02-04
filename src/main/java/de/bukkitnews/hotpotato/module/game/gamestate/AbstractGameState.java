package de.bukkitnews.hotpotato.module.game.gamestate;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class AbstractGameState {

    private final @NotNull GameModule gameModule;

    public AbstractGameState(@NotNull GameModule gameModule) {
        this.gameModule = gameModule;
    }

    /**
     * Activates the game state.
     * This method must be implemented by subclasses to define specific activation logic.
     */
    public abstract void activate();

    /**
     * Deactivates the game state.
     * This method must be implemented by subclasses to define specific deactivation logic.
     */
    public abstract void deactivate();

    /**
     * Handles the logic when a player joins this game state.
     * This method must be implemented by subclasses to define what should happen when a player joins.
     *
     * @param player The player who joined the game state.
     */
    public abstract void onJoin(@NotNull Player player);

    /**
     * Handles the logic when a player quits this game state.
     * This method must be implemented by subclasses to define what should happen when a player quits.
     *
     * @param player The player who left the game state.
     */
    public abstract void onQuit(@NotNull Player player);

    protected void logToConsole(@NotNull String message) {
        Bukkit.getLogger().info(message);
    }
}