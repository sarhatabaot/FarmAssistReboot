package com.github.sarhatabaot.farmassistreboot.placeholders;


import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Placeholder extends PlaceholderExpansion {
    private final FarmAssistReboot plugin;
    private final MainConfig mainConfig;

    public Placeholder(FarmAssistReboot plugin, MainConfig mainConfig) {
        this.plugin = plugin;
        this.mainConfig = mainConfig;
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
        return "2.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {

        switch (params) {
            case "server_toggle": {
                return String.valueOf(plugin.getToggleManager().isGlobalStatus());
            }
            case "player_toggle": {
                return String.valueOf(plugin.getToggleManager().getToggle(player.getUniqueId()));
            }
            default: {
                if (params.startsWith("player_toggle_uuid_")) {
                    return String.valueOf(plugin.getToggleManager().getToggle(UUID.fromString(params.split("player_toggle_uuid_")[1])));
                }
                if (params.startsWith("crop_disabled_")) {
                    return mainConfig.getDisabledCrops().contains(params.split("crop_disabled_")[1]) ? "true" : "false";
                }
                return null;
            }
        }
    }

    @Override
    public boolean persist() {
        return true;
    }
}
