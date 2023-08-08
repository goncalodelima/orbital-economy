package pt.gongas.economy;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pt.gongas.economy.command.CashCommand;
import pt.gongas.economy.command.MoneyCommand;
import pt.gongas.economy.database.DatabaseConnector;
import pt.gongas.economy.database.MariaDB;
import pt.gongas.economy.database.SQLite;
import pt.gongas.economy.listener.PlayerListener;
import pt.gongas.economy.user.manager.UserManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class EconomyPlugin extends JavaPlugin {

    @Getter
    private UserManager userManager;
    @Getter
    private DatabaseConnector dataBase;
    @Getter
    public static EconomyPlugin instance;
    @Getter
    private final Map<Player, Long> earnCooldown = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("MariaDB.enable")) dataBase = new MariaDB(getConfig().getString("MariaDB.hostname"), getConfig().getInt("MariaDB.port"), getConfig().getString("MariaDB.database"), getConfig().getString("MariaDB.username"), getConfig().getString("MariaDB.password"));
        else dataBase = new SQLite();

        dataBase.connect();

        userManager = new UserManager();
        try {
            userManager.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        register();

    }

    @Override
    public void onDisable() {
        userManager.saveAll();
        dataBase.close();
    }

    public void register() {
        registerCommand();
        registerListener();
    }

    public void registerCommand() {
        Objects.requireNonNull(Bukkit.getPluginCommand("money")).setExecutor(new MoneyCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("cash")).setExecutor(new CashCommand());
    }

    public void registerListener() { Bukkit.getPluginManager().registerEvents(new PlayerListener(), this); }


}
