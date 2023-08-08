package pt.gongas.economy.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.gongas.economy.EconomyPlugin;
import pt.gongas.economy.currency.Currency;
import pt.gongas.economy.currency.type.CurrencyType;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String name;
    private final List<Currency> currencies;

    public User(String name){
        this.name = name;
        this.currencies = new ArrayList<>();
        EconomyPlugin.getInstance().getUserManager().getUsers().add(this);
    }

    public void loadCurrency(CurrencyType type, double amount){
        this.currencies.add(new Currency(type, amount));
    }

    public Currency getCurrency(CurrencyType type){
        return this.currencies.stream().filter(currency -> currency.getType() == type).findAny().orElse(null);
    }

    public Player getPlayer(){
        return Bukkit.getPlayerExact(this.name);
    }

    public String getName() {
        return name;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
