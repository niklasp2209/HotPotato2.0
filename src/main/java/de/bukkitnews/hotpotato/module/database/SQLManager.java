package de.bukkitnews.hotpotato.module.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.bukkitnews.hotpotato.HotPotato;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * SQLManager is responsible for managing database connections and executing SQL queries using HikariCP.
 * It handles establishing a connection to a MySQL database, executing asynchronous queries, and creating necessary database tables.
 */
public class SQLManager {

    private final HotPotato hotPotato;
    private final HikariDataSource hikariDataSource;

    /**
     * Constructor for initializing the SQLManager and establishing a connection to the MySQL database.
     * It reads the configuration details from the provided ConfigManager and configures the HikariCP DataSource.
     * It also creates a table named "hotpotato" in the database if it doesn't already exist.
     *
     * @param configManager The ConfigManager instance to load database configuration settings
     */
    public SQLManager(@NonNull HotPotato hotPotato, @NonNull ConfigManager configManager) {
        this.hotPotato = hotPotato;

        FileConfiguration fileConfiguration = configManager.getConfig();
        HikariConfig hikariConfig = new HikariConfig();

        // Set the JDBC URL, username, password, and pool settings based on the configuration
        hikariConfig.setJdbcUrl("jdbc:mysql://" + fileConfiguration.getString("mysql.host") + ":" +
                fileConfiguration.getInt("mysql.port") + "/" + fileConfiguration.getString("mysql.database"));
        hikariConfig.setUsername(fileConfiguration.getString("mysql.username"));
        hikariConfig.setPassword(fileConfiguration.getString("mysql.password"));

        // Set connection pool properties from the configuration
        hikariConfig.setMaximumPoolSize(fileConfiguration.getInt("mysql.pool.maximumPoolSize", 10));
        hikariConfig.setMinimumIdle(fileConfiguration.getInt("mysql.pool.minimumIdle", 2));
        hikariConfig.setIdleTimeout(fileConfiguration.getLong("mysql.pool.idleTimeout", 10000));
        hikariConfig.setMaxLifetime(fileConfiguration.getLong("mysql.pool.maxLifetime", 1800000));

        // Initialize the HikariDataSource with the configuration
        this.hikariDataSource = new HikariDataSource(hikariConfig);

        // Log successful database connection
        this.hotPotato.getLogger().info("Successfully connected to the database.");

        // Create the 'hotpotato' table if it doesn't exist
        createTable("hotpotato", "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(36), " +
                "name VARCHAR(16), " +
                "wins INT DEFAULT 0, " +
                "playtime BIGINT DEFAULT 0, " +
                "gamesPlayed INT DEFAULT 0");
    }

    /**
     * Executes an SQL query asynchronously, making sure it doesn't block the main thread.
     *
     * @param query The SQL query to execute
     * @return A CompletableFuture representing the asynchronous operation
     */
    public CompletableFuture<Void> executeAsync(@NonNull String query) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                connection.createStatement().execute(query);
            } catch (SQLException e) {
                this.hotPotato.getLogger().severe("Error executing query: " + query);
            }
        });
    }

    /**
     * Retrieves a connection from the HikariDataSource.
     *
     * @return A database connection
     * @throws SQLException If a connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }

    /**
     * Closes the HikariDataSource, releasing all database connections.
     */
    public void close() {
        if (this.hikariDataSource != null && !this.hikariDataSource.isClosed()) {
            this.hikariDataSource.close();
        }
    }

    /**
     * Creates a table in the database if it does not already exist.
     * This method executes an asynchronous query to create the table with the specified schema.
     *
     * @param tableName The name of the table to create
     * @param tableSchema The schema definition of the table
     */
    public void createTable(@NonNull String tableName, @NonNull String tableSchema) {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ");";

        executeAsync(query).thenRun(() ->
                this.hotPotato.getLogger().info("Table '" + tableName + "' has been created or already exists.")
        ).exceptionally(e -> {
            this.hotPotato.getLogger().severe("Error creating table: " + e.getMessage());
            return null;
        });
    }
}