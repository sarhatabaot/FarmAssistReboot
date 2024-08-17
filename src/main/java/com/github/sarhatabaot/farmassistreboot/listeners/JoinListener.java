package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.lang.LanguageFile;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class JoinListener implements Listener {
    private final FarmAssistReboot plugin;

    public JoinListener(final FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final LanguageFile activeLang = plugin.getLanguageManager().getActiveLanguage();
		if (!player.hasPermission(Permissions.UPDATE_NOTIFY)) {
			return;
		}

        if (!plugin.isNeedsUpdate() && !FarmAssistConfig.DISABLE_LATEST_VERSION) {
            Util.sendMessage(player, String.format(activeLang.getUpdateLatestVersion(), plugin.getDescription().getVersion()));
            return;
        }

        Util.sendMessage(player, String.format(activeLang.getUpdateNew(), plugin.getNewVersion(), plugin.getDescription().getVersion()));
        Util.sendMessage(player, String.format(activeLang.getUpdateGetNew(), plugin.getDescription().getWebsite()));
    }
}
