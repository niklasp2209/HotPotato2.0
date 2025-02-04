package de.bukkitnews.hotpotato.module.arena;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.arena.voting.Voting;
import de.bukkitnews.hotpotato.module.arena.listener.VotingListener;
import de.bukkitnews.hotpotato.config.ConfigManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class ArenaModule extends CustomModule {

    private final @NotNull ConfigManager arenaConfig;
    private final @NotNull HotPotato hotPotato;

    private Voting voting;

    public ArenaModule(@NotNull HotPotato hotPotato) {
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
        voting = new Voting(this);
        voting.initVoting();

        setListeners(List.of(new VotingListener(voting)));
    }

    @Override
    public void deactivate() {
    }
}