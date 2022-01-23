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

public class PluginCommand implements CommandExecutor {

    private final HiderPlugin hider;
    private final ConsoleCommandSender log;

    private Sound permission;
    private int volume;
    private int pitch;

    public PluginCommand(HiderPlugin hider) {
        Optional<XSound> xs = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.permission"));
        if(xs.isPresent()) this.permission = xs.get().parseSound();

        this.volume = hider.getConfiguration().getConfig().getInt("sounds.volume");
        this.pitch = hider.getConfiguration().getConfig().getInt("sounds.pitch");
        this.log = Bukkit.getConsoleSender();
        this.hider = hider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String ja, String[] args) {
        FileConfiguration config = hider.getConfiguration().getConfig();
        String prefix = config.getString("prefix");
        if(sender instanceof Player) {
            final Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage(Msg.color(prefix + "&7 Running on version &e" + hider.getVersionID() + "&7 by &a" + hider.getAuthor()));
                return true;
            }

            if(args[0].equalsIgnoreCase("list")) {
                if(player.hasPermission("hiderplayers.cmd.list")) {
                    String text = config.getString("messages.list_cmds");
                    String[] textSplit = text.split("\n");
                    for(int i = 0 ; i < textSplit.length ; i++) {
                        String message = textSplit[i];
                        message = Msg.color(message);
                        player.sendMessage(message);
                    }
                    return true;
                }
                if(config.getBoolean("sounds.reproduce")) executePermissionSound(player);
                player.sendMessage(Msg.color(config.getString("messages.not_permission"))
                    .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
                return false;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                if(player.hasPermission("hiderplayers.cmd.list")) {
                    long RELOAD_TIME = System.currentTimeMillis();
                    hider.getConfiguration().reload();
                    player.sendMessage(Msg.color(config.getString("messages.reload"))
                        .replace(config.getString("prefix_identifier"), Msg.color(prefix))
                        .replace("%ms%", (System.currentTimeMillis() - RELOAD_TIME) + ""));
                    return true;
                }
                if(config.getBoolean("sounds.reproduce")) executePermissionSound(player);
                player.sendMessage(Msg.color(config.getString("messages.not_permission"))
                        .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
                return false;
            }

            player.sendMessage(Msg.color(config.getString("messages.not_command"))
                    .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
            return false;
        }
        if(args.length == 0) {
            log.sendMessage(Msg.color(prefix + "&7 Running on version &e" + hider.getVersionID() + "&7 by &a" + hider.getAuthor()));
            return true;
        }

        if(args[0].equalsIgnoreCase("list")) {
            if(log.hasPermission("hiderplayers.cmd.list")) {
                String text = config.getString("messages.list_cmds");
                String[] textSplit = text.split("\n");
                for(int i = 0 ; i < textSplit.length ; i++) {
                    String message = textSplit[i];
                    message = Msg.color(message);
                    log.sendMessage(message);
                }
                return true;
            }
            log.sendMessage(Msg.color(config.getString("messages.not_permission"))
                    .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
            return false;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            if(log.hasPermission("hiderplayers.cmd.list")) {
                long RELOAD_TIME = System.currentTimeMillis();
                hider.getConfiguration().reload();
                log.sendMessage(Msg.color(config.getString("messages.reload"))
                        .replace(config.getString("prefix_identifier"), Msg.color(prefix))
                        .replace("%ms%", (System.currentTimeMillis() - RELOAD_TIME) + ""));
                return true;
            }
            log.sendMessage(Msg.color(config.getString("messages.not_permission"))
                    .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
            return false;
        }

        log.sendMessage(Msg.color(config.getString("messages.not_command"))
                .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
        return false;
    }

    private void executePermissionSound(Player player) {
        player.playSound(player.getLocation(), permission, volume, pitch);
    }
}
