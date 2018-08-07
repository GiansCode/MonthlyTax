package me.itsmas.tax.command;

import me.itsmas.tax.MonthlyTax;
import me.itsmas.tax.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TaxCommand implements CommandExecutor
{
    private final MonthlyTax plugin;

    public TaxCommand(MonthlyTax plugin)
    {
        this.plugin = plugin;

        plugin.getCommand("tax").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp() && !sender.hasPermission("monthlytax.command"))
        {
            Message.send(sender, Message.NO_PERMISSION);
            return true;
        }

        if (args.length != 1)
        {
            Message.send(sender, Message.COMMAND_USAGE);
            return true;
        }

        handleArgs(sender, args[0]);
        return true;
    }

    private void handleArgs(CommandSender sender, String argument)
    {
        Player player = Bukkit.getPlayer(argument);

        if (player == null)
        {
            if (argument.equalsIgnoreCase("all"))
            {
                plugin.getTaxManager().collectTax();
                Message.send(sender, Message.COLLECTED_TAX_ALL);
                return;
            }

            Message.send(sender, Message.PLAYER_OFFLINE);
            return;
        }

        plugin.getVaultManager().takeTax(player);
        Message.send(sender, Message.COLLECTED_TAX_PLAYER, player.getName());
    }
}
