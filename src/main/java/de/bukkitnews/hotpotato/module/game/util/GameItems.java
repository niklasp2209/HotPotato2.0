package de.bukkitnews.hotpotato.module.game.util;

import de.bukkitnews.hotpotato.util.ItemBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for defining special game items used in the lobby.
 * This class provides pre-configured items for specific actions like voting and leaving the round.
 */
@UtilityClass
public class GameItems {

    public static final ItemStack ITEM_LOBBY_VOTING = new ItemBuilder(Material.PAPER)
            .setDisplayname("Map Voting")
            .setLore(" ", "Rechtsklick zum öffnen.")
            .build();

    public static final ItemStack ITEM_LOBBY_LEAVE = new ItemBuilder(Material.MAGMA_CREAM)
            .setDisplayname("Runde verlassen")
            .setLore(" ", "Rechtsklick zum verlassen.")
            .build();
}
