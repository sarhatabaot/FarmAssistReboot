package io.github.sarhatabaot.commands;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandToggle implements CommandExecutor {
    private FarmAssist plugin;

    public CommandToggle(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("Toggle")) {
            if (!sender.hasPermission("farmassist.toggle")) {
                sender.sendMessage("You don't have Farmassist.toggle");
                return true;
            }

            if (plugin.disabledPlayerList.contains(sender.getName())) {
                plugin.disabledPlayerList.remove(sender.getName());
                sender.sendMessage(ChatColor.GREEN + "FarmAssist functions are now on for you!");
            } else {
                plugin.disabledPlayerList.add(sender.getName());
                sender.sendMessage(ChatColor.GREEN + "FarmAssist functions turned off for you!");
            }

            return true;
        }
        return false;
    }
}
