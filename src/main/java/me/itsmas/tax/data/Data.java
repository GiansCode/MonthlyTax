package me.itsmas.tax.data;

import me.itsmas.tax.MonthlyTax;
import me.itsmas.tax.util.Logs;
import me.itsmas.tax.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class Data implements Listener
{
    private final MonthlyTax plugin;

    public Data(MonthlyTax plugin)
    {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists())
        {
            plugin.saveResource("data.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        lastCollection = config.getLong("last_collection", System.currentTimeMillis());

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private final File file;
    private final YamlConfiguration config;

    private long lastCollection;
    private long nextCollection;

    private void updateLastCollection()
    {
        lastCollection = System.currentTimeMillis();
        nextCollection = plugin.getTaxManager().getNextCollection(lastCollection);

        config.set("last_collection", lastCollection);
    }

    public long getLastCollection()
    {
        return lastCollection;
    }

    public long getNextCollection()
    {
        return nextCollection;
    }

    public void setNextCollection(long time)
    {
        nextCollection = time;
    }

    public boolean shouldCollectTax()
    {
        boolean collect = System.currentTimeMillis() > nextCollection;

        if (collect)
        {
            updateLastCollection();
        }

        return collect;
    }

    public void updateLastTaxAmount(OfflinePlayer player, double amount)
    {
        config.set(player.getUniqueId() + ".lastTax", amount);
        config.set(player.getUniqueId() + ".informed", false);
    }

    public double getLastTaxAmount(Player player)
    {
        return config.getDouble(player.getUniqueId() + ".lastTax", 0.0D);
    }

    public void setLastTotalTax(double amount)
    {
        config.set("server_last_total_tax", amount);
    }

    public double getLastTotalTax()
    {
        return config.getDouble("server_last_total_tax", 0.0D);
    }

    public void save()
    {
        try
        {
            config.save(file);
        }
        catch (IOException ex)
        {
            Logs.error("Unable to save data.yml file:");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        double lastTax = config.getDouble(player.getUniqueId() + ".lastTax");

        if (lastTax != 0 && !config.getBoolean(player.getUniqueId() + ".informed"))
        {
            player.sendMessage(Message.TAX_COLLECTED_OFFLINE.value().replace("%taken%", String.valueOf(lastTax)));

            config.set(player.getUniqueId() + ".informed", true);
        }
    }
}
