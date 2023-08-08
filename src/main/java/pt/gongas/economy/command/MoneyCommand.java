package pt.gongas.economy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pt.gongas.economy.EconomyPlugin;
import pt.gongas.economy.currency.type.CurrencyType;
import pt.gongas.economy.user.User;

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {

        if (args.length == 0){

            if (!(sender instanceof Player player))
                return true;

            User user = EconomyPlugin.getInstance().getUserManager().loadUser(player.getName());
            EconomyPlugin.getInstance().getUserManager().view(user, user, CurrencyType.MONEY);
            return true;
        }

        if (sender.hasPermission("economy.admin")){

            if (args[0].equalsIgnoreCase("set")){

                if (args.length != 3){
                    sender.sendMessage("§cType '/money set <player> <amount>'");
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

                userTarget.getCurrency(CurrencyType.MONEY).setAmount(amount);
                sender.sendMessage("§aAmount defined successfully!");
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("pay")){

            if (!(sender instanceof Player player))
                return true;

            if (args.length != 3){
                player.sendMessage("§cType '/money pay <player> <amount>'");
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

            EconomyPlugin.getInstance().getUserManager().pay(user, userTarget, CurrencyType.MONEY, amount);
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

            EconomyPlugin.getInstance().getUserManager().view(user, userTarget, CurrencyType.MONEY);
            return true;
        }


        return false;
    }
}
