package de.bukkitnews.hotpotato.module.achievement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an achievement in the game.
 * This class is used to store information about a specific achievement, such as its ID, name, description, and the goal to achieve.
 */
@Getter
@AllArgsConstructor
public class Achievement {

    private final int id;
    private final String name;
    private final String description;
    private final int goal;
}
