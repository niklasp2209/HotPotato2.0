package de.bukkitnews.hotpotato.module.game.util;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class GameUtil {

    /**
     * Selects a random player from the provided list of players.
     *
     * @param players The list of players to choose from.
     * @return An Optional containing the selected player, or an empty Optional if the list is empty.
     */
    public static @NotNull Optional<Player> selectRandomPlayer(@NotNull List<Player> players) {
        if (players.isEmpty()) {
            return Optional.empty();
        }

        Collections.shuffle(players);
        return Optional.of(players.getFirst());
    }

    /**
     * Sets the Hot Potato armor for the specified player.
     * This includes a red leather helmet, chestplate, leggings, and boots.
     *
     * @param player The player who will receive the Hot Potato armor.
     */
    public static void setHotPotatoArmor(@NotNull Player player) {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta leatherHelmetMeta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
        if (leatherHelmetMeta != null) {
            leatherHelmetMeta.setColor(Color.RED);
            leatherHelmet.setItemMeta(leatherHelmetMeta);
        }

        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherChestplateMeta = (LeatherArmorMeta) leatherChestplate.getItemMeta();
        if (leatherChestplateMeta != null) {
            leatherChestplateMeta.setColor(Color.RED);
            leatherChestplate.setItemMeta(leatherChestplateMeta);
        }

        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLeggingsMeta = (LeatherArmorMeta) leatherLeggings.getItemMeta();
        if (leatherLeggingsMeta != null) {
            leatherLeggingsMeta.setColor(Color.RED);
            leatherLeggings.setItemMeta(leatherLeggingsMeta);
        }

        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootsMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        if (leatherBootsMeta != null) {
            leatherBootsMeta.setColor(Color.RED);
            leatherBoots.setItemMeta(leatherBootsMeta);
        }

        player.getInventory().setHelmet(leatherHelmet);
        player.getInventory().setChestplate(leatherChestplate);
        player.getInventory().setLeggings(leatherLeggings);
        player.getInventory().setBoots(leatherBoots);
    }

    /**
     * Teleports all players to the provided lobby location.
     *
     * @param players          The list of players to teleport.
     * @param lobbyLocationOpt The location to teleport the players to.
     */
    public static void teleportPlayersToLobby(@NotNull List<Player> players, @NotNull Optional<Location> lobbyLocationOpt) {
        lobbyLocationOpt.ifPresent(lobbyLocation -> {
            for (Player player : players) {
                player.teleport(lobbyLocation);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                player.setGameMode(GameMode.SURVIVAL);
            }
        });
    }

    /**
     * Clears the inventories of all players except for the player holding the Hot Potato.
     *
     * @param players The list of players whose inventories should be cleared.
     * @param potato  The player holding the Hot Potato.
     */
    public static void clearInventoriesExceptPotato(@NotNull List<Player> players, @NotNull Player potato) {
        players.stream()
                .filter(player -> !player.equals(potato))
                .forEach(player -> player.getInventory().clear());
    }

    /**
     * Triggers a firework and explosion particle effect at the specified player's location.
     *
     * @param player The player at whose location the effects will occur.
     */
    public static void triggerFireworkAndExplosionEffects(@NotNull Player player) {
        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 0, 0, 0, 0);
        player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1F, 1F);
    }

    /**
     * Sets the level display for all players to the remaining time.
     * If the remaining time is less than or equal to 20 seconds, it sets the level to that value.
     * If the time is greater than 20 seconds, the level is set to 0.
     *
     * @param players The list of players whose level will be updated.
     * @param time    The remaining time in seconds.
     */
    public static void updatePlayerLevels(@NotNull List<Player> players, int time) {
        players.forEach(player -> player.setLevel(time <= 20 ? time : 0));
    }

    /**
     * Sends a message to all players currently online.
     *
     * @param message The message to broadcast to all players.
     */
    public static void broadcastMessage(@NotNull String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    /**
     * Checks if a given player exists in the list of players.
     *
     * @param players The list of players to check against.
     * @param player  The player to check.
     * @return An Optional containing the player if found, or an empty Optional if not found.
     */
    public static @NotNull Optional<Player> findPlayerInList(@NotNull List<Player> players, @NotNull Player player) {
        return players.stream()
                .filter(p -> p.equals(player))
                .findFirst();
    }
}