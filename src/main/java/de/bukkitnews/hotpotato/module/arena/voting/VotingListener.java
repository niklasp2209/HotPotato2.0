package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.utils.MessageUtil;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class VotingListener implements Listener {

    private final Voting voting;

    public VotingListener(@NonNull Voting voting) {
        this.voting = voting;
    }

    @EventHandler
    public void handleInventoryClick(@NonNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equals(MessageUtil.getMessage("inventory_voting"))) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null) {
            Optional.ofNullable(clickedItem.getItemMeta())
                    .map(itemMeta -> itemMeta.getPersistentDataContainer().get(
                            new NamespacedKey("hotpotato", "arena_id"),
                            PersistentDataType.STRING))
                    .flatMap(arenaName -> this.voting.getArenaList().stream()
                            .filter(arena -> arena.getName().equals(arenaName))
                            .findFirst())
                    .ifPresent(selectedArena -> this.voting.vote(player, selectedArena));
        }
    }
}