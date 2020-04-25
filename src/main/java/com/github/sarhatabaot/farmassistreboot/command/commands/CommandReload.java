package com.github.sarhatabaot.farmassistreboot.command.commands;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.command.Command;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
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
        FarmAssistConfig.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot has been reloaded.");
        FarmAssistReboot.debug("FarmAssistReboot Reloaded.");
    }
}
