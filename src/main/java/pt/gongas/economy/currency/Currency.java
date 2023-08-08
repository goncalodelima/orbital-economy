package pt.gongas.economy.currency;

import pt.gongas.economy.currency.type.CurrencyType;

public class Currency {

    private final CurrencyType type;
    private double amount;

    public Currency(CurrencyType type, double amount){
        this.type = type;
        this.amount = amount;
    }

    public void addAmount(double amount){
        this.amount += amount;
    }

    public void removeAmount(double amount){
        this.amount -= amount;
    }

    public CurrencyType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
