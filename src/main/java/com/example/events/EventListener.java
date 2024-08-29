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

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("Welcome to SaprLand server " + player.getName());
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        ItemStack item = event.getItem();
        if (item == null)
            return;
        if (item.getType() == Material.BLAZE_ROD) {
            event.getPlayer().sendMessage("Blaze rod clicked");
        }
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.COBBLESTONE) {
            return;
        }

        event.getPlayer().sendMessage("You have clicked cobblestone");
    }

    @EventHandler
    public void onPickUpItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.sendMessage("Picked item: " + event.getItem().getItemStack().getType().name().toLowerCase());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage() instanceof String) {
            String text = event.getMessage();
            event.getPlayer().sendMessage(ChatColor.GOLD + "You have sent message - " + text);
        }
    }

    @EventHandler
    public void onBlickBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.PODZOL && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.blockList().removeIf((block) -> block.getType() == Material.PODZOL);
    }
}
