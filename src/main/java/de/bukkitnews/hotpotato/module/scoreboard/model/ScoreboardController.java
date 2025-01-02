package de.bukkitnews.hotpotato.module.scoreboard.model;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardController {

    private final HotPotato hotPotato;
    private final List<ScoreboardElement> elements;

    public ScoreboardController(@NonNull HotPotato hotPotato) {
        this.hotPotato = hotPotato;
        this.elements = new ArrayList<>();
    }

    public void registerElement(ScoreboardElement scoreboardElement){
        elements.add(scoreboardElement);
    }

    public void applyScoreboard(GamePlayer gamePlayer){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("hotpotato", Criteria.DUMMY, "§lHot Potato");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int scoreIndex = elements.size() * 2 + (elements.size() -1);
        for(ScoreboardElement scoreboardElement : elements){
            scoreboardElement.applyToScoreboard(objective, scoreIndex);
            scoreIndex--;
        }

        Bukkit.getPlayer(UUID.fromString(gamePlayer.getUuid())).setScoreboard(scoreboard);
    }

    public void updateScoreboard(GamePlayer gamePlayer){
        elements.forEach(element -> element.update(gamePlayer));
    }
}
