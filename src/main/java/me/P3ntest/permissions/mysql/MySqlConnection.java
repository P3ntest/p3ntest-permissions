package me.P3ntest.permissions.mysql;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;

public class MySqlConnection {

    private Connection connection;

    public MySqlConnection(FileConfiguration config, String path) {
        int port = config.getInt(path + ".port");
        String user = config.getString(path + ".user");
        String password = config.getString(path + ".password");
        String database = config.getString(path + ".database");

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:" + port + "/" + database, user, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `minecraft`.`user-perms` " +
                                    "( `id` INT NOT NULL AUTO_INCREMENT , " +
                                    "`uuid` VARCHAR(64) NOT NULL , `permission` VARCHAR(128) NOT NULL , " +
                                    "`until` BIGINT NOT NULL ," +
                                    " PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `minecraft`.`usernames` " +
                                    "( `uuid` VARCHAR(128) NOT NULL , " +
                                    "`username` VARCHAR(16) NOT NULL ) " +
                                    "ENGINE = InnoDB;");
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `minecraft`.`ranks` " +
                                    "( `id` INT NOT NULL AUTO_INCREMENT , " +
                                    "`humanid` VARCHAR(64) NOT NULL , " +
                                    "`displayname` VARCHAR(64) NOT NULL , " +
                                    "`prefix` VARCHAR(16) NULL ," +
                                    "`color` VARCHAR(16) NOT NULL ," +
                                    "`power` INT NOT NULL ," +
                                    " PRIMARY KEY (`id`)) ENGINE = InnoDB;");
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `minecraft`.`player-ranks` " +
                                    "( `id` INT NOT NULL AUTO_INCREMENT , " +
                                    "`uuid` VARCHAR(128) NOT NULL , " +
                                    "`rankid` INT NOT NULL , " +
                                    "`until` BIGINT NOT NULL , " +
                                    "PRIMARY KEY (`id`)) " +
                                    "ENGINE = InnoDB;");
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        try {
            PreparedStatement statement =
                    connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `minecraft`.`rank-permissions` " +
                                    "( `rankid` INT NOT NULL , " +
                                    "`permission` VARCHAR(128) NOT NULL ) " +
                                    "ENGINE = InnoDB;");
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
