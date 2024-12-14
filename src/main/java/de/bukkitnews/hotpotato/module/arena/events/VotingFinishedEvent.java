package de.bukkitnews.hotpotato.module.arena.events;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when the voting has finished.
 * This event is used to signal that the voting process has been completed.
 */
public class VotingFinishedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

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

    /**
     * Gets the winner arena of the vote.
     *
     * @return The arena that was selected as the winner.
     */
    public Arena getWinnerArena() {
        return winnerArena;
    }
}