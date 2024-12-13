package de.bukkitnews.hotpotato.module.arena;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.module.arena.voting.VotingManager;
import de.bukkitnews.hotpotato.module.database.ConfigManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArenaModule extends CustomModule {

    public static ArenaModule instance;
    private final ConfigManager arenaConfig;
    private final HotPotato hotPotato;

    private VotingManager votingManager;

    private final List<Arena> arenaList = new ArrayList<>();

    public ArenaModule(HotPotato hotPotato) {
        super(hotPotato, "Arena");

        instance = this;
        this.hotPotato = hotPotato;
        this.arenaConfig = new ConfigManager(hotPotato, "arena.yml");
    }

    @Override
    public void activate() {
        this.votingManager = new VotingManager(this);
    }

    @Override
    public void deactivate() {

    }
}
