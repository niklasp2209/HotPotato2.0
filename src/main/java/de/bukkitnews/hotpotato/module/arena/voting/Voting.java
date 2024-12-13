package de.bukkitnews.hotpotato.module.arena.voting;

import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Voting {

    private final ArenaModule arenaModule;
    private final List<Arena> votingArena;

    public Voting(ArenaModule arenaModule) {
        this.arenaModule = arenaModule;
        this.votingArena = chooseRandomMaps();
    }

    private List<Arena> chooseRandomMaps() {
        var arenas = this.arenaModule.getArenaList();

        return ThreadLocalRandom.current()
                .ints(0, arenas.size())
                .distinct()
                .limit(this.arenaModule.getVotingManager().VOTING_MAP_AMOUNT)
                .mapToObj(arenas::get)
                .collect(Collectors.toList());
    }
}