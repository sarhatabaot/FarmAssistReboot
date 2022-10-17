package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.messages.InternalMessages;
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
	public void onPlayerJoin(final @NotNull PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if(!player.hasPermission(Permissions.UPDATE_NOTIFY))
			return;

		if(!plugin.isNeedsUpdate()) {
			player.sendMessage(String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateLatestVersion() ,plugin.getDescription().getVersion()));
			return;
		}

		player.sendMessage(String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateNew(),plugin.getNewVersion(),plugin.getDescription().getVersion()));
		player.sendMessage(String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateGetNew(),plugin.getDescription().getWebsite()));
	}
}
