package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Getter
public class Voting {

    private static final int VOTING_MAP_AMOUNT = 2;
    private final ArenaModule arenaModule;
    private final List<Arena> votingArena;

    public Voting(ArenaModule arenaModule) {
        this.arenaModule = arenaModule;
        this.votingArena = chooseRandomMaps(loadPlayableArenas());
    }

    public void initVoting() {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                List<Arena> playableArenas = loadPlayableArenas();

                if (playableArenas.size() < VOTING_MAP_AMOUNT) {
                    arenaModule.getHotPotato().getLogger().info("Not enough playable maps set up");
                } else {
                    votingArena.addAll(chooseRandomMaps(playableArenas));
                }
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
                .limit(VOTING_MAP_AMOUNT)
                .collect(Collectors.toList());
    }

    private List<Arena> chooseRandomMaps(List<Arena> playableArenas) {
        return ThreadLocalRandom.current()
                .ints(0, playableArenas.size())
                .distinct()
                .limit(VOTING_MAP_AMOUNT)
                .mapToObj(playableArenas::get)
                .collect(Collectors.toList());
    }
}