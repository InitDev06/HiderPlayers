package org.alqj.dev.hider.commands;

import org.alqj.dev.hider.HiderPlugin;
import org.alqj.dev.hider.color.Msg;
import org.alqj.dev.hider.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

public class InventoryCommand implements CommandExecutor {

    private final HiderPlugin hider;
    private final ConsoleCommandSender log;

    private String message;

    private Sound permission;
    private int volume;
    private int pitch;

    public InventoryCommand(HiderPlugin hider) {
        Optional<XSound> xs = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.permission"));
        if(xs.isPresent()) this.permission = xs.get().parseSound();

        this.volume = hider.getConfiguration().getConfig().getInt("sounds.volume");
        this.pitch = hider.getConfiguration().getConfig().getInt("sounds.pitch");
        this.log = Bukkit.getConsoleSender();
        this.hider = hider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String la, String[] args) {
        FileConfiguration config = hider.getConfiguration().getConfig();
        String prefix = config.getString("prefix");
        if(sender instanceof Player) {
            final Player player = (Player) sender;
            if(player.hasPermission("hiderplayers.menu.open")) {
                hider.getLc().TOOLS_GUI.createInventory(player);
                String message = config.getString("messages.events.opened_tools_menu");
                message = message.replace(config.getString("prefix_identifier"), prefix);
                player.sendMessage(Msg.color(message));
                return true;
            }
            message = config.getString("messages.not_permission");
            message = message.replace(config.getString("prefix_identifier"), prefix);
            if(config.getBoolean("sounds.reproduce")) executePermissionSound(player);
            player.sendMessage(Msg.color(message));
            return false;
        }
        log.sendMessage(Msg.color(config.getString("messages.not_console"))
            .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
        return false;
    }

    private void executePermissionSound(Player player) {
        player.playSound(player.getLocation(), permission, volume, pitch);
    }
}
