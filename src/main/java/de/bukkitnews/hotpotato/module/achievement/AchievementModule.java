package de.bukkitnews.hotpotato.module.achievement;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.module.CustomModule;
import de.bukkitnews.hotpotato.module.achievement.model.Achievement;
import de.bukkitnews.hotpotato.module.arena.ArenaModule;
import lombok.Getter;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The AchievementModule handles the loading, storing, and retrieval of achievements in the game.
 * It interacts with the database to manage achievement data and provides methods to access cached achievements.
 */
@Getter
public class AchievementModule extends CustomModule {

    @NonNull private final HotPotato hotPotato;

    @NonNull private final Map<Integer, Achievement> achievementCache;

    public AchievementModule(@NonNull HotPotato hotPotato) {
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
        this.hotPotato.getSqlManager().createTable("hotpotato_achieve_objects",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(36), " +
                        "description VARCHAR(128), " +
                        "goal INT DEFAULT 0");

        this.hotPotato.getSqlManager().createTable("hotpotato_achieve_player",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(36), " +
                        "achievement_id INT, " +
                        "progress INT DEFAULT 0, " +
                        "FOREIGN KEY (achievement_id) REFERENCES hotpotato_achieve_objects(id) " +
                        "ON DELETE CASCADE ON UPDATE CASCADE");

        loadAchievements().thenRun(() ->
                this.hotPotato.getLogger().info("Achievements successfully cached."));
    }


    /**
     * Deactivates the module by clearing the achievement cache.
     * This method is called when the module is disabled.
     */
    @Override
    public void deactivate() {
        this.achievementCache.clear();
    }


    /**
     * Asynchronously loads achievements from the database and stores them in the achievementCache.
     * This method is called when the module is activated.
     *
     * @return A CompletableFuture that indicates when the loading process is complete
     */
    private CompletableFuture<Void> loadAchievements() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = hotPotato.getSqlManager().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM hotpotato_achieve_objects");

                achievementCache.clear();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int goal = resultSet.getInt("goal");

                    Achievement achievement = new Achievement(id, name, description, goal);

                    achievementCache.put(id, achievement);
                }

            } catch (SQLException e) {
                hotPotato.getLogger().severe("Fehler beim Laden der Achievements: " + e.getMessage());
            }
        });
    }

    public Achievement getAchievementById(int id) {
        return achievementCache.get(id);
    }
}
