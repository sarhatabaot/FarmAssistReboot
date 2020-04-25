package com.github.sarhatabaot.farmassistreboot.command.commands;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandGlobal {
    private FarmAssistReboot plugin;

    public CommandGlobal(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = "global",
            description = "turn farmassist off/on globally",
            permissions = "farmassist.toggle.global"
    )
    public void global(CommandSender sender, String[] args){
        if(!plugin.isGlobalEnabled()){
            plugin.setGlobalEnabled(true);
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are globally back on!");
        } else {
            plugin.setGlobalEnabled(false);
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off globally!");
        }
    }

}
