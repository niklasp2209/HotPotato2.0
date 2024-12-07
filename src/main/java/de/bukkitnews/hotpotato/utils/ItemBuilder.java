package de.bukkitnews.hotpotato.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    /**
     * Constructs an ItemBuilder with the specified material.
     * @param material The material of the item to be created.
     */
    public ItemBuilder(Material material){
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Sets the display name of the item.
     * @param name The display name to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setDisplayname(final String name){
        this.itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder addEnchantment (Enchantment enchantment, int level){
        this.itemMeta.addEnchant(enchantment, level, true);
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Sets the amount of the item.
     * @param amount The amount to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setAmount (final int amount){
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the lore (description) of the item.
     * @param lore The lore to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setLore (final String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Makes the item unbreakable.
     */
    public void setUnbreakable(){
        itemMeta.setUnbreakable(true);
    }

    /**
     * Makes the item glow by adding a hidden enchantment.
     */
    public void setGlowing(){
        itemMeta.addEnchant(Enchantment.UNBREAKING, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    /**
     * Builds the final ItemStack with the applied settings.
     * @return The resulting ItemStack.
     */
    public ItemStack build () {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}