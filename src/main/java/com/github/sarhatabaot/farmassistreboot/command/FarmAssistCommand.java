package com.github.sarhatabaot.farmassistreboot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.messages.Commands;
import com.github.sarhatabaot.farmassistreboot.messages.InternalMessages;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@CommandAlias(Commands.BASE_COMMAND)
public class FarmAssistCommand extends BaseCommand {
	private final FarmAssistReboot plugin;

	@Default
	@Subcommand(Commands.TogglePlayer.SUB_COMMAND)
	@CommandPermission(Commands.TogglePlayer.PERMISSION)
	@Description(Commands.TogglePlayer.DESCRIPTION)
	public void onToggle(final @NotNull Player player){
		if (plugin.getDisabledPlayerList().contains(player.getUniqueId())) {
			plugin.getDisabledPlayerList().remove(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + InternalMessages.TogglePlayer.ON);
		} else {
			plugin.getDisabledPlayerList().add(player.getUniqueId());
			player.sendMessage(ChatColor.GREEN + InternalMessages.TogglePlayer.OFF);
		}
	}

	@Subcommand(Commands.ToggleGlobal.SUB_COMMAND)
	@CommandPermission(Commands.ToggleGlobal.PERMISSION)
	@Description(Commands.ToggleGlobal.DESCRIPTION)
	public void onGlobal(final CommandSender sender){
		if(!plugin.isGlobalEnabled()){
			plugin.setGlobalEnabled(true);
			sender.sendMessage(ChatColor.GREEN + InternalMessages.ToggleGlobal.ON);
		} else {
			plugin.setGlobalEnabled(false);
			sender.sendMessage(ChatColor.GREEN + InternalMessages.ToggleGlobal.OFF);
		}
	}

	@Subcommand(Commands.Info.SUB_COMMAND)
	@CommandPermission(Commands.Info.PERMISSION)
	@Description(Commands.Info.DESCRIPTION)
	public void onInfo(final @NotNull CommandSender sender){
		sender.sendMessage(String.format(InternalMessages.Info.VERSION,plugin.getName(),plugin.getDescription().getVersion()));
		sender.sendMessage(String.format(InternalMessages.Info.MAINTAINERS, plugin.getDescription().getAuthors()));
	}

	@Subcommand(Commands.Update.SUB_COMMAND)
	@CommandPermission(Commands.Update.PERMISSION)
	@Description(Commands.Update.DESCRIPTION)
	public void onUpdate(final CommandSender sender){
		if(!plugin.isNeedsUpdate()) {
			sender.sendMessage(String.format(InternalMessages.Update.RUNNING_LATEST_VERSION ,plugin.getDescription().getVersion()));
			return;
		}

		sender.sendMessage(String.format(InternalMessages.Update.NEW_UPDATE,plugin.getNewVersion(),plugin.getDescription().getVersion()));
		sender.sendMessage(String.format(InternalMessages.Update.GET_NEW_UPDATE,plugin.getDescription().getWebsite()));
	}

	@Subcommand(Commands.Reload.SUB_COMMAND)
	@CommandPermission(Commands.Reload.PERMISSION)
	@Description(Commands.Reload.DESCRIPTION)
	public void onReload(final @NotNull CommandSender sender){
		plugin.getAssistConfig().reloadConfig();
		sender.sendMessage(ChatColor.GREEN + InternalMessages.Reload.COMMAND);
		plugin.debug(FarmAssistCommand.class,InternalMessages.Reload.DEBUG);
	}
}
