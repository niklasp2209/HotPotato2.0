package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command class to start the game countdown from the lobby.
 * This command is used to set the starting countdown time when the game is in the lobby state.
 */
public class StartCommand implements CommandExecutor {

    private final GameModule gameModule;
    private final int STARTING_TIME = 5;

    public StartCommand(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!(this.gameModule.getCurrentState().isPresent() &&
                this.gameModule.getCurrentState().get() instanceof LobbyState)) {
            player.sendMessage(MessageUtil.getMessage("lobby_start"));
            return true;
        }

        LobbyState lobbyState = (LobbyState) this.gameModule.getCurrentState().get();

        // Check if the lobby countdown is already close to or below the starting time.
        if (lobbyState.getLobbyTask().getSeconds() <= STARTING_TIME) {
            player.sendMessage(MessageUtil.getMessage("lobby_start_already"));
            return true;
        }

        // Set the lobby countdown to the predefined starting time and notify the player.
        lobbyState.getLobbyTask().setSeconds(STARTING_TIME);
        player.sendMessage(MessageUtil.getMessage("lobby_started"));

        return false;
    }
}
