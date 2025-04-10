package com.example.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

// Класс, реализующий обработку различных событий сервера
public class EventListener implements Listener {
    // Событие при входе игрока на сервер
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Устанавливаем сообщение при входе игрока
        event.setJoinMessage("Welcome to SaprLand server " + player.getName());
    }

    // Событие при взаимодействии игрока (клик правой кнопкой) с предметом
    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        // Проверяем, что действие - клик правой кнопкой (в блок или в воздух)
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        ItemStack item = event.getItem();
        if (item == null)
            return;
        // Если игрок использует предмет - Blaze Rod, отправляем сообщение
        if (item.getType() == Material.BLAZE_ROD) {
            event.getPlayer().sendMessage("Blaze rod clicked");
        }
    }

    // Событие при клике по блоку
    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        // Обрабатываем только клик правой кнопкой по блоку
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block clickedBlock = event.getClickedBlock();
        // Если кликнутый блок существует и является камнем (cobblestone)
        if (clickedBlock != null && clickedBlock.getType() == Material.COBBLESTONE) {
            event.getPlayer().sendMessage("You have clicked cobblestone");
        }
    }

    // Событие подбора предмета сущностью (если это игрок)
    @EventHandler
    public void onPickUpItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Сообщаем игроку, какой предмет он подобрал
            player.sendMessage("Picked item: " + event.getItem().getItemStack().getType().name().toLowerCase());
        }
    }

    // Событие отправки сообщения в чат
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage() instanceof String) {
            String text = event.getMessage();
            // Отправляем игроку подтверждение с текстом отправленного сообщения
            event.getPlayer().sendMessage(ChatColor.GOLD + "You have sent message - " + text);
        }
    }

    // Событие разрушения блока
    @EventHandler
    public void onBlickBreak(BlockBreakEvent event) {
        // Если игрок в режиме выживания пытается сломать PODZOL, отменяем разрушение
        if (event.getBlock().getType() == Material.PODZOL && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }

    // Событие взрыва сущности
    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        // Удаляем из списка блоков, затронутых взрывом, все блоки типа PODZOL
        event.blockList().removeIf((block) -> block.getType() == Material.PODZOL);
    }
}
