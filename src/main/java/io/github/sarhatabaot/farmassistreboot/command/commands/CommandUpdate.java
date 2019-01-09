package io.github.sarhatabaot.farmassistreboot.command.commands;

import io.github.sarhatabaot.farmassistreboot.Config;
import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.command.Command;
import org.bukkit.command.CommandSender;

public class CommandUpdate {
    private FarmAssistReboot plugin;

    public CommandUpdate(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = "updates",
            description = "check for updates",
            permissions = "farmassist.update"
    )
    public void update(CommandSender sender, String[] args) {
        if (!Config.isCheckForUpdates()) {
            sender.sendMessage("Check for updates is disabled.");
            return;
        }
        sender.sendMessage("New update found: " + plugin.getNewVersion() + " You are running:" + plugin.getDescription().getVersion());
        sender.sendMessage("https://github.com/sarhatabaot/FarmAssistReboot/releases");
    }

}
