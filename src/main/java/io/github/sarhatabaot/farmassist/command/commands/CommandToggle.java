package io.github.sarhatabaot.farmassist.command.commands;

import io.github.sarhatabaot.farmassist.FarmAssist;
import io.github.sarhatabaot.farmassist.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandToggle {
    private FarmAssist plugin;

    public CommandToggle(FarmAssist plugin) {
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
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions are now on for you!");
        } else {
            plugin.getDisabledPlayerList().add(sender.getName());
            sender.sendMessage(ChatColor.GREEN + "FarmAssist functions turned off for you!");
        }
    }

}
