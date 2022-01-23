package org.alqj.dev.hider.minecraft;

import org.bukkit.entity.Player;

public interface Minecraft {

    void sendTitle(Player player, String title, String subtitle, int in, int show, int out);

}
