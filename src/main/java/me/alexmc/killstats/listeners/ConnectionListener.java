package me.alexmc.killstats.listeners;

import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;
import me.alexmc.killstats.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ConnectionListener implements Listener {
    private final KillStats killStats;
    private final Connection connection;

    public ConnectionListener(KillStats killStats) {
        this.killStats = killStats;
        connection = killStats.getMySQL().getConnection();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        try {
            ResultSet rs = Utils.getUserSet(connection, player.getUniqueId());
            while (rs.next()) {
                User user = new User(rs.getString("name"), UUID.fromString(rs.getString("uuid")));
                user.setKills(rs.getInt("kills"));
                user.setDeaths(rs.getInt("deaths"));
                killStats.getUserList().add(user);
                return;
            }
            User user = new User(player.getName(), player.getUniqueId());
            killStats.getUserList().add(user);
            user.executeInsert(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Optional<User> user = Utils.getUser(killStats, player.getUniqueId());
        if (!user.isPresent()) {
            player.sendMessage("The player was not present in the list, there was a reload?");
            return;
        }

        try {
            user.get().executeUpdate(connection);
            System.out.println("Update effettuato!");
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }
}
