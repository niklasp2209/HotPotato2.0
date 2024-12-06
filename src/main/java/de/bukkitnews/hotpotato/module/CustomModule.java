package de.bukkitnews.hotpotato.module;

import de.bukkitnews.hotpotato.HotPotato;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstract base class for modules that are loaded and unloaded in a Bukkit plugin.
 * This class handles the registration of event listeners and commands, as well as the activation and deactivation of the module.
 */
@Getter
@Setter
public abstract class CustomModule {

    // The instance of the HotPotato plugin to which this module belongs.
    private final HotPotato hotPotato;

    // The name of the module, used for identification purposes.
    private final String moduleName;

    // A list of event listeners to be registered for this module.
    private List<Listener> listeners;

    // A map of commands associated with this module. The keys are command names,
    // and the values are the CommandExecutor instances that handle the commands.
    private Map<String, CommandExecutor> commandExecutors;

    /**
     * Constructor that initializes the module with the HotPotato plugin instance and module name.
     *
     * @param hotPotato The HotPotato plugin instance to which this module belongs.
     * @param name The name of the module.
     */
    public CustomModule(HotPotato hotPotato, String name) {
        this.hotPotato = hotPotato;
        this.moduleName = name;
        this.commandExecutors = new HashMap<>();
    }

    /**
     * Abstract method to activate the module. Must be implemented by subclasses.
     */
    public abstract void activate();

    /**
     * Abstract method to deactivate the module. Must be implemented by subclasses.
     */
    public abstract void deactivate();

    /**
     * Initializes the module by registering event listeners and binding commands.
     * This method should be called when the module is loaded.
     */
    public void start() {
        logToConsole("Initializing module: " + moduleName);
        registerEventListeners();
        bindCommands();
        logToConsole("Module: " + moduleName + " loaded successfully!");
    }

    /**
     * Disables the module by unregistering event listeners and performing any necessary cleanup.
     * This method should be called when the module is unloaded.
     */
    public void stop() {
        logToConsole("Disabling module: " + moduleName);
        unregisterEventListeners();
        logToConsole("Module: " + moduleName + " has been disabled.");
    }

    /**
     * Binds the commands for this module. It iterates over the command map and registers each
     * command with the corresponding executor.
     */
    private void bindCommands() {
        if (commandExecutors == null || commandExecutors.isEmpty()) return;
        commandExecutors.forEach((command, executor) -> {
            hotPotato.getCommand(command).setExecutor(executor);
            logToConsole("Module: " + moduleName + " has registered command: " + command);
        });
    }

    /**
     * Registers the event listeners for this module. It iterates over the list of listeners
     * and registers each with the plugin's event manager.
     */
    private void registerEventListeners() {
        if (listeners == null || listeners.isEmpty()) return;
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        listeners.forEach(listener -> {
            pluginManager.registerEvents(listener, hotPotato);
            logToConsole("Module: " + moduleName + " has registered event listener: " + listener);
        });
    }

    /**
     * Unregisters the event listeners for this module. It iterates over the list of listeners
     * and removes all of them from the event handler list.
     */
    private void unregisterEventListeners() {
        if (listeners == null || listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            logToConsole("Module: " + moduleName + " has unregistered event listener: " + listener);
        });
    }

    /**
     * Logs a message to the console with the prefix "Module: ".
     *
     * @param message The message to log.
     */
    private void logToConsole(String message) {
        Bukkit.getLogger().info(message);
    }
}