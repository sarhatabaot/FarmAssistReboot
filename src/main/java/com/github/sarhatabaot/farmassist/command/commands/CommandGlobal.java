package com.github.sarhatabaot.farmassist.command.commands;

import com.github.sarhatabaot.commandmanager.Command;
import com.github.sarhatabaot.farmassist.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandGlobal {
    private FarmAssist plugin;

    public CommandGlobal(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"global","g"},
            description = "turn farmassist off/on globally",
            permissions = "farmassist.toggle.global",
            usage = "/farmassist global"
    )
    public void global(CommandSender sender, String[] args){
        if(!plugin.isGlobalEnabled()){
            plugin.setGlobalEnabled(true);
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions are globally back on!");
        } else {
            plugin.setGlobalEnabled(false);
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions turned off globally!");
        }
    }

}
