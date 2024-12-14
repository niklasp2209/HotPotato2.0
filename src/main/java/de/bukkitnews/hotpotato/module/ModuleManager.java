package de.bukkitnews.hotpotato.module;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.scoreboard.ScoreboardModule;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Manages and controls the lifecycle of modules within the HotPotato application.
 * This class ensures that modules can be integrated into the system and removed when necessary.
 */
public final class ModuleManager {

    private final HotPotato hotPotato;
    private final LinkedHashMap<Class<? extends CustomModule>, CustomModule> modules;

    public ModuleManager(@NonNull HotPotato hotPotato){
        this.hotPotato = hotPotato;
        this.modules = new LinkedHashMap<>();
    }

    /**
     * Activates all modules in the module list by calling their enable method.
     * This method is responsible for starting all modules in the system.
     */
    public void activateModules(){
        this.modules.put(GameModule.class, new GameModule(this.hotPotato));
        this.modules.put(PlayerModule.class, new PlayerModule(this.hotPotato));
        this.modules.put(ArenaModule.class, new ArenaModule(this.hotPotato));
        this.modules.put(ScoreboardModule.class, new ScoreboardModule(this.hotPotato));

        loadModules();
    }

    /**
     * Deactivates all modules by calling their stop method.
     * This method removes all modules from the system.
     */
    public void deactivateModules(){
        unloadModules();
    }

    /**
     * Loads all modules into the system by invoking their activation methods.
     */
    private void loadModules(){
        this.modules.forEach((moduleClass, customModuleInstance) -> {
            customModuleInstance.activate();
        });
    }

    /**
     * Removes all modules from the system by calling their deactivation methods.
     */
    private void unloadModules() {
        this.modules.forEach((moduleClass, customModuleInstance) -> {
            customModuleInstance.deactivate();
        });
        this.modules.clear();
    }

    /**
     * Retrieves a specific module by its class.
     * @param moduleClass The class of the module to be retrieved.
     * @return The requested module if it exists, otherwise null.
     */
    public Optional<CustomModule> getModule(@Nullable Class<? extends CustomModule> moduleClass) {
        return Optional.ofNullable(modules.get(moduleClass));
    }

}
