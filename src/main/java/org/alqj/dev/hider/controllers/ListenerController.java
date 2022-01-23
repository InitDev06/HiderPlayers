package org.alqj.dev.hider.controllers;

import org.alqj.dev.hider.HiderPlugin;
import org.alqj.dev.hider.menu.ToolsGUI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerController {

    private final HiderPlugin hider;

    public ToolsGUI TOOLS_GUI;

    public ListenerController(HiderPlugin hider) {
        this.hider = hider;
        setupListeners();
    }

    private void setupListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(TOOLS_GUI = new ToolsGUI(hider), hider);
    }
}
