package org.alqj.dev.hider;

import org.alqj.dev.hider.color.Msg;
import org.alqj.dev.hider.commands.*;
import org.alqj.dev.hider.config.Config;
import org.alqj.dev.hider.controllers.ListenerController;
import org.alqj.dev.hider.controllers.VersionController;
import org.alqj.dev.hider.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iAlqjDV
 */
public final class HiderPlugin extends JavaPlugin {

    PluginDescriptionFile pdffile = getDescription();

    private final ConsoleCommandSender log = Bukkit.getConsoleSender();
    private final String VERSION_ID = "1.0.1";
    private final String AUTHOR = "iAlqjDV";

    private Config configuration;
    private Cooldown cooldown;
    private VersionController version;
    private ListenerController lc;
    private List<String> hiders;
    private List<String> vanish;

    public String getVersionID() { return VERSION_ID; }
    public String getAuthor() { return AUTHOR; }
    public PluginDescriptionFile getPDFFile() { return pdffile; }

    @Override
    public void onEnable() {
        long START_TIME = System.currentTimeMillis();

        log.sendMessage(Msg.color(""));
        log.sendMessage(Msg.color("&6  HiderPlayers"));
        log.sendMessage(Msg.color(""));
        log.sendMessage(Msg.color("&f Version: &a" + VERSION_ID));
        log.sendMessage(Msg.color("&f Author: &a" + AUTHOR));
        log.sendMessage(Msg.color(""));

        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch(ClassNotFoundException ex) {
            log.sendMessage(Msg.color("&c Could not found a Spigot jar, you're using a valid Spigot jar?"));
            log.sendMessage(Msg.color("&c The plugin will be deactivated."));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        configuration = new Config(this);
        setupCommands();
        cooldown = new Cooldown(this);
        hiders = new ArrayList<>();
        vanish = new ArrayList<>();
        lc = new ListenerController(this);

        version = new VersionController(this);

        log.sendMessage(Msg.color("&a Enabled in &e" + (System.currentTimeMillis() - START_TIME) + "ms&a."));
        log.sendMessage(Msg.color(""));
    }

    @Override
    public void onDisable() {}

    public void addPlayerToHide(final String uuid) { hiders.add(uuid); }

    public void removePlayerFromHide(final String uuid) { hiders.remove(uuid); }

    public boolean playerWithHide(final String uuid) { return hiders.contains(uuid); }

    public void addPlayerToVanishMode(final String uuid) { vanish.add(uuid); }

    public void removePlayerFromVanishMode(final String uuid) { vanish.remove(uuid); }

    public boolean playerWithVanishMode(final String uuid) { return vanish.contains(uuid); }

    public Cooldown getCooldown() { return cooldown; }

    public ListenerController getLc() { return lc; }

    public VersionController getVc() { return version; }

    public Config getConfiguration() { return configuration; }

    private void setupCommands() {
        getCommand("tools").setExecutor(new InventoryCommand(this));
        getCommand("hiderplayers").setExecutor(new PluginCommand(this));

        getCommand("hiderplayers").setTabCompleter(new TabComplete());
    }
}
