package me.alexmc.killstats.handlers;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class User {
    private final String name;
    private final UUID uuid;

    private int kills = 0;
    private int deaths = 0;

    public User(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    private String getUpdateString() {
        return "UPDATE `plugins`.`killstats` SET kills = ?, deaths = ? WHERE uuid = ?";
    }

    private String getInsertString() {
        return "INSERT INTO `plugins`.`killstats` (uuid, name, kills, deaths) VALUES (?,?,?,?)";
    }


    // UPDATE killstats SET kills = ? WHERE name = ?
    // setInt(1, 100) setString(2, "nicecoglione")

    public void executeUpdate(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getUpdateString());
        preparedStatement.setInt(1, kills);
        preparedStatement.setInt(2, deaths);
        preparedStatement.setString(3, uuid.toString());
        preparedStatement.execute();
    }


    public void executeInsert(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getInsertString());
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, name);
        preparedStatement.setInt(3, kills);
        preparedStatement.setInt(4, deaths);
        preparedStatement.execute();
    }
}
