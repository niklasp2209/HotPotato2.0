package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.module.ModuleManager;
import de.bukkitnews.hotpotato.module.database.ConfigManager;
import de.bukkitnews.hotpotato.module.database.SQLManager;
import de.bukkitnews.hotpotato.utils.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class for the "HotPotato" plugin,
 * developed as part of the BukkitNews project.
 *
 * Created on: 06.12.2024
 */
@Getter
public class HotPotato extends JavaPlugin {

    private ConfigManager sqlConfigManager;
    private ConfigManager messagesConfig;

    private SQLManager sqlManager;
    private ModuleManager moduleManager;

    /**
     * This method is called when the server starts and the plugin is loaded.
     * It initializes configuration managers, loads messages, and prepares SQL and module managers.
     */
    @Override
    public void onLoad(){
        this.sqlConfigManager = new ConfigManager(this, "mysql.yml");
        this.messagesConfig = new ConfigManager(this, "messages.yml");
        MessageUtil.loadMessages(messagesConfig);

        this.sqlManager = new SQLManager(this, sqlConfigManager);
        this.moduleManager = new ModuleManager(this);
    }

    @Override
    public void onEnable(){
        this.moduleManager.activateModules();
    }

    @Override
    public void onDisable(){
        this.sqlManager.close();
        this.moduleManager.deactivateModules();
    }
}
