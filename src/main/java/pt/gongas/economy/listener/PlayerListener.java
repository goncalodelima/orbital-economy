package pt.gongas.economy.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.gongas.economy.EconomyPlugin;
import pt.gongas.economy.currency.type.CurrencyType;
import pt.gongas.economy.user.User;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (EconomyPlugin.getInstance().getUserManager().getUser(player.getName()) == null) {

            User user = new User(player.getName());
            for (CurrencyType value : CurrencyType.values()) user.loadCurrency(value, 0);

        }

    }

}
