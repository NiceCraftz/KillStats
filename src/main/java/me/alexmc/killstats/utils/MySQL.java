package me.alexmc.killstats.utils;

import lombok.Getter;
import me.alexmc.killstats.KillStats;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class MySQL {
    private final KillStats killStats;
    private String host, port, database, username, password;
    private Connection connection;

    public MySQL(KillStats killStats) {
        this.killStats = killStats;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ConfigurationSection cs = killStats.getConfig().getConfigurationSection("mysql");
        host = cs.getString("host");
        port = cs.getString("port");
        database = cs.getString("database");
        username = cs.getString("username");
        password = cs.getString("password");
    }


    public boolean isConnected() {
        return connection == null;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=UTF-8", username, password);
            createTableIfNonExistent();
            killStats.getLogger().info("Database Successfully Connected!");
        } catch (SQLException e) {
            killStats.getLogger().severe("Couldn't connect to the database, are the credentials right?");
            e.printStackTrace();
        }
    }

    private void createTableIfNonExistent() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "`killstats` (" +
                "  `uuid` varchar(45) NOT NULL," +
                "  `name` varchar(45) NOT NULL," +
                "  `kills` int NOT NULL," +
                "  `deaths` int NOT NULL," +
                "  PRIMARY KEY (`uuid`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
