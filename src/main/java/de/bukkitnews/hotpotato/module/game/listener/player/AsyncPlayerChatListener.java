package de.bukkitnews.hotpotato.module.game.listener.player;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This class listens for player chat events and handles the chat formatting
 * and player restrictions (such as spectators being unable to chat).
 */
@RequiredArgsConstructor
public class AsyncPlayerChatListener implements Listener {

    private final @NotNull GameModule gameModule;

    /**
     * Handles the asynchronous player chat event.
     * <p>
     * This method cancels the chat event for players who are spectators and
     * formats the chat message for others.
     */
    @EventHandler
    public void handleChat(@NotNull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Optional<PlayerModule> playerModuleOpt = gameModule.getHotPotato().getModuleManager().getModule(PlayerModule.class);
        playerModuleOpt.ifPresent(playerModule -> {
            Optional<GamePlayer> gamePlayerOpt = Optional.of(playerModule.getGamePlayerManager()
                    .getCachedPlayer(player.getUniqueId().toString()));

            gamePlayerOpt.ifPresent(gamePlayer -> {
                if (!gamePlayer.isAlive()) {
                    event.setCancelled(true);
                } else {
                    event.setFormat("§a%1$s §8» §7%2$s");
                }
            });
        });
    }

}