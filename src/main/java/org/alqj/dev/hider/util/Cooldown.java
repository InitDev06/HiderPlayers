package org.alqj.dev.hider.util;

import org.alqj.dev.hider.HiderPlugin;

import java.util.HashMap;

/**
 * @author iAlqjDV
 */
public class Cooldown {

    private final HiderPlugin hider;
    private final HashMap<String, Integer> cooldowns;

    public Cooldown(HiderPlugin hider) {
        this.hider = hider;
        this.cooldowns = new HashMap<>();
    }

    public void setCooldown(final String uuid) { cooldowns.put(uuid, (int) System.currentTimeMillis() / 1000); }

    public boolean hasCooldown(final String uuid) {
        if(cooldowns.containsKey(uuid)) {
            if(((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid) < hider.getConfiguration().getConfig().getInt("cooldown_time")) return true;
        }
        return false;
    }

    public void removeCooldown(final String uuid) { cooldowns.remove(uuid); }

    public Integer getCooldown(final String uuid) {
        if(!cooldowns.containsKey(uuid)) return null;
        return hider.getConfiguration().getConfig().getInt("cooldown_time") - (((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid));
    }
}
