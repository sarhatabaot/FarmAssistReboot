package com.github.sarhatabaot.farmassist.command.commands;

import com.github.sarhatabaot.commandmanager.Command;
import com.github.sarhatabaot.farmassist.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandToggle {
    private FarmAssist plugin;

    public CommandToggle(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"toggle","t"},
            description = "Lets a player turn off FarmAssist features",
            permissions = "farmassist.toggle",
            usage = "/farmassist global"
    )
    public void toggle(CommandSender sender, String[] args){
        if (plugin.getDisabledPlayerList().contains(sender.getName())) {
            plugin.getDisabledPlayerList().remove(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions are now on for you!");
        } else {
            plugin.getDisabledPlayerList().add(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions turned off for you!");
        }
    }

}
