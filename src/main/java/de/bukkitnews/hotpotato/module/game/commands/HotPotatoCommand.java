package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command handler for the HotPotato plugin.
 * This class is responsible for handling the HotPotato commands and executing the appropriate actions.
 */
public class HotPotatoCommand implements CommandExecutor {

    private final GameModule gameModule;

    public HotPotatoCommand(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
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
        return false;
    }

    private void handleCreateArena(String arenaName, Player player) {
        Arena arena = new Arena(arenaName, gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class).get());

        if (arena.alreadyExists()) {
            player.sendMessage(MessageUtil.getMessage("setup_arena_exists"));
        } else {
            player.sendMessage(MessageUtil.getMessage("setup_arena_spawn"));
        }
    }

    private void handleSpawnLocation(Player player) {

        player.sendMessage(MessageUtil.getMessage("setup_spawn_location"));
    }
}
