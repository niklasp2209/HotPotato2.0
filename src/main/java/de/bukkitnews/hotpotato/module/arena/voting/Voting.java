package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.utils.ItemUtil;
import de.bukkitnews.hotpotato.utils.MessageUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
public class Voting {

    private static final int VOTING_MAP_AMOUNT = 2;
    private final ArenaModule arenaModule;
    private final List<Arena> arenaList;

    public Voting(ArenaModule arenaModule) {
        this.arenaModule = arenaModule;
        this.arenaList = new CopyOnWriteArrayList<>();
    }

    public void initVoting() {
        CompletableFuture.runAsync(() -> {
            List<Arena> playableArenas = loadPlayableArenas();

            if (playableArenas.size() < VOTING_MAP_AMOUNT) {
                arenaModule.getHotPotato().getLogger().info("Not enough playable maps set up");
            } else {
                arenaList.addAll(chooseRandomMaps(playableArenas));
            }
        });
    }

    private List<Arena> loadPlayableArenas() {
        return arenaModule.getArenaConfig()
                .getConfig()
                .getConfigurationSection(".Arenas").getKeys(false)
                .stream()
                .map(Arena::new)
                .filter(Arena::isPlayable)
                .collect(Collectors.toList());
    }

    private List<Arena> chooseRandomMaps(List<Arena> playableArenas) {
        Collections.shuffle(playableArenas);
        return playableArenas.stream()
                .limit(VOTING_MAP_AMOUNT)
                .collect(Collectors.toList());
    }

    public Arena getVotingWinner(){
        Arena arena = this.arenaList.getFirst();

        for(int i = 1; i < this.arenaList.size(); i++){
            if(this.arenaList.get(i).getVotes() >= arena.getVotes()){
                arena = this.arenaList.get(i);
            }
        }
        return arena;
    }

    public void createVotingInventory(@NonNull Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, MessageUtil.getMessage("Abstimmung"));

        NamespacedKey namespacedKey = new NamespacedKey("hotpotato", "arena_id");

        this.arenaList.forEach(arena -> {
            ItemStack itemStack = new ItemUtil(Material.PAPER)
                    .setDisplayname(arena.getName())
                    .setLore("Votes: " + arena.getVotes())
                    .build();

            itemStack.getItemMeta().getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, arena.getName());

            inventory.addItem(itemStack);
        });

        player.openInventory(inventory);
    }


    public void vote(@NonNull Player player, @NonNull Arena arena){
        GamePlayer gamePlayer = PlayerModule.gamePlayerManager.getCachedPlayer(player.getUniqueId().toString());

        if(gamePlayer.isVoted()){
            player.sendMessage(MessageUtil.getMessage("voting_already"));
            return;
        }

        arena.addVote();
        gamePlayer.setVoted(true);
        player.sendMessage(MessageUtil.getMessage("voting_map", arena.getName()));
        player.closeInventory();
    }
}