package io.github.sarhatabaot.farmassistreboot.command.commands;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandReload {
    private FarmAssistReboot plugin;

    public CommandReload(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = "reload",
            description = "Reload FarmAssistReboot",
            permissions ="farmassist.reload"
    )
    public void reload(CommandSender sender, String[] args){
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot has been reloaded.");
        FarmAssistReboot.debug("FarmAssistReboot Reloaded.");
    }
}
