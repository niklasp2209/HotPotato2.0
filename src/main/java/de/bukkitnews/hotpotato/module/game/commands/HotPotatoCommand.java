package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.utils.MessageUtil;
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

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        // Command structure: /hotpotato setup create <arena_name>
        if (args.length >= 3 && args[0].equalsIgnoreCase("setup") && args[1].equalsIgnoreCase("create")) {
            String arenaName = args[2];
            Arena arena = new Arena(arenaName);

            if (arena.alreadyExists()) {
                player.sendMessage(MessageUtil.getMessage("setup_arena_exists"));
                return true;
            }

            player.sendMessage(MessageUtil.getMessage("setup_arena_spawn"));
            // Add additional logic to handle the arena creation here (e.g., setting spawn points, saving arena, etc.)
        }

        return false;
    }
}
