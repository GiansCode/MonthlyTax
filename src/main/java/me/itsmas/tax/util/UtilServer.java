package me.itsmas.tax.util;

import me.itsmas.tax.MonthlyTax;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class UtilServer
{
    private UtilServer(){}

    private static final MonthlyTax plugin = JavaPlugin.getPlugin(MonthlyTax.class);

    static MonthlyTax getPlugin()
    {
        return plugin;
    }

    public static void registerListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void dispatchCommand(String command)
    {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
