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
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ArenaModule extends CustomModule {

    @NonNull private final ConfigManager arenaConfig;
    @NonNull private final HotPotato hotPotato;

    private Voting voting;

    public ArenaModule(@NonNull HotPotato hotPotato) {
        super(hotPotato, "Arena");

        this.hotPotato = hotPotato;
        this.arenaConfig = new ConfigManager(hotPotato, "arena.yml");
    }

    /**
     * Activates the Arena module, setting up the necessary components such as voting.
     * This method is called when the module is enabled.
     */
    @Override
    public void activate() {
        this.voting = new Voting(this);
        setListeners(List.of(new VotingListener(voting)));
        this.voting.initVoting();
    }

    /**
     * Deactivates the Arena module. This method is called when the module is disabled.
     * In this case, no additional cleanup is needed.
     */
    @Override
    public void deactivate() {

    }
}