package org.alqj.dev.hider.minecraft.versions;

import org.alqj.dev.hider.minecraft.Minecraft;
import org.bukkit.entity.Player;

public class Minecraft_v1_18_r1 implements Minecraft {

    @Override
    public void sendTitle(Player player, String title, String subtitle, int in, int show, int out) {
        player.sendTitle(title, subtitle, in, show, out);
    }
}
