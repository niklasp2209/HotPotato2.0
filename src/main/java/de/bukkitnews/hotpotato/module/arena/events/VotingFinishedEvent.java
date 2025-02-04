package de.bukkitnews.hotpotato.module.arena.events;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when the voting has finished.
 * This event is used to signal that the voting process has been completed.
 */
@Getter
public class VotingFinishedEvent extends Event {

    private static final @NotNull HandlerList handlerList = new HandlerList();

    private final Arena winnerArena;

    public VotingFinishedEvent(@NotNull Arena winnerArena) {
        this.winnerArena = winnerArena;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

}