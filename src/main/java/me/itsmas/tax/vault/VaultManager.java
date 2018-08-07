package me.itsmas.tax.vault;

import me.itsmas.tax.MonthlyTax;
import me.itsmas.tax.util.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager
{
    private final MonthlyTax plugin;

    public VaultManager(MonthlyTax plugin)
    {
        this.plugin = plugin;

        percentageTax = plugin.getConfig("percentage_tax", 20.0D);

        setupEconomy();
    }

    private void setupEconomy()
    {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (provider != null)
        {
            economy = provider.getProvider();
        }
        else
        {
            throw new AssertionError("Missing dependency: Vault/Economy");
        }
    }

    private Economy economy;

    private double percentageTax;

    public double getPercentageTax()
    {
        return percentageTax;
    }

    public double takeTax(OfflinePlayer player)
    {
        double balance = economy.getBalance(player);
        double newBalance = balance * (1 - (percentageTax / 100));

        double taken = balance - newBalance;

        economy.withdrawPlayer(player, taken);

        if (player.isOnline())
        {
            String msg = Message.TAX_COLLECTED.value();

            msg = msg
                    .replace("%old_balance%", String.valueOf(balance))
                    .replace("%new_balance%", String.valueOf(newBalance))
                    .replace("%taken%", String.valueOf(taken));

            player.getPlayer().sendMessage(msg);
        }

        plugin.getData().updateLastTaxAmount(player, taken);
        return taken;
    }
}
