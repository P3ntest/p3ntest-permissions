package me.P3ntest.permissions;

import me.P3ntest.permissions.commands.PermissionCommand;
import me.P3ntest.permissions.events.PlayerLoginEventListener;
import me.P3ntest.permissions.events.PlayerQuitEventListener;
import me.P3ntest.permissions.mysql.MySqlConnection;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.Collection;

public class Main extends JavaPlugin {

    public static final String prefix = ChatColor.translateAlternateColorCodes('&', "&6P3ntestPerms &7| &a");

    private static MySqlConnection mySqlConnection;

    public static MySqlConnection getMySqlConnection() {return mySqlConnection;}

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new PlayerLoginEventListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(), this);

        this.getServer().getPluginCommand("perms").setExecutor(new PermissionCommand());

        unregisterAllTeams();

        instance = this;

        mySqlConnection = new MySqlConnection(getConfig(), "mysql");
    }

    private void unregisterAllTeams() {
        Collection<Team> teams = this.getServer().getScoreboardManager().getMainScoreboard().getTeams();
        teams.forEach(team -> team.unregister());
    }
}
