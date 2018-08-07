package me.itsmas.tax;

import me.itsmas.tax.command.TaxCommand;
import me.itsmas.tax.data.Data;
import me.itsmas.tax.placeholder.PlaceholderHook;
import me.itsmas.tax.util.Message;
import me.itsmas.tax.vault.VaultManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MonthlyTax extends JavaPlugin
{
    private TaxManager taxManager; public TaxManager getTaxManager() { return taxManager; }
    private Data data; public Data getData() { return data; }
    private VaultManager vaultManager; public VaultManager getVaultManager() { return vaultManager; }

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        Message.init(this);

        data = new Data(this);
        taxManager = new TaxManager(this);
        vaultManager = new VaultManager(this);

        new PlaceholderHook(this).hook();
        new TaxCommand(this);

    }

    @Override
    public void onDisable()
    {
        getData().save();
    }

    public <T> T getConfig(String path)
    {
        return getConfig(path, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfig(String path, Object defaultValue)
    {
        return (T) getConfig().get(path, defaultValue);
    }
}
