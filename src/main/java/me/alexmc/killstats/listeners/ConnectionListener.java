package me.alexmc.killstats.listeners;

import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;
import me.alexmc.killstats.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ConnectionListener implements Listener {
    private final KillStats killStats;
    private final Connection connection;

    public ConnectionListener(KillStats killStats) {
        this.killStats = killStats;
        this.connection = killStats.getMySQL().getConnection();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String sql = "SELECT * FROM killstats WHERE uuid = ?";

        Bukkit.getScheduler().runTaskAsynchronously(killStats, () -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    User user = new User(rs.getString("name"), UUID.fromString(rs.getString("uuid")));
                    user.setKills(rs.getInt("kills"));
                    user.setDeaths(rs.getInt("deaths"));
                    killStats.getUserList().add(user);
                    return;
                }

                User user = new User(player.getName(), player.getUniqueId());
                killStats.getUserList().add(user);
                Utils.executeInsert(connection, user);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(killStats, () -> {
            Optional<User> user = Utils.getUser(killStats, player.getUniqueId());
            if (!user.isPresent()) {
                return;
            }
            Utils.executeUpdate(killStats.getMySQL().getConnection(), user.get());
            System.out.println("Update effettuato!");
        });
    }
}
