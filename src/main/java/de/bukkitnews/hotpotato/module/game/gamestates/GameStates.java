package de.bukkitnews.hotpotato.module.game.gamestates;

/**
 * Enum representing the various states of the game.
 *
 * This enum is used to track the current phase or state of the game, allowing the game logic
 * to behave differently depending on whether the game is in the lobby, being played, or ending.
 * It helps manage the game's flow and ensures that actions are only executed when appropriate.
 *
 * - LOBBY: The game is in the lobby phase, where players are waiting for the game to start.
 * - INGAME: The game is in progress, with players actively participating.
 * - ENDING: The game is finishing, and the endgame actions (like scoring, results, etc.) are taking place.
 */
public enum GameStates {

    /**
     * Represents the lobby phase of the game, where players are waiting for the game to begin.
     * During this state, players can join, prepare, or perform other pre-game actions.
     */
    LOBBY,

    /**
     * Represents the phase where the game is actively being played.
     * Players are engaging in the main game mechanics, and the game progresses towards its end.
     */
    INGAME,

    /**
     * Represents the end phase of the game.
     * Players have completed the game, and the endgame logic (like tallying scores or showing results) is executed.
     */
    ENDING;
}