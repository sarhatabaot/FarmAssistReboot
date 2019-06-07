package com.github.sarhatabaot.farmassist.command.commands;

import com.github.sarhatabaot.commandmanager.Command;
import com.github.sarhatabaot.farmassist.FarmAssist;
import com.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import org.bukkit.command.CommandSender;

public class CommandUpdate {
    private FarmAssist plugin;

    public CommandUpdate(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"update","u"},
            description = "Check for updates.",
            permissions = "farmassist.update",
            usage = "/farmassist update"
    )
    public void update(CommandSender sender, String[] args) {
        if (!FarmAssistConfig.getInstance().getCheckForUpdates()) {
            sender.sendMessage("Check for updates is disabled.");
            return;
        }
        if (plugin.getNewVersion()!=null) {
            sender.sendMessage("New update found: " + plugin.getNewVersion());
            sender.sendMessage("Current version: " + plugin.getDescription().getVersion());
            sender.sendMessage("https://github.com/sarhatabaot/FarmAssistReboot/releases");
        }
        else {
            sender.sendMessage("You are running the latest version.");
        }
    }

}
