package de.bukkitnews.hotpotato.utils;

import de.bukkitnews.hotpotato.module.database.ConfigManager;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public class MessageUtil {

    private static final Map<String, String> messages = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(MessageUtil.class.getName());

    // Example key constants for frequently used messages
    public static final String PREFIX = "prefix";
    public static final String NO_PERMISSION_KEY = "no_permission";
    public static final String PLAYER_JOIN_KEY = "player_join";
    public static final String PLAYER_QUIT_KEY = "player_quit";

    /**
     * Loads messages from the configuration file.
     *
     * @param configManager The ConfigManager that manages the configuration file.
     */
    public static void loadMessages(ConfigManager configManager) {
        FileConfiguration config = configManager.getConfig();

        if (!config.contains("messages")) {
            LOGGER.log(Level.WARNING, "No 'messages' section found in the configuration file!");
            return;
        }

        for (String key : config.getConfigurationSection("messages").getKeys(false)) {
            String message = config.getString("messages." + key, "");
            if (message.isEmpty()) {
                LOGGER.log(Level.WARNING, "Empty message found for key '" + key + "' in the configuration.");
            } else {
                messages.put(key, message);
            }
        }
    }

    /**
     * Retrieves a formatted message with placeholders.
     *
     * @param key          The key of the message in the configuration.
     * @param placeholders The placeholders to replace in the message.
     * @return The formatted message.
     */
    public static String getMessage(String key, String... placeholders) {
        String template = messages.getOrDefault(key, "Unknown message key: " + key);
        String formatted = formatMessage(template, placeholders);
        return ChatColor.translateAlternateColorCodes('&', formatted);
    }

    /**
     * Retrieves a message without placeholders.
     *
     * @param key The key of the message in the configuration.
     * @return The message without placeholders.
     */
    public static String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes(
                '&', messages.getOrDefault(key, "Unknown message key: " + key));
    }

    /**
     * Helper method to format messages with placeholders.
     *
     * @param template     The message template with placeholders.
     * @param placeholders The placeholders to replace in the message.
     * @return The formatted message.
     */
    private static String formatMessage(String template, String... placeholders) {
        if (placeholders.length != countPlaceholders(template)) {
            LOGGER.log(Level.WARNING, "Number of placeholders does not match the number of '%s' in the template.");
        }
        return String.format(template, (Object[]) placeholders);
    }

    /**
     * Counts the placeholders (%s) in a message template.
     *
     * @param template The message template.
     * @return The number of placeholders.
     */
    private static int countPlaceholders(String template) {
        int count = 0;
        for (int i = 0; i < template.length() - 2; i++) {
            if (template.charAt(i) == '%' && template.charAt(i + 1) == 's') {
                count++;
            }
        }
        return count;
    }

    /**
     * Retrieves a message as an Optional, in case the key is not found.
     *
     * @param key The key of the message in the configuration.
     * @return An Optional containing the message, if present, or empty if not found.
     */
    public static Optional<String> getMessageOptional(String key) {
        return Optional.ofNullable(messages.get(key));
    }

    /**
     * Retrieves a formatted message with placeholders as an Optional.
     *
     * @param key          The key of the message.
     * @param placeholders The placeholders to replace in the message.
     * @return The formatted message as an Optional.
     */
    public static Optional<String> getMessageOptional(String key, String... placeholders) {
        return getMessageOptional(key).map(message -> formatMessage(message, placeholders));
    }
}