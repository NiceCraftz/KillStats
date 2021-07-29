package me.alexmc.killstats.utils;

import lombok.Getter;
import me.alexmc.killstats.KillStats;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class MySQL {
    private String host, port, database, username, password;
    private Connection connection;

    public MySQL(KillStats killStats) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
