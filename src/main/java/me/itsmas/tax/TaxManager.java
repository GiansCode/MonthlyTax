package me.itsmas.tax;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class TaxManager
{
    private final MonthlyTax plugin;

    TaxManager(MonthlyTax plugin)
    {
        this.plugin = plugin;

        daysInterval = plugin.getConfig("days_interval", 30);

        if (daysInterval < 1)
        {
            daysInterval = 1;
        }

        plugin.getData().setNextCollection(plugin.getData().getLastCollection() + (daysInterval * TimeUnit.DAYS.toMillis(1)));

        startRunnable();
    }

    private int daysInterval;

    public long getNextCollection(long lastCollection)
    {
        return lastCollection + (daysInterval * TimeUnit.DAYS.toMillis(1));
    }

    public void collectTax()
    {
        double total = 0.0D;

        for (OfflinePlayer player : Bukkit.getOfflinePlayers())
        {
            total += plugin.getVaultManager().takeTax(player);
        }

        plugin.getData().setLastTotalTax(total);
    }

    private void startRunnable()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (plugin.getData().shouldCollectTax())
                {
                    collectTax();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}
