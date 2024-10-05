package com.github.sarhatabaot.farmassistreboot.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.language.LanguageFile;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("far|farmassistreboot")
public class FarmAssistRebootCommand extends BaseCommand {
    private final FarmAssistReboot plugin;
    private final LanguageManager lm;

    public FarmAssistRebootCommand(FarmAssistReboot plugin, LanguageManager languageManager) {
        this.plugin = plugin;
        this.lm = languageManager;
    }

    @Subcommand("languages|lang")
    @CommandPermission("farmassist.player.language")
    @CommandCompletion("@languages")
    public void onLanguage(final Player player, final String language) {

    }

    @Subcommand("toggle")
    @CommandPermission("farmassist.player.toggle")
    public void onToggle(final Player player) {

    }

    @Subcommand("admin player toggle")
    @CommandPermission("farmassist.admin.toggle.player")
    public void onPlayerToggle(final CommandSender sender, final Player player) {

    }

    @Subcommand("admin global toggle")
    @CommandPermission("farmassist.admin.toggle.global")
    public void onGlobalToggle(final CommandSender sender) {
        this.plugin.getToggleManager().toggleGlobalStatus();
        if (plugin.getToggleManager().isGlobalStatus()) {
            sender.sendMessage(String.format(this.lm.getActiveLanguage().getToggleGlobalOn()));
        } else {
            sender.sendMessage(String.format(this.lm.getActiveLanguage().getToggleGlobalOff()));
        }
    }

    @Subcommand("admin version")
    @CommandPermission("farmassist.admin.version")
    public void onVersion(final CommandSender sender) {
        final LanguageFile languageFile = getLanguageForPlayerOrConsole(sender);
        sender.sendMessage(String.format(languageFile.getInfoVersion(), plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        sender.sendMessage(String.format(languageFile.getInfoMaintainers(), plugin.getDescription().getAuthors()));
    }

    @Subcommand("admin reload")
    @CommandPermission("farmassist.admin.reload")
    public void onReload(final CommandSender sender) {
        this.plugin.reload();
        final LanguageFile languageFile = getLanguageForPlayerOrConsole(sender);
        sender.sendMessage(Util.color(String.format("%s The plugin has been reloaded.", languageFile.getPrefix())));
    }

    private LanguageFile getLanguageForPlayerOrConsole(final CommandSender sender) {
        if (sender instanceof Player) {
            return lm.getPlayerLanguage((Player) sender);
        }

        return lm.getActiveLanguage();
    }
}
