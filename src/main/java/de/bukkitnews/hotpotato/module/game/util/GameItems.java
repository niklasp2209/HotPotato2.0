package de.bukkitnews.hotpotato.module.game.util;

import de.bukkitnews.hotpotato.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GameItems {

    public static final ItemStack ITEM_LOBBY_VOTING = new ItemUtil(Material.PAPER)
            .setDisplayname("Map Voting")
            .setLore(" ", "Rechtsklick zum öffnen.")
            .build();

    public static final ItemStack ITEM_LOBBY_LEAVE = new ItemUtil(Material.MAGMA_CREAM)
            .setDisplayname("Runde verlassen")
            .setLore(" ", "Rechtsklick zum verlassen.")
            .build();
}
