package de.bukkitnews.hotpotato.module.achievement;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.achievement.model.Achievement;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The AchievementModule handles the loading, storing, and retrieval of achievements in the game.
 * It interacts with the database to manage achievement data and provides methods to access cached achievements.
 */
@Getter
public class AchievementModule extends CustomModule {

    private final @NotNull HotPotato hotPotato;
    private final @NotNull Map<Integer, Achievement> achievementCache;

    public AchievementModule(@NotNull HotPotato hotPotato) {
        super(hotPotato, "Achievement");

        this.achievementCache = new ConcurrentHashMap<>();
        this.hotPotato = hotPotato;
    }

    /**
     * Activates the module by creating the necessary database tables and loading achievements.
     * This method is called when the module is enabled.
     */
    @Override
    public void activate() {
        hotPotato.getSqlManager().createTable("hotpotato_achieve_objects",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(36), " +
                        "description VARCHAR(128), " +
                        "goal INT DEFAULT 0");

        hotPotato.getSqlManager().createTable("hotpotato_achieve_player",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(36), " +
                        "achievement_id INT, " +
                        "progress INT DEFAULT 0, " +
                        "FOREIGN KEY (achievement_id) REFERENCES hotpotato_achieve_objects(id) " +
                        "ON DELETE CASCADE ON UPDATE CASCADE");

        loadAchievements().thenRun(() ->
                hotPotato.getLogger().info("Achievements successfully cached."));
    }


    /**
     * Deactivates the module by clearing the achievement cache.
     * This method is called when the module is disabled.
     */
    @Override
    public void deactivate() {
        achievementCache.clear();
    }


    /**
     * Asynchronously loads achievements from the database and stores them in the achievementCache.
     * This method is called when the module is activated.
     *
     * @return A CompletableFuture that indicates when the loading process is complete
     */
    private @NotNull CompletableFuture<Void> loadAchievements() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = hotPotato.getSqlManager().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM hotpotato_achieve_objects");

                while (resultSet.next()) {
                    Achievement achievement = new Achievement(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("goal"));

                    achievementCache.put(achievement.getId(), achievement);
                }

            } catch (SQLException e) {
                hotPotato.getLogger().severe("Failed to load achievements: " + e.getMessage());
            }
        });
    }
}
