package de.bukkitnews.hotpotato;

import de.bukkitnews.hotpotato.module.ModuleManager;
import de.bukkitnews.hotpotato.module.database.ConfigManager;
import de.bukkitnews.hotpotato.module.database.SQLManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HotPotato extends JavaPlugin {

    private ConfigManager sqlConfigManager;
    private SQLManager sqlManager;
    private ModuleManager moduleManager;

    @Override
    public void onLoad(){
        this.sqlConfigManager = new ConfigManager(this, "mysql.yml");
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
