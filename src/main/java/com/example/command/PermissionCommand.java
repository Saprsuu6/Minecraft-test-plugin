package com.example.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.example.Market;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

// Класс, реализующий команду /grantperm для выдачи прав игрокам
public class PermissionCommand extends AbstractCommand {
    // Конструктор, передаёт имя команды родительскому классу
    public PermissionCommand() {
        super("grantperm");
    }

    // Обработка выполнения команды /grantperm
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        // Если отправитель null - выходим
        if (sender == null) {
            return;
        }

        // Команда может использоваться только игроком, а не консолью
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            return;
        }

        // Блокировка использования команды для конкретного игрока (например, с именем
        // "Saprsuu6")
        if (sender.getName().equals("Saprsuu6")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        // Если аргументы отсутствуют, выводим подсказку по использованию команды
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Get permissions: /" + label + " <player> <permission>");
            return;
        }

        // Получаем имя целевого игрока и право, которое нужно выдать
        String targetPlayerName = args[0];
        String permission = args[1];

        // Ищем игрока по имени
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        // Выдаем игроку разрешение с помощью PermissionAttachment
        PermissionAttachment permissionAttachment = targetPlayer.addAttachment(Market.getInstance());
        permissionAttachment.setPermission(permission, true);

        // Сообщаем отправителю, что право успешно выдано
        sender.sendMessage(
                ChatColor.GREEN + "Permission " + permission + " has been granted to " + targetPlayerName);
    }

    // Метод автодополнения для команды /grantperm
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("<player> <permission>");
        }

        return Lists.newArrayList();
    }
}
