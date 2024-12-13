package de.bukkitnews.hotpotato.module.arena.events;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VotingFinishedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    public VotingFinishedEvent(){
        /*
        VOTING ENDING
         */
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlerList;
    }

    public Arena getWinnerArena(){
        return null;
    }
}
