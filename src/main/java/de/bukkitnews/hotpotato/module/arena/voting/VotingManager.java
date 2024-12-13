package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class VotingManager {

    public final int VOTING_MAP_AMOUNT = 2;
    private final ArenaModule arenaModule;
    private Voting voting;

    public VotingManager(ArenaModule arenaModule) {
        this.arenaModule = arenaModule;

        initVoting();
    }

    private void initVoting() {
        CompletableFuture.runAsync(() -> {
            var arenaConfig = this.arenaModule.getArenaConfig().getConfig().getConfigurationSection(".Arenas").getKeys(false);
            var arenaList = this.arenaModule.getArenaList();

            arenaConfig.stream()
                    .map(Arena::new)
                    .filter(Arena::isPlayable)
                    .forEach(arena -> {
                        arenaList.add(arena);
                        if (arenaList.size() >= VOTING_MAP_AMOUNT && voting == null) {
                            voting = new Voting(this.arenaModule);
                        }
                    });

            if (arenaList.size() < VOTING_MAP_AMOUNT) {
                this.arenaModule.getHotPotato().getLogger().info("Not enough maps set up");
            }
        });
    }
}