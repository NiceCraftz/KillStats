package me.alexmc.killstats.listeners;

import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;
import me.alexmc.killstats.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class LivingListener implements Listener {
    private final KillStats killStats;
    private final Connection connection;

    public LivingListener(KillStats killStats) {
        this.killStats = killStats;
        connection = killStats.getMySQL().getConnection();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        try {
            Player victim = e.getEntity();
            Optional<User> victimOptional = Utils.getUser(killStats, victim.getUniqueId());
            if (victimOptional.isPresent()) {
                victimOptional.get().addDeath();
                victimOptional.get().executeUpdate(connection);
            }

            if (victim.getKiller() == null)
                return;

            Player killer = victim.getKiller();
            Optional<User> killerOptional = Utils.getUser(killStats, killer.getUniqueId());
            if (killerOptional.isPresent()) {
                killerOptional.get().addKill();
                killerOptional.get().executeUpdate(connection);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
