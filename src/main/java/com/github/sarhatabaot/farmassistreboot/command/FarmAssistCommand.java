package com.github.sarhatabaot.farmassistreboot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("farmassist|fa")
public class FarmAssistCommand extends BaseCommand {
	private final FarmAssistReboot plugin;

	@Default
	@CommandAlias("toggle")
	@CommandPermission("farmassist.toggle")
	@Description("toggle usage of FarmAssist")
	public void onToggle(final Player player){
		if (plugin.getDisabledPlayerList().contains(player.getUniqueId())) {
			plugin.getDisabledPlayerList().remove(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are now on for you!");
		} else {
			plugin.getDisabledPlayerList().add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off for you!");
		}
	}

	@CommandAlias("global|g")
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

	@CommandAlias("info|about")
	@CommandPermission("farmassist.info")
	public void onInfo(final CommandSender sender){
		sender.sendMessage(String.format("%s version: %s",plugin.getName(),plugin.getDescription().getVersion()));
		sender.sendMessage(String.format("Maintainers: %s", plugin.getDescription().getAuthors()));
	}

	@CommandAlias("reload")
	@CommandPermission("farmassist.reload")
	@Description("Reload FarmAssistReboot")
	public void onReload(final CommandSender sender){
		plugin.getAssistConfig().reloadConfig();
		sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot has been reloaded.");
		FarmAssistReboot.debug("FarmAssistReboot Reloaded.");
	}
}
