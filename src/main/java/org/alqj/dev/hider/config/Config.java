package org.alqj.dev.hider.config;

import org.alqj.dev.hider.HiderPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private final HiderPlugin hider;

    private File cfile;
    private FileConfiguration config;

    public Config(HiderPlugin hider) {
        this.hider = hider;
        createFiles();
        config = loadConfiguration();
    }

    private void createFiles() {
        cfile = new File("plugins/HiderPlayers", "config.yml");
        if(!cfile.exists()) hider.saveResource("config.yml", false);
    }

    private FileConfiguration loadConfiguration() { return YamlConfiguration.loadConfiguration(cfile); }

    public void saveFile() {
        try {
            config.save(cfile);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() { config = YamlConfiguration.loadConfiguration(cfile); }

    public FileConfiguration getConfig() { return config; }
}
