package de.bukkitnews.hotpotato.module.stats.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStats {

    private int wins;
    private int gamesPlayed;
    private long playtime;

    public PlayerStats(int wins, int gamesPlayed, long playtime) {
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
        this.playtime = playtime;
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public void incrementPlaytime(long amount) {
        this.playtime += amount;
    }

    public void resetStats() {
        this.wins = 0;
        this.gamesPlayed = 0;
        this.playtime = 0;
    }
}
