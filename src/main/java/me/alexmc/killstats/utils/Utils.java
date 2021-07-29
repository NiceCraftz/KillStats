package me.alexmc.killstats.utils;

import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Utils {

    public static ResultSet getUserSet(Connection connection, UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM killstats where uuid = ?");
        preparedStatement.setString(1, uuid.toString());
        return preparedStatement.executeQuery();
    }

    public static Optional<User> getUser(KillStats killStats, UUID uuid) {
        for (User user : killStats.getUserList()) {
            if (user.getUuid().equals(uuid))
                return Optional.of(user);
        }
        return Optional.empty();
    }
}
