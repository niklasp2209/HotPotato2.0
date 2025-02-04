package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command class to start the game countdown from the lobby.
 * This command is used to set the starting countdown time when the game is in the lobby state.
 */
@RequiredArgsConstructor
public class StartCommand implements CommandExecutor {

    private final @NotNull GameModule gameModule;
    private final int STARTING_TIME = 5;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!(gameModule.getCurrentState().isPresent() &&
                gameModule.getCurrentState().get() instanceof LobbyState lobbyState)) {
            player.sendMessage(MessageUtil.getMessage("lobby_start"));
            return true;
        }

        if (lobbyState.getLobbyCountdown().getSeconds() <= STARTING_TIME) {
            player.sendMessage(MessageUtil.getMessage("lobby_start_already"));
            return true;
        }

        lobbyState.getLobbyCountdown().setSeconds(STARTING_TIME);
        player.sendMessage(MessageUtil.getMessage("lobby_started"));

        return true;
    }
}
