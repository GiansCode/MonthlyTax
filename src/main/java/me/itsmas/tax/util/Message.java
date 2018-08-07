package me.itsmas.tax.util;

import me.itsmas.tax.MonthlyTax;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Message
{
    TAX_COLLECTED,
    COMMAND_USAGE,
    PLAYER_OFFLINE,
    COLLECTED_TAX_ALL,
    COLLECTED_TAX_PLAYER,
    NO_PERMISSION,
    TAX_COLLECTED_OFFLINE;

    private String msg;

    private void setValue(String msg)
    {
        assert this.msg == null : "Message is already set";

        this.msg = msg;
    }

    public String value()
    {
        return msg;
    }

    public static void send(CommandSender player, Message message, Object... params)
    {
        if (!message.value().isEmpty())
        {
            player.sendMessage(String.format(message.value(), params));
        }
    }

    public static void init(MonthlyTax plugin)
    {
        final String messagesPath = "messages";

        for (Message message : values())
        {
            String value = plugin.getConfig(messagesPath + "." + message.name().toLowerCase());

            if (value == null)
            {
                Logs.error("No value found for message '%s'", message.name());
                value = message.name();
            }

            message.setValue(ChatColor.translateAlternateColorCodes('&', value));
        }
    }
}

