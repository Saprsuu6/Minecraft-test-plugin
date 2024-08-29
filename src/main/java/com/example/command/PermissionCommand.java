package com.example.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.example.Market;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

public class PermissionCommand extends AbstractCommand {
    public PermissionCommand() {
        super("grantperm");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender == null) {
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player.");
            return;
        }

        if (sender.getName().equals("Saprsuu6")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Get permissions: /" + label + " <player> <permission>");
            return;
        }

        String targetPlayerName = args[0];
        String permission = args[1];

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        // Даём игроку разрешение
        PermissionAttachment permissionAttachment = targetPlayer.addAttachment(Market.getInstance());
        permissionAttachment.setPermission(permission, true);

        sender.sendMessage(
                ChatColor.GREEN + "Permission " + permission + " has been granted to " + targetPlayerName);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("<player> <permission>");
        }

        return Lists.newArrayList();
    }
}
