package me.P3ntest.permissions.commands;

import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.messages.TextBuilder;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.permissions.P3ntestPermission;
import me.P3ntest.permissions.permissions.P3ntestRank;
import me.P3ntest.permissions.time.InvalidDateException;
import me.P3ntest.permissions.time.TimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.soap.Text;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class PermissionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pp.cmd"))
            return true;

        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("user")) {
                if (args.length == 2) {
                    showPlayerInfo(sender, args[1]);
                    return true;
                } else if (args.length > 3) {
                    if (args[2].equalsIgnoreCase("add")) {
                        try {
                            addUserPermission(args[1], args[3], args.length > 4 ? Arrays.copyOfRange(args, 4, args.length) : null);
                        } catch (InvalidDateException exception) {
                            sender.sendMessage(ChatColor.RED + exception.getError());
                        }
                        return true;
                    }
                }
            }
        }

        showHelp(sender);
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(Main.prefix + "Help:");
        sender.sendMessage("/perms user <user>");
        sender.sendMessage("/perms user <user> add <permission> [time]");
        sender.sendMessage("/perms user <user> remove <permission>");
        sender.sendMessage("/perms user <user> join <rank> [time]");
        sender.sendMessage("/perms user <user> leave <rank>");
        sender.sendMessage("/perms rank <rank>");
        sender.sendMessage("/perms user <rank> add <permission>");
        sender.sendMessage("/perms user <rank> remove <permission>");
        sender.sendMessage("/perms rank list");
    }

    private void addUserPermission(String username, String permission, String[] time) throws InvalidDateException {
        long until = -1;
        if (time != null && time.length > 0) {
            until = TimeFormatter.formattedToMilliseconds(time) + System.currentTimeMillis();
        }

        String uuid = MySqlPermissionUtils.getUuid(username);
        try {
            PreparedStatement statement =
                    Main.getMySqlConnection().getConnection().prepareStatement(
                            "INSERT INTO `user-perms` (`uuid`, `permission`, `until`) VALUES (?, ?, ?)");

            statement.setString(1, MySqlPermissionUtils.getUuid(username));
            statement.setString(2, permission);
            statement.setLong(3, until);

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void showPlayerInfo(CommandSender sender, String username) {
        String uuid = MySqlPermissionUtils.getUuid(username);
        if (uuid != null) {
            //Header
            sender.sendMessage(Main.prefix + "Info about " + ChatColor.BOLD + ChatColor.GOLD + username);

            //UUID (Click = open namemc profile)
            new TextBuilder(ChatColor.translateAlternateColorCodes('&', "&c&oUUID: &r&a" + uuid))
                    .setHoverEvent(TextBuilder.HoverEventType.SHOW_TEXT, ChatColor.GREEN + "OPEN PROFILE")
                        .setClickEvent(TextBuilder.ClickEventType.OPEN_URL, "https://de.namemc.com/profile/" + uuid)
                        .buildText().sendMessage((Player) sender);

            //Spacer
            sender.sendMessage(" ");

            //Ranks Header
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6&lRanks"));

            //Ranks Display
            for (P3ntestRank userRank : MySqlPermissionUtils.getUserRanks(MySqlPermissionUtils.getUuid(username))) {

                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + userRank.getDisplayName());

            }

            //Spacer
            sender.sendMessage(" ");

            //Permissions Header
            String rawPermissionsHeaderAddButton = new TextBuilder(ChatColor.GREEN + "" + ChatColor.BOLD + "[ADD]")
                    .setClickEvent(TextBuilder.ClickEventType.SUGGEST_TEXT, "/permissions user " + username + " add ")
                    .buildText().getJson();
            new TextBuilder(ChatColor.translateAlternateColorCodes('&',"&6&lPermissions")
                    + rawPermissionsHeaderAddButton).buildText().sendMessage((Player) sender);


            //Permissions
            for (P3ntestPermission userPermission : MySqlPermissionUtils.getUserPermissions(MySqlPermissionUtils.getUuid(username))) {
                if (userPermission.getEndTimestamp() == -1 || userPermission.getEndTimestamp() >= System.currentTimeMillis()) {
                    String text = ChatColor.GRAY + " - " + ChatColor.RESET + userPermission.getPermission();
                    if (userPermission.getEndTimestamp() >= System.currentTimeMillis()) {
                        text += ChatColor.GRAY + " (" + ChatColor.GREEN +
                                TimeFormatter.formatMilliseconds(userPermission.getEndTimestamp() - System.currentTimeMillis()) +
                                ChatColor.RESET + ChatColor.GRAY + ")";
                    }
                    sender.sendMessage(text);
                }
            }
        } else {
            sender.sendMessage(Main.prefix + "No information.");
        }
    }
}
