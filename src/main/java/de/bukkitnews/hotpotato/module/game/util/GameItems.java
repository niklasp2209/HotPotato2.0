package de.bukkitnews.hotpotato.module.game.util;

import de.bukkitnews.hotpotato.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GameItems {

    public static final ItemStack ITEM_LOBBY_VOTING = new ItemUtil(Material.PAPER)
            .setDisplayname("Map Voting")
            .setLore("§r", "§7Rechtsklick zum öffnen.")
            .build();
}
