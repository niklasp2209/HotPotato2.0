package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.gamestate.ending.EndingState;
import de.bukkitnews.hotpotato.module.game.gamestate.ingame.IngameState;
import de.bukkitnews.hotpotato.module.game.gamestate.lobby.LobbyState;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final GameModule gameModule;

    public PlayerConnectionListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleJoin(@NonNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        this.gameModule.getCurrentState().ifPresent(state -> {
            if (state instanceof LobbyState) {
                handleLobbyJoin(player);
            } else if (state instanceof IngameState) {
                handleIngameJoin(player);
            } else if (state instanceof EndingState) {
                player.kickPlayer("Game has ended");
            }
        });
    }

    @EventHandler
    public void handleQuit(@NonNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        this.gameModule.getCurrentState().ifPresent(state -> {
            if (state instanceof LobbyState) {
                handleLobbyQuit(player);
            } else if (state instanceof IngameState) {
                handleIngameQuit(player);
            }
        });
    }

    private void handleLobbyJoin(@NonNull Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        showPlayersToEachOther(player);

        LobbyState lobbyState = (LobbyState) this.gameModule.getCurrentState().get();
        if (Bukkit.getOnlinePlayers().size() >= 2 && !lobbyState.getLobbyTask().isRunning()) {
            lobbyState.getLobbyTask().start();
        }
    }

    private void handleIngameJoin(@NonNull Player player) {
        GamePlayer gamePlayer = PlayerModule.gamePlayerManager.getCachedPlayer(player.getUniqueId().toString());
        if (gamePlayer != null) {
            gamePlayer.setAlive(false);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }

    private void handleLobbyQuit(@NonNull Player player) {
        LobbyState lobbyState = (LobbyState) this.gameModule.getCurrentState().get();
        if (Bukkit.getOnlinePlayers().size() < 2 && lobbyState.getLobbyTask().isRunning()) {
            lobbyState.getLobbyTask().startIdle();
        }
    }

    private void handleIngameQuit(@NonNull Player player) {
    }

    private void showPlayersToEachOther(Player player) {
        for (Player current : Bukkit.getOnlinePlayers()) {
            current.showPlayer(player);
            player.showPlayer(current);
        }
    }
}
