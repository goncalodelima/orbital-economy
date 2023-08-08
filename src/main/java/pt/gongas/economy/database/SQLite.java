package pt.gongas.economy.database;

import org.bukkit.Bukkit;
import pt.gongas.economy.EconomyPlugin;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite extends DatabaseConnector {

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + EconomyPlugin.getInstance().getDataFolder() + File.separator + "database.db");
            Bukkit.getConsoleSender().sendMessage("§a[orbital-economy] Connection opened successfully");
            createTable();
        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§c[orbital-economy] Error while opening connection");
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("§a[orbital-economy] Connection closed successfully");
            } catch (SQLException e) {
                System.out.println("§c[orbital-economy] Error while closing connection");
                e.printStackTrace();
            }
        }
    }

    public void createTable(){

        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS economy (NAME VARCHAR(255), CURRENCIES TEXT)");
            statement.executeUpdate();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
