package me.P3ntest.permissions.commands.permission.subcommands;

import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.permissions.P3ntestRank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  RdfuCommand {

    public static boolean execute(CommandSender sender, String[] args) {
        String uuid = MySqlPermissionUtils.getUuid(args[1]);

        if (uuid == null)
            return false;

        header(sender, args[1]);

        List<P3ntestRank> allRanks = MySqlPermissionUtils.getAllRanks();
        List<P3ntestRank> userRanks = MySqlPermissionUtils.getUserRanks(uuid);

        HashMap<Integer, P3ntestRank> userRankIds = new HashMap<>();
        for (P3ntestRank userRank : userRanks) {
            userRankIds.put(userRank.getRankId(), userRank);
        }

        for (P3ntestRank rank : allRanks) {
            TextComponent dash = new TextComponent(" - ");
            dash.setColor(ChatColor.GRAY);

            TextComponent rankComponent = new TextComponent(rank.getDisplayName());

            dash.addExtra(rankComponent);

            if (userRankIds.containsKey(rank.getRankId())) {
                TextComponent removeButton = new TextComponent(" [REMOVE]");
                removeButton.setBold(true);
                removeButton.setColor(ChatColor.RED);
                removeButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to remove rank from player")
                                .color(ChatColor.GOLD.GREEN).create()));
                removeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/permissions rrwi " + userRankIds.get(rank.getRankId()).getAssignedId() + " " + args[1]));
                dash.addExtra(removeButton);
            } else {
                TextComponent addButton = new TextComponent(" [ADD]");
                addButton.setBold(true);
                addButton.setColor(ChatColor.GREEN);
                addButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to add rank to player")
                                .color(ChatColor.GOLD.GREEN).create()));
                addButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/permissions user " + args[1] + " join " + rank.getHumanId()));
                dash.addExtra(addButton);
            }

            ((Player) sender).spigot().sendMessage(dash);
        }

        return true;

    }

    static void header (CommandSender sender, String username) {
        TextComponent header = new TextComponent("Showing all ranks for ");
        header.setColor(ChatColor.YELLOW);


        TextComponent name = new TextComponent(username);
        name.setColor(ChatColor.GOLD);
        name.setBold(true);
        name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click to show Info about user!").color(ChatColor.GREEN).create()));
        name.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "permissions user " + username));

        header.addExtra(name);

        ((Player) sender).spigot().sendMessage(header);
    }

}
