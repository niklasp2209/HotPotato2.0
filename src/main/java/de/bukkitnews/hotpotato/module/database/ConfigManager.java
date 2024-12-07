package de.bukkitnews.hotpotato.module.database;

import de.bukkitnews.hotpotato.HotPotato;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * ConfigManager is responsible for managing the configuration file in YAML format for the HotPotato plugin.
 * This class handles loading, saving, and reloading the configuration, as well as ensuring that the necessary file
 * exists and is accessible.
 */
public class ConfigManager {
    private final HotPotato hotPotato;
    private final String fileName;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigManager(HotPotato hotPotato, String fileName) {
        this.hotPotato = hotPotato;
        this.fileName = fileName;
        setup();
    }

    /**
     * Initializes the configuration file by checking if it exists, creating the necessary directories,
     * and loading the configuration. If the configuration file doesn't exist, it will attempt to save the
     * default configuration resource from the plugin's JAR file.
     */
    private void setup() {
        // Create a File object for the configuration file in the plugin's data folder
        configFile = new File(hotPotato.getDataFolder(), fileName);

        // If the configuration file does not exist, create the necessary directories and save the default resource
        if (!configFile.exists()) {
            hotPotato.getDataFolder().mkdirs();
            hotPotato.saveResource(fileName, false);
        }

        // Load the configuration file into the fileConfiguration object
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Gets the FileConfiguration object, which provides access to the configuration data.
     * @return The FileConfiguration object containing the loaded configuration data
     */
    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    /**
     * Saves the current configuration to the file system. If an error occurs while saving, it logs the exception.
     */
    public void save() {
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            // Log an error if saving fails
            hotPotato.getLogger().severe("Could not save config file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Reloads the configuration from the file, refreshing the configuration data.
     */
    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile); // Reload the configuration from the file
    }

    /**
     * Checks if the configuration file exists on the disk.
     * @return true if the configuration file exists, false otherwise
     */
    public boolean configExists() {
        return configFile.exists(); // Returns whether the file exists on disk
    }
}