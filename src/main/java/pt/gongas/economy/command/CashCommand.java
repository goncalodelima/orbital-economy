package pt.gongas.economy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.gongas.economy.EconomyPlugin;
import pt.gongas.economy.currency.type.CurrencyType;
import pt.gongas.economy.user.User;
import org.jetbrains.annotations.NotNull;

public class CashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {

        if (args.length == 0){

            if (!(sender instanceof Player player))
                return true;

            User user = EconomyPlugin.getInstance().getUserManager().loadUser(player.getName());
            EconomyPlugin.getInstance().getUserManager().view(user, user, CurrencyType.CASH);
            return true;
        }

        if (sender.hasPermission("economy.admin")){

            if (args[0].equalsIgnoreCase("set")){

                if (args.length != 3){
                    sender.sendMessage("§cType '/cash set <player> <amount>'");
                    return true;
                }

                if (args[2].equalsIgnoreCase("nan")){
                    sender.sendMessage("§cInvalid amount!");
                    return true;
                }

                double amount;
                String targetName = args[1];
                User userTarget = EconomyPlugin.getInstance().getUserManager().getUser(targetName);

                if (userTarget == null){
                    sender.sendMessage("§cInvalid player!");
                    return true;
                }

                try {
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    sender.sendMessage("§cInvalid amount!");
                    return true;
                }

                userTarget.getCurrency(CurrencyType.CASH).setAmount(amount);
                sender.sendMessage("§aAmount defined successfully!");
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("pay")){

            if (!(sender instanceof Player player))
                return true;

            if (args.length != 3){
                player.sendMessage("§cType '/cash pay <player> <amount>'");
                return true;
            }

            if (args[2].equalsIgnoreCase("nan")){
                sender.sendMessage("§cInvalid amount!");
                return true;
            }

            double amount;
            String targetName = args[1];
            User user = EconomyPlugin.getInstance().getUserManager().loadUser(player.getName());
            User userTarget = EconomyPlugin.getInstance().getUserManager().getUser(targetName);

            if (userTarget == null){
                player.sendMessage("§cInvalid player!");
                return true;
            }

            try {
                amount = Double.parseDouble(args[2]);
            }catch (Exception e){
                player.sendMessage("§cInvalid amount!");
                return true;
            }

            EconomyPlugin.getInstance().getUserManager().pay(user, userTarget, CurrencyType.CASH, amount);
            return true;
        }

        if (args[0].equalsIgnoreCase("earn")){

            if (!(sender instanceof Player player))
                return true;

            if (args.length != 1){
                player.sendMessage("§cType '/cash earn'");
                return true;
            }

            if (EconomyPlugin.getInstance().getEarnCooldown().containsKey(player)) {
                long cooldownEnd = EconomyPlugin.getInstance().getEarnCooldown().get(player) + 60 * 1000;
                long currentTime = System.currentTimeMillis();
                if (currentTime < cooldownEnd) {
                    long remainingTime = (cooldownEnd - currentTime) / 1000;
                    player.sendMessage("§cYou must wait another §f" + remainingTime + " §cseconds before doing this again.");
                    return true;
                }
            }

            double amount = 1 + Math.random() * 4;
            User user = EconomyPlugin.getInstance().getUserManager().loadUser(player.getName());

            user.getCurrency(CurrencyType.CASH).addAmount(amount);
            EconomyPlugin.getInstance().getEarnCooldown().put(player, System.currentTimeMillis());
            sender.sendMessage("§aAmount added successfully!");
            return true;
        }

        if (args.length == 1){

            if (!(sender instanceof Player player))
                return true;

            String targetName = args[0];
            User user = EconomyPlugin.getInstance().getUserManager().loadUser(player.getName());
            User userTarget = EconomyPlugin.getInstance().getUserManager().getUser(targetName);

            if (userTarget == null){
                player.sendMessage("§cInvalid player!");
                return true;
            }

            EconomyPlugin.getInstance().getUserManager().view(user, userTarget, CurrencyType.CASH);
            return true;
        }


        return false;
    }
}
