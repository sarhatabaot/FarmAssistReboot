package com.github.sarhatabaot.farmassistreboot.command.commands;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandToggle {
    private FarmAssistReboot plugin;

    public CommandToggle(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = "toggle",
            description = "toggle farmassist",
            help="toggle usage of FarmAssist",
            permissions = "farmassist.toggle"
    )
    public void toggle(CommandSender sender, String[] args){
        if (plugin.disabledPlayerList.contains(sender.getName())) {
            plugin.disabledPlayerList.remove(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are now on for you!");
        } else {
            plugin.disabledPlayerList.add(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off for you!");
        }
    }

}
