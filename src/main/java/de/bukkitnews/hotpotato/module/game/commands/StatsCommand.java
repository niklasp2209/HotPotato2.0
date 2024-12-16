package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.util.MessageUtil;
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
        if (!(sender instanceof Player player)) {
            return true;
        }

        GamePlayer gamePlayer = this.gameModule.getHotPotato().getModuleManager().getModule(PlayerModule.class).get()
                .getGamePlayerManager().getCachedPlayer(player.getUniqueId().toString());

        player.sendMessage(MessageUtil.getMessage("stats_line1", player.getName()));
        player.sendMessage("");
        player.sendMessage(MessageUtil.getMessage("stats_line2", String.valueOf(gamePlayer.getWins())));
        player.sendMessage(MessageUtil.getMessage("stats_line3", String.valueOf(gamePlayer.getGamesPlayed())));
        player.sendMessage(MessageUtil.getMessage("stats_line4", String.valueOf(gamePlayer.getPlaytime())));


        return false;
    }
}
