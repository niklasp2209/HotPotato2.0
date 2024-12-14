package de.bukkitnews.hotpotato.module.game.listener;

import de.bukkitnews.hotpotato.module.game.GameModule;
import de.bukkitnews.hotpotato.module.game.util.GameItems;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {

    private final GameModule gameModule;

    public PlayerInteractListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

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

        if (event.getItem().isSimilar(GameItems.ITEM_LOBBY_VOTING)) {

        }
    }
}
