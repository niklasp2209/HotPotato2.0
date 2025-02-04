package de.bukkitnews.hotpotato.module.stats.command;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.stats.StatsModule;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command class to display player statistics.
 * This class implements the `/stats` command to show the player their game statistics such as wins, games played, and playtime.
 */
@RequiredArgsConstructor
public class StatsCommand implements CommandExecutor {

    private final @NotNull StatsModule statsModule;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        statsModule.getHotPotato().getModuleManager().getModule(PlayerModule.class).ifPresent(playerModule -> {
            GamePlayer gamePlayer = playerModule.getGamePlayerManager().getCachedPlayer(player.getUniqueId().toString());

            player.sendMessage(MessageUtil.getMessage("stats_line1", player.getName()));
            player.sendMessage("");
            player.sendMessage(statsModule.getStats(gamePlayer));
        });

        return true;
    }

}
