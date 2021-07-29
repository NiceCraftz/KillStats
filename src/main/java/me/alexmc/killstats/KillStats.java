package me.alexmc.killstats;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.alexmc.killstats.commands.DataCommand;
import me.alexmc.killstats.handlers.User;
import me.alexmc.killstats.listeners.ConnectionListener;
import me.alexmc.killstats.listeners.LivingListener;
import me.alexmc.killstats.utils.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class KillStats extends JavaPlugin {
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final List<User> userList = Lists.newArrayList();
    private MySQL mySQL;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        mySQL = new MySQL(this);
        mySQL.connect();

        // INSERT INTO users VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid = uuid
        // sql.setString(1, uuid.toString)
        // ...
        // sql.executeUpdate()
        // UPDATE users SET kills = ? WHERE uuid = ?
        getCommand("data").setExecutor(new DataCommand(this));
        pluginManager.registerEvents(new ConnectionListener(this), this);
        pluginManager.registerEvents(new LivingListener(this), this);
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
    }
}
