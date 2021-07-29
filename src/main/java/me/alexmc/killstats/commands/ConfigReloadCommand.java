package me.alexmc.killstats.commands;

import lombok.RequiredArgsConstructor;
import me.alexmc.killstats.KillStats;
import me.alexmc.killstats.utils.ColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ConfigReloadCommand implements CommandExecutor {
    private final KillStats killStats;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("killstats.reload")) {
            sender.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.no-permission")));
            return true;
        }

        killStats.reloadConfig();
        sender.sendMessage(ColorAPI.color(killStats.getConfig().getString("messages.reload")));
        return true;
    }
}
