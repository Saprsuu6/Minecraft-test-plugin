package com.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.command.MarketCommand;
import com.example.command.PermissionCommand;
import com.example.events.EventListener;

/*
 * Демонстрационный плагин для Bukkit
 */
public class Market extends JavaPlugin {
  // Статическая ссылка на экземпляр плагина для удобного доступа в других классах
  private static Market instatnce;

  // Экземпляры команд
  private MarketCommand marketCommand;
  private PermissionCommand permissionCommand;

  @Override
  public void onEnable() {
    // Сохранение ссылки на текущий экземпляр плагина
    instatnce = this;

    // Инициализация и регистрация команды /market
    marketCommand = new MarketCommand();
    marketCommand.register();

    // Сохранение дефолтного конфига, если его нет
    saveDefaultConfig();

    // Инициализация и регистрация команды /grantperm
    permissionCommand = new PermissionCommand();
    permissionCommand.register();

    // Регистрация слушателя событий
    Bukkit.getPluginManager().registerEvents(new EventListener(), this);
  }

  @Override
  public void onDisable() {
    // Вызываем метод onDisable() родительского класса (можно добавить логику
    // очистки)
    super.onDisable();
  }

  // Метод для получения экземпляра плагина из других классов
  public static Market getInstance() {
    return instatnce;
  }
}
