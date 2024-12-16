package de.bukkitnews.hotpotato.module.achievement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Achievement {

    private final int id;
    private final String name;
    private final String description;
    private final int goal;
}
