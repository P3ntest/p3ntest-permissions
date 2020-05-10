package me.P3ntest.permissions.events;

import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.permissions.P3ntestRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PlayerLoginEventListener implements Listener {

    public static HashMap<Player, Team> playerTeams = new HashMap<>();

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        handleUsernameCorrection(event.getPlayer());

        PermissionAttachment standardAttachment = event.getPlayer().addAttachment(Main.getInstance());

        addUserPermissions(standardAttachment, event.getPlayer());

        addAllRankPermissions(standardAttachment, event.getPlayer());

        managerPlayerScoreboardTeam(event.getPlayer());

    }

    private void addUserPermissions(PermissionAttachment standardAttachment, Player player) {
        MySqlPermissionUtils.getUserPermissions(player.getUniqueId().toString()).forEach(permission -> {
            if (permission.getEndTimestamp() == -1) {
                standardAttachment.setPermission(permission.getPermission(), true);
            } else {
                if (permission.getEndTimestamp() > System.currentTimeMillis()) {
                    long ticks = (permission.getEndTimestamp() - System.currentTimeMillis()) / 50;
                    PermissionAttachment attachment = player.addAttachment(Main.getInstance(), (int) ticks);
                    attachment.setPermission(permission.getPermission(), true);
                }
            }
        });
    }

    private void addAllRankPermissions(PermissionAttachment standardAttachment, Player player) {
        MySqlPermissionUtils.getUserRanks(player.getUniqueId().toString()).forEach(p3ntestRank -> {
            addRankPermissions(standardAttachment, player, p3ntestRank);
        });
    }

    private void addRankPermissions(PermissionAttachment standardAttachment, Player player, P3ntestRank p3ntestRank) {
        if (p3ntestRank.getEndTimestamp() == -1) {
            MySqlPermissionUtils.getRankPermissions(p3ntestRank.getRankId()).forEach(permission -> {
                standardAttachment.setPermission(permission, true);
            });
        } else {
            if (p3ntestRank.getEndTimestamp() > System.currentTimeMillis()) {
                long ticks = (p3ntestRank.getEndTimestamp() - System.currentTimeMillis()) / 50;
                PermissionAttachment attachment = player.addAttachment(Main.getInstance(), (int) ticks);
                MySqlPermissionUtils.getRankPermissions(p3ntestRank.getRankId()).forEach(permission -> {
                    attachment.setPermission(permission, true);
                });
            }
        }
    }

    private void managerPlayerScoreboardTeam(Player player) {
        P3ntestRank highestRank = getHighestPlayerRank(player);

        Team playerTeam = Main.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(
                String.format("%04d", 1000 - highestRank.getPower()) + player.getName().substring(0, 3)
        );

        playerTeam.addEntry(player.getName());

        playerTeam.setColor(highestRank.getColor());

        playerTeam.setPrefix(highestRank.getPrefix());

        playerTeams.put(player, playerTeam);
    }

    private P3ntestRank getHighestPlayerRank(Player player) {
        P3ntestRank highestRank = MySqlPermissionUtils.getDefaultRank();

        for (P3ntestRank userRank : MySqlPermissionUtils.getUserRanks(player.getUniqueId().toString())) {
            if (userRank.getPower() > highestRank.getPower()) {
                highestRank = userRank;
            }
        }

        return highestRank;
    }

    private void handleUsernameCorrection(Player player) {
        if (checkIfUsernameIsRegistered(player)) {
            updateUsername(player);
        } else {
            registerUsername(player);
        }
    }

    private void updateUsername(Player player) {
        try {
            PreparedStatement statement =
                    Main.getInstance().getMySqlConnection().getConnection().prepareStatement(
                            "UPDATE usernames SET username=? WHERE uuid=?");

            statement.setString(1, player.getName());
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private boolean checkIfUsernameIsRegistered(Player player) {
        try {
            PreparedStatement statement =
                    Main.getInstance().getMySqlConnection().getConnection().prepareStatement(
                            "SELECT username FROM usernames WHERE uuid=?");

            statement.setString(1, player.getUniqueId().toString());

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return true;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    private void registerUsername(Player player) {
        try {
            PreparedStatement statement =
                    Main.getInstance().getMySqlConnection().getConnection().prepareStatement(
                            "INSERT INTO usernames (uuid, username) VALUES (?, ?)");

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}