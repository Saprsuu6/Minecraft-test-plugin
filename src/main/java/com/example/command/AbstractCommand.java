package com.example.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import com.example.Market;

// Абстрактный класс для реализации команд с поддержкой выполнения и автодополнения
public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    // Ссылка на команду, определённую в plugin.yml
    private final PluginCommand pluginCommand;

    // Конструктор, получает команду по её имени из конфигурации плагина
    public AbstractCommand(String command) {
        this.pluginCommand = Market.getInstance().getCommand(command);
    }

    // Метод регистрации команды: установка текущего класса в качестве исполнителя и
    // обработчика автодополнения
    public void register() {
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    // Абстрактный метод, который будет реализован в наследниках для обработки
    // выполнения команды
    public abstract void execute(CommandSender sender, String label, String[] args);

    // Метод автодополнения, который можно переопределить в наследниках
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }

    // Метод, вызываемый при выполнении команды, делегирует выполнение в метод
    // execute
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    // Метод обработки автодополнения, вызывает метод complete и фильтрует
    // результаты
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return filter(complete(sender, args), args);
    }

    // Фильтрация подсказок автодополнения по последнему введённому аргументу (без
    // учета регистра)
    private List<String> filter(List<String> list, String[] args) {
        if (list == null)
            return null;

        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();

        for (String arg : list) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) {
                result.add(arg);
            }
        }

        return result;
    }
}
