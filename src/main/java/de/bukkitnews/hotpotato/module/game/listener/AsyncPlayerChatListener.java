package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * This class listens for player chat events and handles the chat formatting
 * and player restrictions (such as spectators being unable to chat).
 */
public class AsyncPlayerChatListener implements Listener {

    /**
     * Handles the asynchronous player chat event.
     *
     * This method cancels the chat event for players who are spectators and
     * formats the chat message for others.
     */
    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerModule.gamePlayerManager.getCachedPlayer(player.getUniqueId().toString());

        if (!gamePlayer.isAlive()) {
            event.setCancelled(true);
            return;
        }

        event.setFormat("§a%1$s §8» §7%2$s");
    }
}