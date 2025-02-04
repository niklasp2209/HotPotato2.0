package de.bukkitnews.hotpotato.module.stats;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.player.PlayerModule;
import de.bukkitnews.hotpotato.module.player.model.GamePlayer;
import de.bukkitnews.hotpotato.module.stats.command.StatsCommand;
import de.bukkitnews.hotpotato.module.stats.model.PlayerStats;
import org.jetbrains.annotations.NotNull;

public class StatsModule extends CustomModule {

    public StatsModule(@NotNull HotPotato hotPotato) {
        super(hotPotato, "Stats");
    }

    @Override
    public void activate() {
        getCommandExecutors().put("stats", new StatsCommand(this));
    }

    @Override
    public void deactivate() {

    }

    /**
     * Updates a player's win count.
     *
     * @param gamePlayer The player whose win count is to be updated.
     */
    public void incrementWins(@NotNull GamePlayer gamePlayer) {
        PlayerStats stats = gamePlayer.getStats();
        stats.incrementWins();
        savePlayerStats(gamePlayer);
    }

    /**
     * Updates a player's games played count.
     *
     * @param gamePlayer The player whose games played count is to be updated.
     */
    public void incrementGamesPlayed(@NotNull GamePlayer gamePlayer) {
        PlayerStats stats = gamePlayer.getStats();
        stats.incrementGamesPlayed();
        savePlayerStats(gamePlayer);
    }

    /**
     * Updates a player's playtime.
     *
     * @param gamePlayer The player whose playtime is to be updated.
     * @param amount     The amount of playtime to add (in minutes).
     */
    public void incrementPlaytime(@NotNull GamePlayer gamePlayer, long amount) {
        PlayerStats stats = gamePlayer.getStats();
        stats.incrementPlaytime(amount);
        savePlayerStats(gamePlayer);
    }

    /**
     * Saves the player's updated stats to Redis and SQL.
     *
     * @param gamePlayer The player whose stats should be saved.
     */
    private void savePlayerStats(@NotNull GamePlayer gamePlayer) {
        getHotPotato().getModuleManager().getModule(PlayerModule.class).ifPresent(playerModule ->
                playerModule.getGamePlayerManager().savePlayer(gamePlayer)
        );
    }

    /**
     * Retrieves the player's current statistics.
     *
     * @param gamePlayer The player whose stats are to be retrieved.
     * @return A formatted string containing the player's stats.
     */
    public @NotNull String getStats(@NotNull GamePlayer gamePlayer) {
        PlayerStats stats = gamePlayer.getStats();
        return String.format("Wins: %d | Games Played: %d | Playtime: %d minutes",
                stats.getWins(), stats.getGamesPlayed(), stats.getPlaytime());
    }
}
