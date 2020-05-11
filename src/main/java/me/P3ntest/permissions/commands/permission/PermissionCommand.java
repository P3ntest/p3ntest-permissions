package me.P3ntest.permissions.commands.permission;

import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.commands.permission.subcommands.*;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import me.P3ntest.permissions.time.InvalidDateException;
import me.P3ntest.permissions.time.TimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                return UserSubCommand.execute(sender, args);
            } else if (args[0].equalsIgnoreCase("rpwi")) {
                if (RpwiCommand.execute(sender, args))
                    return true;
            } else if (args[0].equalsIgnoreCase("rdfu")) {
                if (RdfuCommand.execute(sender, args))
                    return true;
            } else if (args[0].equalsIgnoreCase("rrwi")) {
                if (RrwiCommand.execute(sender, args))
                    return true;
            } else if (args[0].equalsIgnoreCase("arwi")) {
                if (ArwiCommand.execute(sender, args))
                    return true;
            }
        }

        ShowHelp.showHelp(sender);
        return true;
    }
}
