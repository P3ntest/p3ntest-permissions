package me.P3ntest.permissions.mysql;

import com.google.gson.internal.$Gson$Preconditions;
import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.permissions.P3ntestPermission;
import me.P3ntest.permissions.permissions.P3ntestRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlPermissionUtils {

    public static List<P3ntestPermission> getUserPermissions(String uuid) {
        List<P3ntestPermission> permissions = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT `id`, `permission`, `until` FROM `user-perms` WHERE `uuid` = ?");

            statement.setString(1, uuid);

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                P3ntestPermission perm = new P3ntestPermission(set.getString("permission"), set.getLong("until"));
                perm.setPermissionId(set.getInt("id"));
                permissions.add(perm);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return permissions;
    }

    public static P3ntestRank getDefaultRank() {
        int lowestPower = Integer.MAX_VALUE;

        P3ntestRank rank = null;

        for (P3ntestRank p3ntestRank : getAllRanks()) {
            if (p3ntestRank.getPower() < lowestPower) {
                lowestPower = p3ntestRank.getPower();
                rank = p3ntestRank;
            }
        }

        return rank;
    }

    public static List<P3ntestRank> getUserRanks(String uuid) {
        List<P3ntestRank> ranks = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT `rankid`, `until` FROM `player-ranks` WHERE `uuid` = ?");

            statement.setString(1, uuid);

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                P3ntestRank rank = getRank(set.getInt("rankid"));
                rank.setEndTimestamp(set.getLong("until"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return ranks;
    }

    public static P3ntestRank getRank(int rankId) {
        P3ntestRank rank = null;
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT `power`, `color`, `prefix`, `displayname` FROM `ranks` WHERE id=?");

            statement.setInt(1, rankId);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                rank = new P3ntestRank(
                        rankId,
                        -1,
                        set.getInt("power"),
                        ChatColor.valueOf(set.getString("color").toUpperCase()),
                        set.getString("prefix"),
                        set.getString("displayname"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return rank;
    }

    public static List<P3ntestRank> getAllRanks() {
        List<P3ntestRank> ranks = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT `id`, `power`, `color`, `prefix`, `displayname` FROM `ranks`");

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                ranks.add(new P3ntestRank(
                        set.getInt("id"),
                        -1,
                        set.getInt("power"),
                        ChatColor.valueOf(set.getString("color").toUpperCase()),
                        set.getString("prefix"),
                        set.getString("displayname")));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return ranks;
    }

    public static List<String> getRankPermissions(int rankId) {
        List<String> permissions = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT `permission` FROM `rank-permissions` WHERE `rank` = ?");

            statement.setInt(1, rankId);

            ResultSet set = statement.executeQuery();

            while (set.next()) {
                permissions.add(set.getString(1));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return permissions;
    }

    public static String getUuid(String username) {
        List<P3ntestPermission> permissions = new ArrayList<>();
        try {
            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "SELECT uuid FROM usernames WHERE UPPER(username) LIKE UPPER(?)");

            statement.setString(1, username);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return set.getString(1);
            }

            return null;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
