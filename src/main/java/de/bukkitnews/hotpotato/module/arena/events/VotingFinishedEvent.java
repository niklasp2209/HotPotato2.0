package de.bukkitnews.hotpotato.module.arena.events;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when the voting has finished.
 * This event is used to signal that the voting process has been completed.
 */
@Getter
public class VotingFinishedEvent extends Event {

    @NonNull private static final HandlerList handlerList = new HandlerList();

    /**
     * -- GETTER --
     *  Gets the winner arena of the vote.
     */
    private final Arena winnerArena;

    /**
     * Constructs the VotingFinishedEvent with the winner arena.
     *
     * @param winnerArena The arena selected as the winner.
     */
    public VotingFinishedEvent(@NonNull Arena winnerArena) {
        this.winnerArena = winnerArena;
    }

    /**
     * Returns the handlers for this event.
     *
     * @return HandlerList containing all registered event handlers.
     */
    @Override
    public @NonNull HandlerList getHandlers() {
        return handlerList;
    }

}