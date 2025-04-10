package com.example.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.example.Market;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

// Класс, реализующий команду /market
public class MarketCommand extends AbstractCommand {
    // Конструктор, передаёт имя команды родительскому классу
    public MarketCommand() {
        super("market");
    }

    // Метод для получения строки сообщения из конфига по ключу и значению по
    // умолчанию
    private String getMessage(String key, String def) {
        String message = Market.getInstance().getConfig().getString(key, def);

        if (message == null) {
            throw new IllegalArgumentException("Message for key '" + key + "' was not found");
        }

        return message;
    }

    // Перегруженный метод для получения сообщения без дефолтного значения
    private String getMessage(String key) {
        return getMessage(key, null);
    }

    // Обработка выполнения команды /market
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Проверяем, имеет ли отправитель права для перезагрузки (требуется permission
        // "market.reload")
        if (!sender.hasPermission("market.reload")) {
            sender.sendMessage(ChatColor.RED + getMessage("messages.noPermission"));
            return;
        }

        // Если аргументы отсутствуют, выводим сообщение с инструкцией по использованию
        if (args.length == 0) {
            sender.sendMessage(getMessage("messages.usage"));
            return;
        }

        // Если первая часть команды равна "sell", обрабатываем создание ордера на
        // продажу
        if (args[0].equalsIgnoreCase("sell")) {
            if (args.length < 2) {
                sender.sendMessage(getMessage("messages.usage"));
            }

            // Команда sell предназначена только для игроков, а не консоли
            if (!(sender instanceof Player))
                return;

            Player player = (Player) sender;
            // Получаем предмет, который игрок держит в основной руке
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                sender.sendMessage(getMessage(ChatColor.RED + "messages.noItem"));
                return;
            }

            double price;
            String priceArg = args[1];

            // Пытаемся распарсить цену и проверяем, что она положительная
            try {
                price = Double.parseDouble(priceArg);
                if (price <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(getMessage(ChatColor.RED + "messages.invalidPrice"));
                return;
            }

            // Формируем сообщение о создании ордера на продажу, подставляя данные предмета
            // и цену
            String orderMessage = getMessage("messages.sellOrderCreated", "Created sell order: {item}");
            if (orderMessage != null) {
                orderMessage = orderMessage.replace("{item}", item.toString());
                orderMessage = orderMessage.replace("{price}", String.valueOf(price));
                sender.sendMessage(getMessage(ChatColor.GREEN + orderMessage));
            } else {
                sender.sendMessage(ChatColor.RED + getMessage("messages.someError"));
            }
        }

        // Если первая часть команды равна "reload", перезагружаем конфигурацию плагина
        if (args[0].equalsIgnoreCase("reload")) {
            Market.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Market reloaded.");
            return;
        }

        // Если команда не распознана, выводим сообщение об ошибке
        sender.sendMessage(ChatColor.RED + "Unknown command. " + args[0]);
    }

    // Метод автодополнения для команды /market
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("reload");
        }

        return Lists.newArrayList();
    }
}
