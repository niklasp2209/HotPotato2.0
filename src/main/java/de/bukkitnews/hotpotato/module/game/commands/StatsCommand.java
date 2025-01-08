package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command class to display player statistics.
 * This class implements the `/stats` command to show the player their game statistics such as wins, games played, and playtime.
 */
@RequiredArgsConstructor
public class StatsCommand implements CommandExecutor {

    @NonNull private final GameModule gameModule;

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
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
