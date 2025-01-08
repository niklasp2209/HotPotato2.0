package de.bukkitnews.hotpotato.module.game.gamestate;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;

@Getter
public abstract class CustomGameStates {

    @NonNull private final GameModule gameModule;
    private List<Listener> listeners;
    @NonNull private final String name;

    public CustomGameStates(@NonNull GameModule gameModule, @NonNull String name) {
        this.gameModule = gameModule;
        this.name = name;
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
    public abstract void onJoin(@NonNull Player player);

    /**
     * Handles the logic when a player quits this game state.
     * This method must be implemented by subclasses to define what should happen when a player quits.
     *
     * @param player The player who left the game state.
     */
    public abstract void onQuit(@NonNull Player player);

    /**
     * Starts the game state by logging the loading process and registering event listeners.
     */
    public void start() {
        logToConsole("State: " + this.name + " is loading...");
        registerListeners();
        logToConsole("State: " + this.name + " finished loading.");
    }

    /**
     * Stops the game state by logging the stopping process and unregistering event listeners.
     */
    public void stop() {
        logToConsole("State: " + this.name + " is stopping...");
        unregisterEventListeners();
        logToConsole("State: " + this.name + " successfully disabled.");
    }

    /**
     * Registers event listeners for this game state.
     * Listeners are added to the plugin manager if they exist.
     */
    private void registerListeners() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        this.listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, this.gameModule.getHotPotato());
            logToConsole("State: " + this.name + " has registered event listener: " + listener);
        });
    }

    /**
     * Unregisters event listeners for this game state.
     * Listeners are removed from the handler list if they exist.
     */
    private void unregisterEventListeners() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        this.listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            logToConsole("State: " + this.name + " has unregistered listener: " + listener);
        });
    }

    /**
     * Logs a message to the console.
     * @param message The message to log.
     */
    protected void logToConsole(@NonNull String message) {
        Bukkit.getLogger().info(message);
    }
}