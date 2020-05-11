package me.P3ntest.permissions.commands.permission.subcommands;

import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.permissions.P3ntestPermission;
import me.P3ntest.permissions.permissions.P3ntestRank;
import me.P3ntest.permissions.time.TimeFormatter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.AccessibleObject;

public class ShowUserInfoSubCommand {

    public static void execute(CommandSender sender, String[] args) {
        String username = args[1];
        String uuid = MySqlPermissionUtils.getUuid(username);
        if (uuid != null) {
            //Header
            header(sender, username);

            //UUID (Click = copy to clipboard)
            showUuid((Player) sender, uuid);

            spacer(sender);

            //Ranks Header
            ranksHeader((Player) sender, username);

            //Ranks Display
            for (P3ntestRank userRank : MySqlPermissionUtils.getUserRanks(MySqlPermissionUtils.getUuid(username))) {

                sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + userRank.getDisplayName());

            }

            spacer(sender);

            //Permissions Header
            permissionsHeader((Player) sender, username);


            //Permissions
            for (P3ntestPermission userPermission : MySqlPermissionUtils.getUserPermissions(MySqlPermissionUtils.getUuid(username))) {
                permission(sender, userPermission, username);
            }
        } else {
            sender.sendMessage(Main.prefix + "No information.");
        }
    }

    private static void permission(CommandSender sender, P3ntestPermission userPermission, String username) {
        if (userPermission.getEndTimestamp() == -1 || userPermission.getEndTimestamp() >= System.currentTimeMillis()) {
            TextComponent dash = new TextComponent(" - ");
            dash.setColor(net.md_5.bungee.api.ChatColor.GRAY);

            TextComponent permission = new TextComponent(userPermission.getPermission());
            permission.setColor(net.md_5.bungee.api.ChatColor.WHITE);
            permission.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click to copy").create()));
            permission.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, userPermission.getPermission()));

            dash.addExtra(permission);

            if (userPermission.getEndTimestamp() >= System.currentTimeMillis()) {
                TextComponent timeRemaining = new TextComponent(" " +
                        TimeFormatter.formatMilliseconds(userPermission.getEndTimestamp() - System.currentTimeMillis()));
                timeRemaining.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                dash.addExtra(timeRemaining);
            }

            TextComponent removeButton = new TextComponent(" [REMOVE]");
            removeButton.setColor(net.md_5.bungee.api.ChatColor.RED);
            removeButton.setBold(true);
            removeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/permissions rpwi " + userPermission.getPermissionId() + " " + username));
            dash.addExtra(removeButton);

            ((Player) sender).spigot().sendMessage(dash);
        }
    }

    private static void permissionsHeader(Player sender, String username) {
        TextComponent header = new TextComponent("Player Permissions: ");
        header.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

        TextComponent addButton = new TextComponent("[ADD]");
        addButton.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        addButton.setBold(true);
        addButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "/permissions user " + username + " add "));
        addButton.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to add permission").create()));

        header.addExtra(addButton);

        sender.spigot().sendMessage(header);
    }

    private static void ranksHeader(Player sender, String username) {
        TextComponent header = new TextComponent("Player Ranks: ");
        header.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

        TextComponent joinButton = new TextComponent("[JOIN]");
        joinButton.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        joinButton.setBold(true);
        joinButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/permissions rdfu " + username));
        joinButton.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to add rank").create()));

        header.addExtra(joinButton);

        sender.spigot().sendMessage(header);
    }

    private static void header(CommandSender sender, String username) {
        TextComponent infoHeader = new TextComponent("Info about: ");
        infoHeader.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

        TextComponent nameComponent = new TextComponent(username);
        nameComponent.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://de.namemc.com/profile/" + username));
        nameComponent.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to open NAMEMC profile").create()));

        infoHeader.addExtra(nameComponent);
        ((Player) sender).spigot().sendMessage(infoHeader);
    }

    private static void showUuid(Player player, String uuid) {
        TextComponent rawUuidTextComponent = new TextComponent(
                ChatColor.translateAlternateColorCodes('&', "UUID: "));
        rawUuidTextComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
        rawUuidTextComponent.setBold(true);

        TextComponent rawUuidTextComponentUuid = new TextComponent(uuid);
        rawUuidTextComponentUuid.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid));

        rawUuidTextComponentUuid.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Click to copy to clipboard").create()));
        rawUuidTextComponent.addExtra(rawUuidTextComponentUuid);

        player.spigot().sendMessage(rawUuidTextComponent);
    }

    private static void spacer(CommandSender sender) {
        sender.sendMessage(" ");
    }

}
