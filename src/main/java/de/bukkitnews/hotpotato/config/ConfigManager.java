package de.bukkitnews.hotpotato.config;

import de.bukkitnews.hotpotato.HotPotato;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * ConfigManager is responsible for managing the configuration file in YAML format for the HotPotato plugin.
 * This class handles loading, saving, and reloading the configuration, as well as ensuring that the necessary file
 * exists and is accessible.
 */
public class ConfigManager {
    private final @NotNull HotPotato hotPotato;
    private final @NotNull String fileName;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigManager(@NotNull HotPotato hotPotato, @NotNull String fileName) {
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
        this.configFile = new File(hotPotato.getDataFolder(), fileName);

        if (!configFile.exists()) {
            hotPotato.getDataFolder().mkdirs();
            hotPotato.saveResource(fileName, false);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Gets the FileConfiguration object, which provides access to the configuration data.
     *
     * @return The FileConfiguration object containing the loaded configuration data
     */
    public @NotNull FileConfiguration getConfig() {
        return fileConfiguration;
    }

    /**
     * Saves the current configuration to the file system. If an error occurs while saving, it logs the exception.
     */
    public void save() {
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            hotPotato.getLogger().severe("Could not save config file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Reloads the configuration from the file, refreshing the configuration data.
     */
    public void reload() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Checks if the configuration file exists on the disk.
     *
     * @return true if the configuration file exists, false otherwise
     */
    public boolean configExists() {
        return configFile.exists();
    }
}