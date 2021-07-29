package me.alexmc.killstats.commands;

import lombok.RequiredArgsConstructor;
import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.handlers.User;
import me.alexmc.killstats.utils.ColorAPI;
import me.alexmc.killstats.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class DataCommand implements CommandExecutor {
    private final KillStats killStats;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.not-player")));
            return true;
        }

        Player player = (Player) sender;
        Optional<User> userOptional = Utils.getUser(killStats, player.getUniqueId());
        if (!userOptional.isPresent()) {
            player.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.no-data")));
            return true;
        }

        for (String s : killStats.getConfig().getStringList("stats")) {
            s = s.replace("%player%", player.getName())
                    .replace("%kills%", userOptional.get().getKills() + "")
                    .replace("%deaths%", userOptional.get().getDeaths() + "");
            sender.sendMessage(ColorAPI.color(s));
        }
        return true;
    }
}
