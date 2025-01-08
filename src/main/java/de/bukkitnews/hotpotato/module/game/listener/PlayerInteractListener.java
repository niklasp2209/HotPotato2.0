package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.util.GameItems;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Listener for player interactions in the game.
 * This class listens to player interaction events and handles specific actions like using special items (e.g., voting, leaving the lobby).
 */
@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    @NonNull private final GameModule gameModule;

    @EventHandler
    public void handleInteract(@NonNull PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        event.setCancelled(true);

        if (event.getItem().isSimilar(GameItems.ITEM_LOBBY_VOTING)) {
            this.gameModule.getHotPotato().getModuleManager().getModule(ArenaModule.class).get()
                    .getVoting().createVotingInventory(player);
            return;
        }

        if(event.getItem().isSimilar(GameItems.ITEM_LOBBY_LEAVE)){
            player.kickPlayer(null);
            return;
        }
    }
}
