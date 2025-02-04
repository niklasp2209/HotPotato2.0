package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Command handler for the HotPotato plugin.
 * This class is responsible for handling the HotPotato commands and executing the appropriate actions.
 */
@RequiredArgsConstructor
public class HotPotatoCommand implements CommandExecutor {

    private final @NotNull GameModule gameModule;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "setlobby" -> {
                if (args.length == 1) {
                    return true;
                }
            }
            case "setup" -> {
                if (args.length >= 3) switch (args[1].toLowerCase()) {
                    case "create" -> handleCreateArena(args[2], player);
                    case "spawnlocation" -> handleSpawnLocation(player);
                }
            }
        }
        return true;
    }

    /**
     * Handles the "create" subcommand under the "setup" command.
     * This method is used to create a new arena with the specified name.
     *
     * @param arenaName The name of the arena to create.
     * @param player    The player who issued the command.
     */
    private void handleCreateArena(@NotNull String arenaName, @NotNull Player player) {
        Optional<ArenaModule> arenaModule = gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class);

        arenaModule.ifPresent(module -> {
            Arena arena = new Arena(arenaName, module);

            if (arena.alreadyExists()) {
                player.sendMessage(MessageUtil.getMessage("setup_arena_exists"));
            } else {
                player.sendMessage(MessageUtil.getMessage("setup_arena_spawn"));
            }
        });
    }


    /**
     * Handles the "spawnlocation" subcommand under the "setup" command.
     * This method is used to set the spawn location for the arena.
     *
     * @param player The player who issued the command.
     */
    private void handleSpawnLocation(@NotNull Player player) {
        player.sendMessage(MessageUtil.getMessage("setup_spawn_location"));
    }
}
