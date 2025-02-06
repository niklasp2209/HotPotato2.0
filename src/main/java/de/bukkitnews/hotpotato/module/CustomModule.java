package de.bukkitnews.hotpotato.module;

import de.bukkitnews.hotpotato.HotPotato;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    private final @NotNull HotPotato hotPotato;
    private final @NotNull String moduleName;
    private final @NotNull List<Listener> listeners;
    private final @NotNull Map<String, CommandExecutor> commandExecutors;

    public CustomModule(@NotNull HotPotato hotPotato, @NotNull String name) {
        this.hotPotato = hotPotato;
        this.moduleName = name;
        this.listeners = new ArrayList<>();
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
        if (commandExecutors.isEmpty()) {
            return;
        }

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
        if (listeners.isEmpty()) {
            return;
        }

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
        if (listeners.isEmpty()) {
            return;
        }

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
    private void logToConsole(@NotNull String message) {
        Bukkit.getLogger().info(message);
    }
}