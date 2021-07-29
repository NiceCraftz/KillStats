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
            sender.sendMessage(ColorAPI.color("&cDevi essere un giocatore per eseguire questo comando!"));
            return true;
        }

        Player player = (Player) sender;
        Optional<User> userOptional = Utils.getUser(killStats, player.getUniqueId());
        if (!userOptional.isPresent()) {
            player.sendMessage(ColorAPI.color("&cNon sono disponibili dati per te, perfavore contatta un amministratore!"));
            return true; 
        }

        player.sendMessage(ColorAPI.color("&bUUID: " + userOptional.get().getUuid()));
        player.sendMessage(ColorAPI.color("&bName: " + userOptional.get().getName()));
        player.sendMessage(ColorAPI.color("&aKills: " + userOptional.get().getKills()));
        player.sendMessage(ColorAPI.color("&cDeaths: " + userOptional.get().getDeaths()));
        return true;
    }
}
