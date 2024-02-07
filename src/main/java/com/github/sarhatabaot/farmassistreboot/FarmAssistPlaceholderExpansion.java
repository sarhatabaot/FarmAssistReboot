package com.github.sarhatabaot.farmassistreboot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author sarhatabaot
 */
public class FarmAssistPlaceholderExpansion extends PlaceholderExpansion {
    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";
    private final FarmAssistReboot plugin;
    
    public FarmAssistPlaceholderExpansion(final FarmAssistReboot plugin) {
        this.plugin = plugin;
    }
    
    
    @Override
    public @Nullable String onRequest(final OfflinePlayer player, @NotNull final String params) {
        if ("toggle".equalsIgnoreCase(params)) {
            return plugin.isGlobalEnabled() ? ENABLED : DISABLED;
        }
        
        if ("player_toggle".equalsIgnoreCase(params)) {
            if(player == null) {
                return "PLAYER IS NULL";
            }
            
            UUID uuid = player.getUniqueId();
            return isFarmAssistEnabledForUuid(uuid);
        }
        
        if (params.startsWith("player_")) {
            final String name = params.split("_")[1];
            if (name != null) {
                UUID uuid = getUuid(name);
                if (uuid == null)
                    return "";
                
                return isFarmAssistEnabledForUuid(uuid);
            }
        }
        
        
        return null;
    }
    private String isFarmAssistEnabledForUuid(UUID uuid) {
        return plugin.getDisabledPlayerList().contains(uuid) ? DISABLED : ENABLED;
    }
    
    
    @Override
    public @NotNull String getIdentifier() {
        return "farmassist";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "sarhatabaot";
    }
    
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Contract(pure = true)
    private boolean isUuid(final @NotNull String string) {
        return string.split("-").length == 5;
    }
    
    private @Nullable UUID getUuid(final String potential) {
        if (isUuid(potential)) {
            return UUID.fromString(potential);
        }
        
        Player player = Bukkit.getPlayer(potential);
        if (player == null)
            return null;
        
        return player.getUniqueId();
    }
    
}
