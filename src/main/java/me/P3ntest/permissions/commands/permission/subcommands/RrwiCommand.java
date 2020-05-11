package me.P3ntest.permissions.commands.permission.subcommands;

import com.google.gson.internal.bind.SqlDateTypeAdapter;
import me.P3ntest.permissions.Main;
import me.P3ntest.permissions.events.PlayerLoginEventListener;
import me.P3ntest.permissions.mysql.MySqlPermissionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RrwiCommand {

    public static boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3)
            return false;

        try {
            int id = Integer.parseInt(args[1]);

            PreparedStatement statement = Main.getMySqlConnection().getConnection().prepareStatement(
                    "DELETE FROM `player-ranks` WHERE id=?");

            statement.setInt(1, id);

            statement.executeUpdate();

            ((Player) sender).performCommand("permissions rdfu " + args[2]);

            return true;
        } catch (NumberFormatException | SQLException e) {
            return false;
        }
    }

}
