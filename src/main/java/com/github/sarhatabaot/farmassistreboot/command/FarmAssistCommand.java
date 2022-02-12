package com.github.sarhatabaot.farmassistreboot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@CommandAlias("farmassist|fa")
public class FarmAssistCommand extends BaseCommand {
	private final FarmAssistReboot plugin;

	@Default
	@Subcommand("toggle")
	@CommandPermission("farmassist.toggle")
	@Description("toggle usage of FarmAssist")
	public void onToggle(final @NotNull Player player){
		if (plugin.getDisabledPlayerList().contains(player.getUniqueId())) {
			plugin.getDisabledPlayerList().remove(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are now on for you!");
		} else {
			plugin.getDisabledPlayerList().add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off for you!");
		}
	}

	@Subcommand("global|g")
	@CommandPermission("farmassist.toggle.global")
	@Description("turn farmassist off/on globally")
	public void onGlobal(final CommandSender sender){
		if(!plugin.isGlobalEnabled()){
			plugin.setGlobalEnabled(true);
			sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are globally back on!");
		} else {
			plugin.setGlobalEnabled(false);
			sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off globally!");
		}
	}

	@Subcommand("info|about")
	@CommandPermission("farmassist.info")
	public void onInfo(final @NotNull CommandSender sender){
		sender.sendMessage(String.format("%s version: %s",plugin.getName(),plugin.getDescription().getVersion()));
		sender.sendMessage(String.format("Maintainers: %s", plugin.getDescription().getAuthors()));
	}

	@Subcommand("update|u")
	@CommandPermission("farmassist.update")
	public void onUpdate(final CommandSender sender){
		if(!plugin.isNeedsUpdate()) {
			sender.sendMessage(String.format("You are running the latest version: %s" ,plugin.getDescription().getVersion()));
			return;
		}

		sender.sendMessage(String.format("New update: %s Current version: %s",plugin.getNewVersion(),plugin.getDescription().getVersion()));
		sender.sendMessage(String.format("Get at %s",plugin.getDescription().getWebsite()));
	}

	@Subcommand("reload")
	@CommandPermission("farmassist.reload")
	@Description("Reload FarmAssistReboot")
	public void onReload(final @NotNull CommandSender sender){
		plugin.getAssistConfig().reloadConfig();
		sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot has been reloaded.");
		FarmAssistReboot.debug("FarmAssistReboot Reloaded.");
	}
}
