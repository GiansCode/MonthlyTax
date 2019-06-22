package me.itsmas.tax.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.itsmas.tax.MonthlyTax;
import me.itsmas.tax.util.Logs;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

public class PlaceholderHook extends PlaceholderExpansion
{
    private final MonthlyTax plugin;

    public PlaceholderHook(MonthlyTax plugin)
    {
        this.plugin = plugin;

        try
        {
            format = new SimpleDateFormat(plugin.getConfig("date_format", "dd/MM/yyyy HH:mm:ss"));
        }
        catch (Exception ex)
        {
            Logs.error("Invalid date format in config");
            ex.printStackTrace();
        }
    }

    @Override
    public String getIdentifier()
    {
        return "monthlytax";
    }

    private SimpleDateFormat format;

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        if (player != null)
        {
            if (identifier.equalsIgnoreCase("last_tax"))
            {
                return String.valueOf(plugin.getData().getLastTaxAmount(player));
            }
        }
        else
        {
            if (identifier.equalsIgnoreCase("last_total_tax"))
            {
                return String.valueOf(plugin.getData().getLastTotalTax());
            }
            else if (identifier.equalsIgnoreCase("tax_percentage"))
            {
                return String.valueOf(plugin.getVaultManager().getPercentageTax());
            }
            else if (identifier.equalsIgnoreCase("next_collection"))
            {
                return format.format(plugin.getData().getNextCollection());
            }
        }

        return null;
    }

    @Override
    public String getAuthor()
    {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion()
    {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }
}
