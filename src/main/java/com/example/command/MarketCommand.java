package com.example.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.example.Market;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

public class MarketCommand extends AbstractCommand {
    public MarketCommand() {
        super("market");
    }

    private String getMessage(String key, String def) {
        String message = Market.getInstance().getConfig().getString(key, def);

        if (message == null) {
            throw new IllegalArgumentException("Message for key '" + key + "' was not found");
        }

        return message;
    }

    private String getMessage(String key) {
        return getMessage(key, null);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("market.reload")) {
            sender.sendMessage(ChatColor.RED + getMessage("messages.noPermission"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(getMessage("messages.usage"));
            return;
        }

        if (args[0].equalsIgnoreCase("sell")) {
            if (args.length < 2) {
                sender.sendMessage(getMessage("messages.usage"));
            }

            if (!(sender instanceof Player))
                return;

            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                sender.sendMessage(getMessage(ChatColor.RED + "messages.noItem"));
                return;
            }

            double price;
            String priceArg = args[1];

            try {
                price = Double.parseDouble(priceArg);
                if (price <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(getMessage(ChatColor.RED + "messages.invalidPrice"));
                return;
            }

            String orderMessage = getMessage("messages.sellOrderCreated", "Created sell order: {item}");
            if (orderMessage != null) {
                orderMessage = orderMessage.replace("{item}", item.toString());
                orderMessage = orderMessage.replace("{price}", String.valueOf(price));
                sender.sendMessage(getMessage(ChatColor.GREEN + orderMessage));
            } else {
                sender.sendMessage(ChatColor.RED + getMessage("messages.someError"));
            }
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Market.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Market reloaded.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown command. " + args[0]);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("reload");
        }

        return Lists.newArrayList();
    }
}