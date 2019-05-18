package io.github.sarhatabaot.farmassistreboot.command.commands;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.command.Command;
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
            permissions = "farmassist.toggle",
            usage = "/farmassist toggle"
    )
    public void toggle(CommandSender sender, String[] args){
        if (plugin.getDisabledPlayerList().contains(sender.getName())) {
            plugin.getDisabledPlayerList().remove(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions are now on for you!");
        } else {
            plugin.getDisabledPlayerList().add(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot functions turned off for you!");
        }
    }

}
