package io.github.sarhatabaot.farmassist.command.commands;

import io.github.sarhatabaot.farmassist.command.Command;
import io.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static io.github.sarhatabaot.farmassist.FarmAssist.debug;

public class CommandReload {
    @Command(
            aliases = "reload",
            description = "Reload FarmAssist",
            permissions ="farmassist.reload",
            usage="/farmassist reload"
    )
    public void reload(CommandSender sender, String[] args){
        FarmAssistConfig.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "FarmAssist has been reloaded.");
        debug("FarmAssist Reloaded.");
    }
}
