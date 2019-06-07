package com.github.sarhatabaot.farmassist.command.commands;

import com.github.sarhatabaot.commandmanager.Command;
import com.github.sarhatabaot.farmassist.FarmAssist;
import com.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandReload {
    @Command(
            aliases = {"reload","r"},
            description = "reload the config",
            permissions = "farmassist.reload"
    )
    public void reload(CommandSender sender, String[] args){
        FarmAssistConfig.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "FarmAssist has been reloaded.");
        FarmAssist.debug("FarmAssist Reloaded.");
    }
}
