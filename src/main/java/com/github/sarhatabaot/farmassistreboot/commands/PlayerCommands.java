package com.github.sarhatabaot.farmassistreboot.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.ToggleManager;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("%far_main_command")
public class PlayerCommands extends BaseCommand {
    private final FarmAssistReboot plugin;
    private final LanguageManager lm;
    private final ToggleManager toggleManager;

    public PlayerCommands(@NotNull FarmAssistReboot plugin, LanguageManager languageManager) {
        this.plugin = plugin;
        this.lm = languageManager;
        this.toggleManager = plugin.getToggleManager();
    }


    @Subcommand("languages|lang")
    @CommandPermission("farmassist.player.language")
    @CommandCompletion("@languages")
    public void onLanguage(final Player player, @Optional final String language) {
        if (language == null || language.isEmpty()) {
            Util.sendMessage(player, "Current active language: %s", lm.getPlayerLanguage(player));
            return;
        }

        if (lm.isNotSupported(language)) {
            Util.sendMessage(player, "Language %s is not supported", language);
            return;
        }

        lm.setPlayerLanguage(player, language);
        Util.sendMessage(player, "Set active language to: %s", lm.getPlayerLanguage(player));
    }

    @Subcommand("toggle")
    @CommandPermission("farmassist.player.toggle")
    @CommandCompletion("@worlds")
    public void onToggle(final Player player, @Optional final String world) {

        //todo, merge this with the admin command, or just move the checks somewhere
        if (world == null || world.isEmpty()) {
            toggleManager.togglePlayer(player.getUniqueId(), player.getWorld());
            //send a message toggling that world
            return;
        }

        if (isNotWorld(world)) {
            Util.sendMessage(player,"World %s is not a valid world", world);
            return;
        }

        toggleManager.togglePlayer(player.getUniqueId(), Bukkit.getWorld(world));
        // send a message here todo
    }

    private boolean isNotWorld(final String world) {
        return Bukkit.getWorld(world) == null;
    }

    @Subcommand("toggle-all")
    @CommandPermission("farmassist.player.toggle.all")
    public void onToggleAll(final @NotNull Player player) {
        toggleManager.togglePlayer(player.getUniqueId());
        boolean toggleState = toggleManager.getToggle(player.getUniqueId());
        if (toggleState) {
            Util.sendMessage(player,lm.getPlayerLanguage(player).getTogglePlayerOn());
        } else {
            Util.sendMessage(player,lm.getPlayerLanguage(player).getTogglePlayerOff());
        }
    }
}
