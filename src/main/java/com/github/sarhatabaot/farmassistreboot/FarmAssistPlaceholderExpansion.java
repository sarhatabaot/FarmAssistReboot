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
    private final FarmAssistReboot plugin;

    public FarmAssistPlaceholderExpansion(final FarmAssistReboot plugin) {
        this.plugin = plugin;
    }


    @Override
    public @Nullable String onRequest(final OfflinePlayer player, @NotNull final String params) {
        if ("toggle".equalsIgnoreCase(params)) {
            return plugin.isGlobalEnabled() ? "enabled" : "disabled";
        }

        if (params.startsWith("player_")) {
            final String name = params.split("_")[1];
            if (name != null) {
                UUID uuid = getUuid(name);
                if (uuid == null)
                    return "";

                return plugin.getDisabledPlayerList().contains(uuid) ? "disabled" : "enabled";
            }
        }


        return null;
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
