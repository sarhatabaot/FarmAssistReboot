package com.github.sarhatabaot.farmassistreboot.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.language.LanguageFile;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("%far_main_command admin")
public class AdminCommands extends BaseCommand {
    private final FarmAssistReboot plugin;
    private final LanguageManager lm;

    public AdminCommands(FarmAssistReboot plugin, LanguageManager languageManager) {
        this.plugin = plugin;
        this.lm = languageManager;
    }


    @Subcommand("player toggle")
    @CommandPermission("farmassist.admin.toggle.player")
    @CommandCompletion("@players @worlds")
    public void onPlayerToggle(final CommandSender sender, final Player player, @Optional String world) {
        World finalWorld;
        if (world == null) {
            finalWorld = player.getWorld();
        } else {
            finalWorld = Bukkit.getWorld(world);
        }
        plugin.getToggleManager().togglePlayer(player.getUniqueId(), finalWorld);
        sender.sendMessage("Toggled the state for player " + player.getName() + " in world " + finalWorld.getName());
    }

    @Subcommand("player toggle all")
    @CommandPermission("farmassist.admin.toggle.player")
    public void onPlayerToggle(final CommandSender sender, final Player player) {
        plugin.getToggleManager().togglePlayer(player.getUniqueId());
        sender.sendMessage("Toggled the state for player " + player.getName());
    }

    @Subcommand("global toggle")
    @CommandPermission("farmassist.admin.toggle.global")
    public void onGlobalToggle(final CommandSender sender) {
        this.plugin.getToggleManager().toggleGlobalStatus();
        if (plugin.getToggleManager().isGlobalStatus()) {
            sender.sendMessage(String.format(this.lm.getActiveLanguage().getToggleGlobalOn()));
        } else {
            sender.sendMessage(String.format(this.lm.getActiveLanguage().getToggleGlobalOff()));
        }
    }

    @Subcommand("version")
    @CommandPermission("farmassist.admin.version")
    public void onVersion(final CommandSender sender) {
        final LanguageFile languageFile = getLanguageForPlayerOrConsole(sender);
        sender.sendMessage(String.format(languageFile.getInfoVersion(), plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        sender.sendMessage(String.format(languageFile.getInfoMaintainers(), plugin.getDescription().getAuthors()));
    }

    @Subcommand("reload")
    @CommandPermission("farmassist.admin.reload")
    public void onReload(final CommandSender sender) {
        this.plugin.reload();
        final LanguageFile languageFile = getLanguageForPlayerOrConsole(sender);
        sender.sendMessage(Util.color(String.format("%s The plugin has been reloaded.", languageFile.getPrefix())));
    }

    @Subcommand("locales")
    @CommandPermission("farmassist.admin.locales")
    public void onLocales(final CommandSender sender) {
        sender.sendMessage("Global Locale: " + lm.getActiveLanguage().getLocale());
        sender.sendMessage("Player locales: ");
        for (final Player player: Bukkit.getOnlinePlayers()) {
            sender.sendMessage(player.getName() + " - " + lm.getPlayerLanguage(player).getLocale());
        }
    }

    private LanguageFile getLanguageForPlayerOrConsole(final CommandSender sender) {
        if (sender instanceof Player) {
            return lm.getPlayerLanguage((Player) sender);
        }

        return lm.getActiveLanguage();
    }
}
