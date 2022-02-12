package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
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
		if(!player.hasPermission("farmassist.notify.update"))
			return;

		if(!plugin.isNeedsUpdate()) {
			player.sendMessage(String.format("You are running the latest version: %s" ,plugin.getDescription().getVersion()));
			return;
		}

		player.sendMessage(String.format("New update: %s Current version: %s",plugin.getNewVersion(),plugin.getDescription().getVersion()));
		player.sendMessage(String.format("Get at %s",plugin.getDescription().getWebsite()));
	}
}
