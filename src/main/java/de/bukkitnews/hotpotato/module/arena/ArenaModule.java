package de.bukkitnews.hotpotato.module.arena;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.module.arena.voting.Voting;
import de.bukkitnews.hotpotato.module.arena.voting.VotingListener;
import de.bukkitnews.hotpotato.module.database.ConfigManager;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.listener.PlayerJoinListener;
import de.bukkitnews.hotpotato.module.player.listener.PlayerQuitListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ArenaModule extends CustomModule {

    public static ArenaModule instance;
    private final ConfigManager arenaConfig;
    private final HotPotato hotPotato;

    private Voting voting;

    private final List<Arena> arenaList = new ArrayList<>();

    public ArenaModule(HotPotato hotPotato) {
        super(hotPotato, "Arena");

        instance = this;
        this.hotPotato = hotPotato;
        this.arenaConfig = new ConfigManager(hotPotato, "arena.yml");
    }

    @Override
    public void activate() {
        this.voting = new Voting(this);

        setListeners(List.of(new VotingListener(voting)));

        this.voting.initVoting();
    }

    @Override
    public void deactivate() {

    }
}
