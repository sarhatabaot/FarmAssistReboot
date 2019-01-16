package io.github.sarhatabaot.farmassistreboot.command.commands;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.command.Command;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.command.CommandSender;

public class CommandUpdate {
    private FarmAssistReboot plugin;

    public CommandUpdate(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"updates","update"},
            description = "check for updates",
            permissions = "farmassist.update"
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
