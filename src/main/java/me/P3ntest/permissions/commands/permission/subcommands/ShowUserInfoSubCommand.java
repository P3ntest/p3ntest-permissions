package me.P3ntest.permissions.commands.permission.subcommands;

import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.messages.TextBuilder;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.permissions.P3ntestPermission;
import me.P3ntest.permissions.permissions.P3ntestRank;
import me.P3ntest.permissions.time.TimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowUserInfoSubCommand {

    public static void execute(CommandSender sender, String[] args) {
        String username = args[1];
        String uuid = MySqlPermissionUtils.getUuid(username);
        if (uuid != null) {
            //Header
            sender.sendMessage(Main.prefix + "Info about " + ChatColor.BOLD + ChatColor.GOLD + username);

            //UUID (Click = open namemc profile)
            new TextBuilder(ChatColor.translateAlternateColorCodes('&', "&c&oUUID: &r&a" + uuid))
                    .setHoverEvent(TextBuilder.HoverEventType.SHOW_TEXT, ChatColor.GREEN + "OPEN PROFILE")
                    .setClickEvent(TextBuilder.ClickEventType.OPEN_URL, "https://de.namemc.com/profile/" + uuid)
                    .buildText().sendMessage((Player) sender);

            spacer(sender);

            //Ranks Header
            String rawRanksHeaderAddButton = new TextBuilder(ChatColor.GREEN + "" + ChatColor.BOLD + "[JOIN]")
                    .setClickEvent(TextBuilder.ClickEventType.SUGGEST_TEXT, "/permissions user " + username + " join ")
                    .buildText().getJson();
            new TextBuilder(ChatColor.translateAlternateColorCodes('&',"&6&lRanks &r")
                    + rawRanksHeaderAddButton).buildText().sendMessage((Player) sender);

            //Ranks Display
            for (P3ntestRank userRank : MySqlPermissionUtils.getUserRanks(MySqlPermissionUtils.getUuid(username))) {

                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + userRank.getDisplayName());

            }

            spacer(sender);

            //Permissions Header
            String rawPermissionsHeaderAddButton = new TextBuilder(ChatColor.GREEN + "" + ChatColor.BOLD + "[ADD]")
                    .setClickEvent(TextBuilder.ClickEventType.SUGGEST_TEXT, "/permissions user " + username + " add ")
                    .buildText().getJson();
            new TextBuilder(ChatColor.translateAlternateColorCodes('&',"&6&lPermissions &r")
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

    private static void spacer(CommandSender sender) {
        sender.sendMessage(" ");
    }

}
