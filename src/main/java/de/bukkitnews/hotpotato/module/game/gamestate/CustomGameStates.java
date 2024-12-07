package de.bukkitnews.hotpotato.module.game.gamestate;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;

@Getter
public abstract class CustomGameStates {

    private final GameModule gameModule;
    private List<Listener> listeners;
    private final String name;

    public CustomGameStates(GameModule gameModule, String name) {
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
    protected void logToConsole(String message) {
        Bukkit.getLogger().info(message);
    }
}