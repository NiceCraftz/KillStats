package me.alexmc.killstats.commands;

import lombok.RequiredArgsConstructor;
import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.utils.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public class LeaderBoardCommand implements CommandExecutor {
    private final KillStats killStats;
    int count, i = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        count = 0;
        i = 0;

        Bukkit.getScheduler().runTaskAsynchronously(killStats, () -> {
            try {
                count = Integer.parseInt(args[0]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                count = 3;
            }

            String sql = "SELECT * FROM killstats ORDER BY kills DESC";
            try {
                Statement statement = killStats.getMySQL().getConnection().createStatement();
                ResultSet rs = statement.executeQuery(sql);
                sender.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.leaderboard-kills")));
                while (rs.next()) {
                    if (i == count) {
                        return;
                    }
                    i++;
                    sender.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.leaderboard-format"))
                            .replace("%id%", i + "")
                            .replace("%player%", rs.getString("name"))
                            .replace("%kills%", rs.getInt("kills") + ""));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
        return true;
    }

}
