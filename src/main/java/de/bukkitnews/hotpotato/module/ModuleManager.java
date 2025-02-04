package de.bukkitnews.hotpotato.module;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.achievement.AchievementModule;
import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.scoreboard.ScoreboardModule;
import de.bukkitnews.hotpotato.module.stats.StatsModule;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Manages and controls the lifecycle of modules within the HotPotato application.
 * This class ensures that modules can be integrated into the system and removed when necessary.
 */
public final class ModuleManager {

    private final @NotNull HotPotato hotPotato;
    private final @NotNull LinkedHashMap<Class<? extends CustomModule>, CustomModule> modules;

    public ModuleManager(@NotNull HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.modules = new LinkedHashMap<>();
    }

    /**
     * Activates all modules in the module list by calling their enable method.
     * This method is responsible for starting all modules in the system.
     */
    public void activateModules() {
        modules.put(GameModule.class, new GameModule(hotPotato));
        modules.put(PlayerModule.class, new PlayerModule(hotPotato));
        modules.put(ArenaModule.class, new ArenaModule(hotPotato));
        modules.put(ScoreboardModule.class, new ScoreboardModule(hotPotato));
        modules.put(AchievementModule.class, new AchievementModule(hotPotato));
        modules.put(StatsModule.class, new StatsModule(hotPotato));

        loadModules();
    }

    /**
     * Deactivates all modules by calling their stop method.
     * This method removes all modules from the system.
     */
    public void deactivateModules() {
        unloadModules();
    }

    /**
     * Loads all modules into the system by invoking their activation methods.
     */
    private void loadModules() {
        modules.forEach((moduleClass, customModuleInstance) -> customModuleInstance.activate());
    }

    /**
     * Removes all modules from the system by calling their deactivation methods.
     */
    private void unloadModules() {
        modules.forEach((moduleClass, customModuleInstance) -> customModuleInstance.deactivate());
        modules.clear();
    }

    /**
     * Retrieves a specific module by its class.
     *
     * @param moduleClass The class of the module to be retrieved.
     * @return The requested module if it exists, otherwise null.
     */
    public @NotNull <T extends CustomModule> Optional<T> getModule(Class<T> moduleClass) {
        return Optional.ofNullable((T) modules.get(moduleClass));
    }

}
