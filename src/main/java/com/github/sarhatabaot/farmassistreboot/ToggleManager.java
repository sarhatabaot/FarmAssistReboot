package com.github.sarhatabaot.farmassistreboot;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class ToggleManager implements Listener {
    private boolean globalStatus;
    private Map<UUID, Boolean> toggles; //Current online players toggles, should be a cache.

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        //load from db if exists

    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        //if has set toggle
        //save to db
    }

    public void toggleGlobalStatus() {
        this.globalStatus = !this.globalStatus;
    }

    public boolean isGlobalStatus() {
        return globalStatus;
    }

    public boolean getToggle(final UUID uuid) {
        return this.toggles.getOrDefault(uuid, true);
    }

    public void togglePlayer(final UUID uuid) {
        this.toggles.put(uuid, !this.toggles.getOrDefault(uuid, true));
    }
}
