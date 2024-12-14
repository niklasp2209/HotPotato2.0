package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.game.GameModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final GameModule gameModule;

    public StatsCommand(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }


        return false;
    }
}
