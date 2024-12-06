package de.bukkitnews.hotpotato.module.game.gamestates;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;

@Getter
public abstract class CustomGameStates {

    // The game module this game state belongs to
    private final GameModule gameModule;

    // List of event listeners associated with this game state
    private List<Listener> listeners;

    // The name of the game state
    private final String name;

    /**
     * Constructor to initialize the game state.
     *
     * @param gameModule The game module this state belongs to.
     * @param name       The name of the game state.
     */
    public CustomGameStates(GameModule gameModule, String name) {
        this.gameModule = gameModule;
        this.name = name;
    }

    /**
     * Activates the current game state.
     * This method must be implemented by subclasses to define the activation logic.
     */
    public abstract void activate();

    /**
     * Deactivates the current game state.
     * This method must be implemented by subclasses to define the deactivation logic.
     */
    public abstract void deactivate();

    /**
     * Starts the game state by logging its loading process and registering the event listeners.
     */
    public void start() {
        logToConsole("State: " + name + " is loading...");
        registerListeners();
        logToConsole("State: " + name + " finished loading.");
    }

    /**
     * Stops the game state by logging its stopping process and unregistering the event listeners.
     */
    public void stop() {
        logToConsole("State: " + name + " is stopping...");
        unregisterEventListeners();
        logToConsole("State: " + name + " successfully disabled.");
    }

    /**
     * Registers all the event listeners associated with this game state.
     * Each listener is registered with the plugin manager of the server.
     */
    private void registerListeners() {
        // Check if there are listeners to register
        if (listeners == null || listeners.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        // Register each listener with the plugin manager
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, gameModule.getHotPotato());
            logToConsole("State: " + name + " has registered event listener: " + listener);
        });
    }

    /**
     * Unregisters all the event listeners associated with this game state.
     * Each listener is unregistered from the event handler list.
     */
    private void unregisterEventListeners() {
        // Check if there are listeners to unregister
        if (listeners == null || listeners.isEmpty()) return;

        // Unregister each listener from the handler list
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            logToConsole("State: " + name + " has unregistered listener: " + listener);
        });
    }

    /**
     * Logs a message to the console with the prefix "Module: ".
     * This is used for logging information regarding the game state.
     *
     * @param message The message to be logged to the console.
     */
    private void logToConsole(String message) {
        Bukkit.getLogger().info(message);
    }
}