package pt.gongas.economy.user.manager;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import pt.gongas.economy.currency.Currency;
import pt.gongas.economy.currency.type.CurrencyType;
import pt.gongas.economy.database.DatabaseConnector;
import pt.gongas.economy.user.User;
import pt.gongas.economy.utils.Formatter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    @SneakyThrows
    public void load() throws SQLException {

        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement("SELECT * FROM economy");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            User user = new User(result.getString("NAME"));

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result.getString("CURRENCIES"));
            for (CurrencyType value : CurrencyType.values())
                user.loadCurrency(value, Double.parseDouble(obj.get(value.name()).toString()));
        }

        statement.close();
        Bukkit.getConsoleSender().sendMessage("§a[orbital-economy] §f" + this.users.size() + " §aloaded successfully!");
    }


    public User loadUser(String name) {

        if (hasUser(name)) return getUser(name);

        User user = new User(name);
        for (CurrencyType value : CurrencyType.values()) user.loadCurrency(value, 0);
        return user;
    }

    @SneakyThrows
    public void saveAll() {
        for (User user : this.users)
            save(user, false);

        Bukkit.getConsoleSender().sendMessage("§c[orbital-economy] §f" + this.users.size() + " §csaved successfully!");
    }

    public void save(User user, boolean debug) throws SQLException {

        JSONObject obj = new JSONObject();
        user.getCurrencies().forEach(currency -> obj.put(currency.getType().name(), currency.getAmount()));

        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement("SELECT * FROM economy WHERE NAME = ?");
        statement.setString(1, user.getName());
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            statement = DatabaseConnector.getConnection().prepareStatement("UPDATE economy SET CURRENCIES = ? WHERE NAME = ?");
            statement.setString(1, obj.toJSONString());
            statement.setString(2, user.getName());
        } else {
            statement = DatabaseConnector.getConnection().prepareStatement("INSERT INTO economy (NAME,CURRENCIES) VALUES(?,?)");
            statement.setString(1, user.getName());
            statement.setString(2, obj.toJSONString());
        }

        statement.execute();
        statement.close();

        if (debug)
            Bukkit.getConsoleSender().sendMessage("§a[orbital-economy] User §f" + user.getName() + " §ahas been successfully saved.");

    }

    public void pay(User sender, User receiver, CurrencyType type, double amount) {

        if (sender == receiver) {
            sender.getPlayer().sendMessage("§cInvalid player!");
            return;
        }

        Currency senderCurrency = sender.getCurrency(type);
        Currency receiverCurrency = receiver.getCurrency(type);

        if (senderCurrency.getAmount() < amount) {
            sender.getPlayer().sendMessage("§e§l[" + senderCurrency.getType() + "] §cYou don't have the amount enough.");
            return;
        }

        senderCurrency.removeAmount(amount);
        receiverCurrency.addAmount(amount);
        sender.getPlayer().sendMessage("§e§l[" + senderCurrency.getType() + "] §aYou have successfully sent an amount of §f" + new Formatter().formatNumber(amount) + " §ato player §f" + receiver.getName() + "§a!");
        receiver.getPlayer().sendMessage("§e§l[" + receiverCurrency.getType() + "] §aYou have successfully received an amount of §f" + new Formatter().formatNumber(amount) + " §afrom the player §f" + sender.getName() + "§a!");
    }

    public void view(User sender, User target, CurrencyType type) {

        Currency currency;

        if (sender == target) {
            currency = sender.getCurrency(type);
            sender.getPlayer().sendMessage("§e§l[" + currency.getType() + "] §aYou have an amount of §f" + new Formatter().formatNumber(currency.getAmount()) + "§a.");
            return;
        }

        currency = target.getCurrency(type);
        sender.getPlayer().sendMessage("§e§l[" + currency.getType() + "] §aThe player §f" + target.getName() + " has an amount of §f" + new Formatter().formatNumber(currency.getAmount()) + "§a.");
    }

    public boolean hasUser(String name) {
        return this.users.stream().anyMatch(user -> user.getName().equalsIgnoreCase(name));
    }

    public User getUser(String name) {
        return this.users.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }
}
