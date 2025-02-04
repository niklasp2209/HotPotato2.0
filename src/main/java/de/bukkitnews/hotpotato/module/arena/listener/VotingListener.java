package de.bukkitnews.hotpotato.module.arena.listener;

import de.bukkitnews.hotpotato.module.arena.voting.Voting;
import de.bukkitnews.hotpotato.util.MessageUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VotingListener implements Listener {

    private final @NotNull Voting voting;

    public VotingListener(@NotNull Voting voting) {
        this.voting = voting;
    }

    /**
     * Handles the inventory click event when a player clicks on an item in the voting inventory.
     * It cancels the event if it's a voting inventory and processes the player's vote.
     *
     * @param event The InventoryClickEvent triggered when a player clicks in an inventory.
     */
    @EventHandler
    public void handleInventoryClick(@NotNull InventoryClickEvent event) {
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
                    .flatMap(arenaName -> voting.getArenaList().stream()
                            .filter(arena -> arena.getName().equals(arenaName))
                            .findFirst())
                    .ifPresent(selectedArena -> voting.vote(player, selectedArena));
        }
    }
}