package com.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.command.MarketCommand;
import com.example.command.PermissionCommand;
import com.example.events.EventListener;

/*
 * demo java plugin
 */
public class Market extends JavaPlugin {
  private static Market instatnce;
  private MarketCommand marketCommand;
  private PermissionCommand permissionCommand;

  @Override
  public void onEnable() {
    instatnce = this;

    marketCommand = new MarketCommand();
    marketCommand.register();

    saveDefaultConfig();

    permissionCommand = new PermissionCommand();
    permissionCommand.register();

    Bukkit.getPluginManager().registerEvents(new EventListener(), this);
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }

  public static Market getInstance() {
    return instatnce;
  }
}
