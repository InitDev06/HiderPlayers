package org.alqj.dev.hider.minecraft.versions;

import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.alqj.dev.hider.minecraft.Minecraft;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Minecraft_v1_16_r3 implements Minecraft {

    @Override
    public void sendTitle(Player player, String title, String subtitle, int in, int show, int out) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, in, show, out);
        connection.sendPacket(titlePacket);
        if(title != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle titleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, component);
            connection.sendPacket(titleInfo);
        }

        if(subtitle != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle subtitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, component);
            connection.sendPacket(subtitleInfo);
        }
    }
}
