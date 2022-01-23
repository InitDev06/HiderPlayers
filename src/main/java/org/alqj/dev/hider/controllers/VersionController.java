package org.alqj.dev.hider.controllers;

import org.alqj.dev.hider.HiderPlugin;
import org.alqj.dev.hider.color.Msg;
import org.alqj.dev.hider.minecraft.Minecraft;
import org.alqj.dev.hider.minecraft.versions.*;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class VersionController {

    private final HiderPlugin hider;
    private final ConsoleCommandSender log;

    private Minecraft minecraft;

    public VersionController(HiderPlugin hider) {
        this.hider = hider;
        this.log = Bukkit.getConsoleSender();
        setupVersion();
    }

    private void setupVersion() {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

            switch(version) {
                case "v1_8_R3":
                    minecraft = new Minecraft_v1_8_r3();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.8_R3"));
                    return;
                case "v1_9_R2":
                    minecraft = new Minecraft_v1_9_r2();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.9_R2"));
                    return;
                case "v1_10_R1":
                    minecraft = new Minecraft_v1_10_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.10_R1"));
                    return;
                case "v1_11_R1":
                    minecraft = new Minecraft_v1_11_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.11_R1"));
                    return;
                case "v1_12_R1":
                    minecraft = new Minecraft_v1_12_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.12_R1"));
                    return;
                case "v1_13_R2":
                    minecraft = new Minecraft_v1_13_r2();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.13_R2"));
                    return;
                case "v1_14_R1":
                    minecraft = new Minecraft_v1_14_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.14_R1"));
                    return;
                case "v1_15_R1":
                    minecraft = new Minecraft_v1_15_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.15_R1"));
                    return;
                case "v1_16_R1":
                    minecraft = new Minecraft_v1_16_r3();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.16_R1"));
                    return;
                case "v1_17_R1":
                    minecraft = new Minecraft_v1_17_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.17_R1"));
                    return;
                case "v1_18_R1":
                    minecraft = new Minecraft_v1_18_r1();
                    log.sendMessage(Msg.color("&f Minecraft: &b1.18_R1"));
                    return;
            }
            log.sendMessage(Msg.color("&c You're using a server version not supported, please install any version with compatibility."));
            log.sendMessage(Msg.color("&c The plugin will be deactivated."));
            Bukkit.getPluginManager().disablePlugin(hider);
        } catch(ArrayIndexOutOfBoundsException ex) {
            log.sendMessage(Msg.color("&c An occurred a error to check your server version, per the moment this is unknown"));
            log.sendMessage(Msg.color("&c The plugin will be deactivated."));
            Bukkit.getPluginManager().disablePlugin(hider);
        }
    }

    public Minecraft getMinecraftVersion() { return minecraft; }
}
