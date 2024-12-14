package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * This class listens for player chat events and handles the chat formatting
 * and player restrictions (such as spectators being unable to chat).
 */
public class AsyncPlayerChatListener implements Listener {

    private final GameModule gameModule;

    public AsyncPlayerChatListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    /**
     * Handles the asynchronous player chat event.
     *
     * This method cancels the chat event for players who are spectators and
     * formats the chat message for others.
     */
    @EventHandler
    public void handleChat(@NonNull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = this.gameModule.getHotPotato().getModuleManager().getModule(PlayerModule.class).get()
                .getGamePlayerManager().getCachedPlayer(player.getUniqueId().toString());

        if (!gamePlayer.isAlive()) {
            event.setCancelled(true);
            return;
        }

        event.setFormat("§a%1$s §8» §7%2$s");
    }
}