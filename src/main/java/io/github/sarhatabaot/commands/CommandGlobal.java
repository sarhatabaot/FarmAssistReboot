package io.github.sarhatabaot.commands;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGlobal implements CommandExecutor {
    private FarmAssist plugin;

    public CommandGlobal(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("Global")) {
            if (!sender.hasPermission("farmassist.toggle.global")) {
                sender.sendMessage("You don't have Farmassist.toggle.global");
                return true;
            }

            if (!plugin.isGlobalEnabled()) {
                plugin.setGlobalEnabled(true);
                sender.sendMessage(ChatColor.GREEN + "FarmAssist functions are globally back on!");
            } else {
                plugin.setGlobalEnabled(false);
                sender.sendMessage(ChatColor.GREEN + "FarmAssist functions turned off globally!");
            }

            return true;
        }
        return false;
    }
}
