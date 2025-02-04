package de.bukkitnews.hotpotato.module.game.listener.player;

import de.bukkitnews.hotpotato.module.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final @NotNull GameModule gameModule;

    /**
     * Handles player join events and delegates to the state-specific logic.
     *
     * @param event The PlayerJoinEvent triggered when a player joins.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void handleJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        gameModule.getCurrentState().ifPresent(state -> state.onJoin(player));
    }
}
