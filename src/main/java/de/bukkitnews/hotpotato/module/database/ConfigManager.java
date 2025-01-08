package de.bukkitnews.hotpotato.module.database;

import de.bukkitnews.hotpotato.HotPotato;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * ConfigManager is responsible for managing the configuration file in YAML format for the HotPotato plugin.
 * This class handles loading, saving, and reloading the configuration, as well as ensuring that the necessary file
 * exists and is accessible.
 */
public class ConfigManager {
    @NonNull private final HotPotato hotPotato;
    @NonNull private final String fileName;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigManager(@NonNull HotPotato hotPotato, @NonNull String fileName) {
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
        this.configFile = new File(this.hotPotato.getDataFolder(), this.fileName);

        if (!this.configFile.exists()) {
            this.hotPotato.getDataFolder().mkdirs();
            this.hotPotato.saveResource(this.fileName, false);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    /**
     * Gets the FileConfiguration object, which provides access to the configuration data.
     *
     * @return The FileConfiguration object containing the loaded configuration data
     */
    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    /**
     * Saves the current configuration to the file system. If an error occurs while saving, it logs the exception.
     */
    public void save() {
        try {
            this.fileConfiguration.save(this.configFile);
        } catch (IOException e) {
            this.hotPotato.getLogger().severe("Could not save config file: " + this.fileName);
            e.printStackTrace();
        }
    }

    /**
     * Reloads the configuration from the file, refreshing the configuration data.
     */
    public void reload() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    /**
     * Checks if the configuration file exists on the disk.
     *
     * @return true if the configuration file exists, false otherwise
     */
    public boolean configExists() {
        return this.configFile.exists();
    }
}