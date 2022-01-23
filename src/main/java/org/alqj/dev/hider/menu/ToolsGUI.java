package org.alqj.dev.hider.menu;

import org.alqj.dev.hider.HiderPlugin;
import org.alqj.dev.hider.color.Msg;
import org.alqj.dev.hider.util.ItemBuilder;
import org.alqj.dev.hider.xseries.XMaterial;
import org.alqj.dev.hider.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author iAlqjDV
 */
public class ToolsGUI implements Listener {

    private final HiderPlugin hider;
    private String message;
    private String title;
    private String subtitle;
    private int in;
    private int show;
    private int out;

    private Sound permission;
    private Sound cooldown;
    private Sound hide;
    private Sound vanish;
    private Sound open;
    private int volume;
    private int pitch;

    private Inventory inv;

    public ToolsGUI(HiderPlugin hider) {
        Optional<XSound> xs = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.open_gui"));
        Optional<XSound> xs1 = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.permission"));
        Optional<XSound> xs2 = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.cooldown"));
        Optional<XSound> xs3 = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.hide_show"));
        Optional<XSound> xs4 = XSound.matchXSound(hider.getConfiguration().getConfig().getString("sounds.vanish"));
        if(xs.isPresent()) this.open = xs.get().parseSound();
        if(xs1.isPresent()) this.permission = xs1.get().parseSound();
        if(xs2.isPresent()) this.cooldown = xs2.get().parseSound();
        if(xs3.isPresent()) this.hide = xs3.get().parseSound();
        if(xs4.isPresent()) this.vanish = xs4.get().parseSound();

        this.volume = hider.getConfiguration().getConfig().getInt("sounds.volume");
        this.pitch = hider.getConfiguration().getConfig().getInt("sounds.pitch");
        this.hider = hider;
    }

    public void createInventory(Player player) {
        FileConfiguration config = hider.getConfiguration().getConfig();
        inv = Bukkit.createInventory(null, config.getInt("menu.size"), Msg.color(config.getString("menu.title")));

        ItemStack description = ItemBuilder.item(XMaterial.valueOf(config.getString("menu.items.description.material")), 1,
                Msg.color(config.getString("menu.items.description.name")), Msg.color(config.getString("menu.items.description.lore")));

        if(hider.playerWithHide(player.getUniqueId().toString())) {
            ItemStack hider_on = ItemBuilder.item(XMaterial.valueOf(config.getString("menu.items.hider_on.material")), 1,
                    Msg.color(config.getString("menu.items.hider_on.name")), Msg.color(config.getString("menu.items.hider_on.lore")));
            inv.setItem(config.getInt("menu.items.hider_slot"), hider_on);
        } else {
            ItemStack hider_off = ItemBuilder.item(XMaterial.valueOf(config.getString("menu.items.hider_off.material")), 1,
                    Msg.color(config.getString("menu.items.hider_off.name")), Msg.color(config.getString("menu.items.hider_off.lore")));
            inv.setItem(config.getInt("menu.items.hider_slot"), hider_off);
        }

        if(hider.playerWithVanishMode(player.getUniqueId().toString())) {
            ItemStack vanish_on = ItemBuilder.item(XMaterial.valueOf(config.getString("menu.items.vanish_on.material")), 1,
                    Msg.color(config.getString("menu.items.vanish_on.name")), Msg.color(config.getString("menu.items.vanish_on.lore")));
            inv.setItem(config.getInt("menu.items.vanish_slot"), vanish_on);
        } else {
            ItemStack vanish_off = ItemBuilder.item(XMaterial.valueOf(config.getString("menu.items.vanish_off.material")), 1,
                    Msg.color(config.getString("menu.items.vanish_off.name")), Msg.color(config.getString("menu.items.vanish_off.lore")));
            inv.setItem(config.getInt("menu.items.vanish_slot"), vanish_off);
        }

        inv.setItem(config.getInt("menu.items.description.slot"), description);

        if(config.getBoolean("sounds.reproduce")) executeOpenSound(player);
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        FileConfiguration config = hider.getConfiguration().getConfig();
        String prefix = config.getString("prefix");
        final Player player = (Player) event.getWhoClicked();
        if(event.getInventory().equals(inv)) {
            if(event.getSlot() == config.getInt("menu.items.description.slot")) {
                event.setCancelled(true);
                return;
            }

            if(event.getSlot() == config.getInt("menu.items.hider_slot")) {
                event.setCancelled(true);
                if(player.hasPermission("hiderplayers.menu.hider")) {
                    if(hider.getCooldown().hasCooldown(player.getUniqueId().toString())) {
                        message = config.getString("messages.events.cooldown_time");
                        message = message.replace(config.getString("prefix_identifier"), prefix);
                        message = message.replace("%time%", hider.getCooldown().getCooldown(player.getUniqueId().toString()) + "");
                        if(config.getBoolean("sounds.reproduce")) executeCooldownSound(player);
                        player.sendMessage(Msg.color(message));
                        return;
                    }
                    hider.getCooldown().setCooldown(player.getUniqueId().toString());
                    if(hider.playerWithHide(player.getUniqueId().toString())) {
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            player.showPlayer(all);
                        }
                        hider.removePlayerFromHide(player.getUniqueId().toString());
                        if(config.getBoolean("sounds.reproduce")) executeHideSound(player);
                        if(config.getBoolean("titles.send")) {
                            title = config.getString("messages.events.hider_disabled_title");
                            subtitle = config.getString("messages.events.hider_disabled_subtitle");

                            title = Msg.color(title);
                            subtitle = Msg.color(subtitle);

                            in = config.getInt("titles.in");
                            show = config.getInt("titles.show");
                            out = config.getInt("titles.out");

                            hider.getVc().getMinecraftVersion().sendTitle(player, title, subtitle, in, show, out);
                        }
                        message = config.getString("messages.events.hider_disabled");
                        message = message.replace(config.getString("prefix_identifier"), prefix);
                        player.sendMessage(Msg.color(message));
                        player.getOpenInventory().close();
                        return;
                    }
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        player.hidePlayer(all);
                    }
                    hider.addPlayerToHide(player.getUniqueId().toString());
                    if(config.getBoolean("sounds.reproduce")) executeHideSound(player);
                    if(config.getBoolean("titles.send")) {
                        title = config.getString("messages.events.hider_enabled_title");
                        subtitle = config.getString("messages.events.hider_enabled_subtitle");

                        title = Msg.color(title);
                        subtitle = Msg.color(subtitle);

                        in = config.getInt("titles.in");
                        show = config.getInt("titles.show");
                        out = config.getInt("titles.out");

                        hider.getVc().getMinecraftVersion().sendTitle(player, title, subtitle, in, show, out);
                    }
                    message = config.getString("messages.events.hider_enabled");
                    message = message.replace(config.getString("prefix_identifier"), prefix);
                    player.sendMessage(Msg.color(message));
                    player.getOpenInventory().close();
                }
                return;
            }

            if(event.getSlot() == config.getInt("menu.items.vanish_slot")) {
                event.setCancelled(true);
                if(player.hasPermission("hiderplayers.menu.vanish")) {
                    if(hider.getCooldown().hasCooldown(player.getUniqueId().toString())) {
                        message = config.getString("messages.events.cooldown_time");
                        message = message.replace(config.getString("prefix_identifier"), prefix);
                        message = message.replace("%time%", hider.getCooldown().getCooldown(player.getUniqueId().toString()) + "");
                        if(config.getBoolean("sounds.reproduce")) executeCooldownSound(player);
                        player.sendMessage(Msg.color(message));
                        return;
                    }
                    hider.getCooldown().setCooldown(player.getUniqueId().toString());
                    if(hider.playerWithVanishMode(player.getUniqueId().toString())) {
                        for(Player all : Bukkit.getOnlinePlayers()) {
                            all.showPlayer(player);
                        }
                        hider.removePlayerFromVanishMode(player.getUniqueId().toString());
                        if(config.getBoolean("sounds.reproduce")) executeVanishSound(player);
                        if(config.getBoolean("titles.send")) {
                            title = config.getString("messages.events.vanish_disabled_title");
                            subtitle = config.getString("messages.events.vanish_disabled_subtitle");

                            title = Msg.color(title);
                            subtitle = Msg.color(subtitle);

                            in = config.getInt("titles.in");
                            show = config.getInt("titles.show");
                            out = config.getInt("titles.out");

                            hider.getVc().getMinecraftVersion().sendTitle(player, title, subtitle, in, show, out);
                        }
                        message = config.getString("messages.events.vanish_disabled");
                        message = message.replace(config.getString("prefix_identifier"), prefix);
                        player.sendMessage(Msg.color(message));
                        player.getOpenInventory().close();
                        return;
                    }
                    for(Player all : Bukkit.getOnlinePlayers()) {
                        all.hidePlayer(player);
                    }
                    hider.addPlayerToVanishMode(player.getUniqueId().toString());
                    if(config.getBoolean("sounds.reproduce")) executeVanishSound(player);
                    if(config.getBoolean("titles.send")) {
                        title = config.getString("messages.events.vanish_enabled_title");
                        subtitle = config.getString("messages.events.vanish_enabled_subtitle");

                        title = Msg.color(title);
                        subtitle = Msg.color(subtitle);

                        in = config.getInt("titles.in");
                        show = config.getInt("titles.show");
                        out = config.getInt("titles.out");

                        hider.getVc().getMinecraftVersion().sendTitle(player, title, subtitle, in, show, out);
                    }
                    message = config.getString("messages.events.vanish_enabled");
                    message = message.replace(config.getString("prefix_identifier"), prefix);
                    player.sendMessage(Msg.color(message));
                    player.getOpenInventory().close();
                    return;
                }
                if(config.getBoolean("sounds.reproduce")) executePermissionSound(player);
                player.sendMessage(Msg.color(config.getString("messages.not_permission"))
                        .replace(config.getString("prefix_identifier"), Msg.color(prefix)));
                return;
            }

            event.setCancelled(true);
            return;
        }
    }

    private void executeOpenSound(Player player) {
        player.playSound(player.getLocation(), open, volume, pitch);
    }
    private void executePermissionSound(Player player) { player.playSound(player.getLocation(), permission, volume, pitch); }
    private void executeCooldownSound(Player player) { player.playSound(player.getLocation(), cooldown, volume, pitch); }
    private void executeHideSound(Player player) { player.playSound(player.getLocation(), hide, volume, pitch); }
    private void executeVanishSound(Player player) { player.playSound(player.getLocation(), vanish, volume, pitch); }
}
