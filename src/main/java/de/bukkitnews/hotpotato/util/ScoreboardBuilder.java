package de.bukkitnews.hotpotato.util;


import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class ScoreboardBuilder {
    @NonNull private final Scoreboard scoreboard;
    @NonNull private final Objective sideBarObjective;
    @NonNull private final Map<Integer, String> scoreboardValues;

    /**
     * Create an sidebar objective by using an existing scoreboard
     * @param scoreboard Scoreboard to use
     * @param objectiveName Name of the objective to use
     */
    public ScoreboardBuilder(@NonNull Scoreboard scoreboard, @NonNull String objectiveID, @NonNull String objectiveName) {
        this.scoreboard = scoreboard;
        this.scoreboardValues = new HashMap<>();
        if(this.scoreboard.getObjective(objectiveID) != null){
            this.sideBarObjective = this.scoreboard.getObjective(objectiveID);
        }else {
            this.sideBarObjective = this.scoreboard.registerNewObjective(objectiveID, "dummy");
            this.sideBarObjective.setDisplayName(objectiveName);
            this.sideBarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }

    /**
     * Create an sidebar objective by using the main scoreboard
     * @param objectiveName Name of the objective to use
     */
    public ScoreboardBuilder(@NonNull String objectiveName) {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.scoreboardValues = new HashMap<>();
        this.sideBarObjective = this.scoreboard.registerNewObjective(objectiveName, "dummy");
        this.sideBarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Set a line
     * @param line Number of the line
     * @param value Text of the line
     * @return Current instance
     */
    public ScoreboardBuilder setLine(int line, @NonNull String value){
        this.sideBarObjective.getScore(value).setScore(line);
        this.scoreboardValues.put(line, value);
        return this;
    }

    /**
     * Remove a line
     * @param line Number of the line to remove
     * @return Current instance
     */
    public ScoreboardBuilder removeLine(int line){
        String value = this.scoreboardValues.get(line);
        this.scoreboard.resetScores(value);
        this.scoreboardValues.remove(line);
        return this;
    }

    /**
     * Replace a line
     * @param line Number of the line to replace
     * @param newValue New text to set
     * @return Current instance
     */
    public ScoreboardBuilder replaceLine(int line, @NonNull String newValue){
        this.removeLine(line);
        this.sideBarObjective.getScore(newValue).setScore(line);
        this.scoreboardValues.put(line, newValue);
        return this;
    }

    /**
     * Register a new team
     * @param teamName Team to register
     * @return Current instance
     */
    public ScoreboardBuilder registerTeam(@NonNull String teamName){
        if(this.scoreboard.getTeam(teamName) == null) this.scoreboard.registerNewTeam(teamName);
        return this;
    }

    /**
     * Set team prefix
     * @param teamName Team to set prefix
     * @param prefix Prefix to set
     * @return Current instance
     */
    public ScoreboardBuilder setTeamPrefix(@NonNull String teamName, @NonNull String prefix){
        if(prefix.length() <= 16){
            this.scoreboard.getTeam(teamName).setPrefix(prefix);
        }
        return this;
    }

    /**
     * Set team suffix
     * @param teamName Team to set suffix
     * @param suffix Suffix to set
     * @return Current instance
     */
    public ScoreboardBuilder setTeamSuffix(@NonNull String teamName, @NonNull String suffix){
        if(suffix.length() <= 16){
            this.scoreboard.getTeam(teamName).setSuffix(suffix);
        }
        return this;
    }

    /**
     * @return Current scoreboard
     */
    public Scoreboard toScoreboard(){
        return this.scoreboard;
    }
}
