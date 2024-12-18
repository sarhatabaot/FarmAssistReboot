package com.github.sarhatabaot.farmassistreboot.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.messages.Commands;
import com.github.sarhatabaot.farmassistreboot.messages.InternalMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias(Commands.BASE_COMMAND)
public class FarmAssistCommand extends BaseCommand {
    private final FarmAssistReboot plugin;

    public FarmAssistCommand(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Default
    @Subcommand(Commands.TogglePlayer.SUB_COMMAND)
    @CommandPermission(Commands.TogglePlayer.PERMISSION)
    @Description(Commands.TogglePlayer.DESCRIPTION)
    public void onToggle(final @NotNull Player player) {
        if (plugin.getDisabledPlayerList().contains(player.getUniqueId())) {
            plugin.getDisabledPlayerList().remove(player.getUniqueId());
            Util.sendMessage(player, ChatColor.GREEN + plugin.getLanguageManager().getActiveLanguage().getTogglePlayerOn());
        } else {
            plugin.getDisabledPlayerList().add(player.getUniqueId());
            Util.sendMessage(player, ChatColor.GREEN + plugin.getLanguageManager().getActiveLanguage().getTogglePlayerOff());
        }
    }

    @Subcommand(Commands.ToggleGlobal.SUB_COMMAND)
    @CommandPermission(Commands.ToggleGlobal.PERMISSION)
    @Description(Commands.ToggleGlobal.DESCRIPTION)
    public void onGlobal(final CommandSender sender) {
        if (!plugin.isGlobalEnabled()) {
            plugin.setGlobalEnabled(true);
            Util.sendMessage(sender, ChatColor.GREEN + plugin.getLanguageManager().getActiveLanguage().getToggleGlobalOn());
        } else {
            plugin.setGlobalEnabled(false);
            Util.sendMessage(sender, ChatColor.GREEN + plugin.getLanguageManager().getActiveLanguage().getToggleGlobalOff());
        }
    }

    @Subcommand(Commands.Info.SUB_COMMAND)
    @CommandPermission(Commands.Info.PERMISSION)
    @Description(Commands.Info.DESCRIPTION)
    public void onInfo(final @NotNull CommandSender sender) {
        Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getInfoVersion(), plugin.getName(), plugin.getDescription().getVersion()));
        Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getInfoMaintainers(), plugin.getDescription().getAuthors()));
    }

    @Subcommand(Commands.Update.SUB_COMMAND)
    @CommandPermission(Commands.Update.PERMISSION)
    @Description(Commands.Update.DESCRIPTION)
    public void onUpdate(final CommandSender sender) {
        if (plugin.doesNotNeedUpdate()) {
            Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateLatestVersion(), plugin.getDescription().getVersion()));
            return;
        }

        Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateNew(), plugin.getNewVersion(), plugin.getDescription().getVersion()));
        Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateGetNew(), plugin.getDescription().getWebsite()));
    }

    @Subcommand(Commands.Reload.SUB_COMMAND)
    @CommandPermission(Commands.Reload.PERMISSION)
    @Description(Commands.Reload.DESCRIPTION)
    public void onReload(final @NotNull CommandSender sender) {
        plugin.getAssistConfig().reload();
        plugin.getLanguageManager().getActiveLanguage().reloadConfig();
        Util.sendMessage(sender,ChatColor.GREEN + InternalMessages.Reload.COMMAND);
        plugin.debug(FarmAssistCommand.class, InternalMessages.Reload.DEBUG);
    }

    @Subcommand(Commands.Language.SUB_COMMAND)
    @CommandPermission(Commands.Language.PERMISSION)
    @Description(Commands.Language.DESCRIPTION)
    @CommandCompletion("@supported-lang")
    public void onLanguages(final @NotNull CommandSender sender, final String locale) {
        if (!plugin.getLanguageManager().isSupported(locale)) {
            Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getLangUnsupported(), locale));
            return;
        }

        final String previousLocale = plugin.getLanguageManager().getActiveLanguage().getLocale();
        plugin.getLanguageManager().switchLanguages(locale);
        plugin.getAssistConfig().setActiveLanguage(locale);

        Util.sendMessage(sender, String.format(plugin.getLanguageManager().getActiveLanguage().getLangChanged(), previousLocale, locale));
    }

}
