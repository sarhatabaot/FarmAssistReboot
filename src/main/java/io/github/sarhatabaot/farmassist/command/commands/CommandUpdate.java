package io.github.sarhatabaot.farmassist.command.commands;

import io.github.sarhatabaot.farmassist.FarmAssist;
import io.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import org.bukkit.command.CommandSender;

public class CommandUpdate {
    private FarmAssist plugin;

    public CommandUpdate(FarmAssist plugin) {
        this.plugin = plugin;
    }

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
