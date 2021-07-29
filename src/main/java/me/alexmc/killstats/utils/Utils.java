package me.alexmc.killstats.utils;

import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Utils {

    public static Optional<User> getUser(KillStats killStats, UUID uuid) {
        for (User user : killStats.getUserList()) {
            if (user.getUuid().equals(uuid))
                return Optional.of(user);
        }
        return Optional.empty();
    }


    public static void executeUpdate(Connection connection, User user) {
        String sql = "UPDATE `plugins`.`killstats` SET kills = ?, deaths = ? WHERE uuid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getKills());
            preparedStatement.setInt(2, user.getDeaths());
            preparedStatement.setString(3, user.getUuid().toString());
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void executeInsert(Connection connection, User user) {
        String sql = "INSERT INTO `plugins`.`killstats` (uuid, name, kills, deaths) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, user.getKills());
            preparedStatement.setInt(4, user.getDeaths());
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
