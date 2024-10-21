package com.github.sarhatabaot.farmassistreboot;


import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class ToggleManager implements Listener {
    private boolean globalStatus;
    private final Map<UUID, Boolean> toggles; //Current online players toggles, should be a cache.
    private final Map<UUID, Set<String>> worldToggles; //Current online players toggles per world. If it's in this list, it's toggled off.

    public ToggleManager() {
        this.globalStatus = true;
        this.toggles = new HashMap<>();
        this.worldToggles = new HashMap<>();
    }

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

    public void togglePlayer(final UUID uuid, final World world) {
        this.worldToggles.computeIfAbsent(uuid, uuid1 -> new HashSet<>());
        this.worldToggles.get(uuid).add(world.getName());
        //todo obv, this should be a cache that never expires unless the player logs out
        // when the player logs out, before the cache is cleared, their info should be saved to the db
    }

    public void save() {
        //save to db
        //todo
    }
}
