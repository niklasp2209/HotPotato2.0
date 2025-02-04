package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.util.ItemBuilder;
import de.bukkitnews.hotpotato.util.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
public class Voting {

    private static final int VOTING_MAP_AMOUNT = 2;

    private final @NotNull ArenaModule arenaModule;
    private final @NotNull List<Arena> arenaList;

    public Voting(@NotNull ArenaModule arenaModule) {
        this.arenaModule = arenaModule;
        this.arenaList = new CopyOnWriteArrayList<>();
    }

    /**
     * Initializes the voting process by asynchronously loading playable arenas and choosing random ones for the vote.
     */
    public void initVoting() {
        CompletableFuture.runAsync(() -> {
            List<Arena> playableArenas = loadPlayableArenas();

            if (playableArenas.size() < VOTING_MAP_AMOUNT) {
                arenaModule.getHotPotato().getLogger().info("Not enough playable maps set up");
                return;
            }

            arenaList.addAll(chooseRandomMaps(playableArenas));
        });
    }

    /**
     * Loads playable arenas from the configuration file, filtering for valid arenas.
     *
     * @return List of playable arenas.
     */
    private @NotNull List<Arena> loadPlayableArenas() {
        return arenaModule.getArenaConfig()
                .getConfig()
                .getConfigurationSection(".Arenas").getKeys(false)
                .stream()
                .map(name -> new Arena(name, arenaModule))
                .filter(Arena::isPlayable)
                .collect(Collectors.toList());
    }

    /**
     * Randomly selects a specified number of arenas from the list of playable arenas.
     *
     * @param playableArenas List of playable arenas.
     * @return List of randomly selected arenas.
     */
    private @NotNull List<Arena> chooseRandomMaps(@NotNull List<Arena> playableArenas) {
        Collections.shuffle(playableArenas);
        return playableArenas.stream()
                .limit(VOTING_MAP_AMOUNT)
                .collect(Collectors.toList());
    }

    /**
     * Determines the arena with the most votes.
     *
     * @return The arena with the highest number of votes.
     */
    public @NotNull Arena getVotingWinner() {
        return arenaList.stream()
                .max(Comparator.comparingInt(Arena::getVotes))
                .orElseThrow(() -> new IllegalStateException("No arenas available for voting"));
    }

    /**
     * Creates the voting inventory for a player to select from the available arenas.
     *
     * @param player The player for whom the inventory is created.
     */
    public void createVotingInventory(@NotNull Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, MessageUtil.getMessage("voting"));
        NamespacedKey namespacedKey = new NamespacedKey("hotpotato", "arena_id");

        arenaList.forEach(arena -> {
            ItemStack itemStack = new ItemBuilder(Material.PAPER)
                    .setDisplayname(arena.getName())
                    .setLore("Votes: " + arena.getVotes())
                    .build();

            itemStack.getItemMeta().getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, arena.getName());
            inventory.addItem(itemStack);
        });

        player.openInventory(inventory);
    }

    /**
     * Allows a player to vote for a specific arena.
     * If the player has already voted, it prevents them from voting again.
     *
     * @param player The player who is voting.
     * @param arena  The arena that the player is voting for.
     */
    public void vote(@NotNull Player player, @NotNull Arena arena) {
        UUID playerUUID = player.getUniqueId();

        if (arenaList.stream().anyMatch(a -> a.getVotedPlayers().contains(playerUUID))) {
            player.sendMessage(MessageUtil.getMessage("voting_already"));
            return;
        }

        if (arena.addVote(playerUUID)) {
            player.sendMessage(MessageUtil.getMessage("voting_map", arena.getName()));
            player.closeInventory();
        } else {
            player.sendMessage(MessageUtil.getMessage("voting_already"));
        }
    }
}