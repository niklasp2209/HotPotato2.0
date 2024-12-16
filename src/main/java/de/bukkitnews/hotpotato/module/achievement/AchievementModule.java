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
import java.util.concurrent.CompletableFuture;

@Getter
public class AchievementModule extends CustomModule {

    public static ArenaModule instance;
    private final HotPotato hotPotato;

    private final List<Achievement> achievementCache = new ArrayList<>();

    public AchievementModule(@NonNull HotPotato hotPotato) {
        super(hotPotato, "Achievement");

        this.hotPotato = hotPotato;
    }

    @Override
    public void activate() {
        this.hotPotato.getSqlManager().createTable("hotpotato_achieve_objects",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(36), " +
                "description VARCHAR(128), " +
                "goal INT DEFAULT 0, ");

        this.hotPotato.getSqlManager().createTable("hotpotato_achieve_player",
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "uuid VARCHAR(36), " +
                        "achievement_id INT(3), " +
                        "progress INT DEFAULT 0, ");

        loadAchievements().thenRun(() ->
                this.hotPotato.getLogger().info("Achievements successfully cached."));
    }

    @Override
    public void deactivate() {
        this.achievementCache.clear();
    }



    private CompletableFuture<Void> loadAchievements(){
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = hotPotato.getSqlManager().getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM hotpotato_achieve_objects");

                List<Achievement> tempCache = new ArrayList<>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int goal = resultSet.getInt("goal");

                    Achievement achievement = new Achievement(id, name, description, goal);

                    tempCache.add(achievement);
                }

                synchronized (achievementCache) {
                    achievementCache.clear();
                    achievementCache.addAll(tempCache);
                }
            } catch (SQLException e) {
                hotPotato.getLogger().severe("Fehler beim Laden der Achievements: " + e.getMessage());
            }
        });
    }

    public Achievement getAchievementById(int id) {
        synchronized (achievementCache) {
            return achievementCache.stream()
                    .filter(achievement -> achievement.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
}
}
