package io.github.sarhatabaot.farmassistreboot.commands;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {
    private FarmAssistReboot plugin;

    public CommandReload(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("Reload")) {
            if (!sender.hasPermission("farmassist.reload")) {
                sender.sendMessage("You don't have Farmassist.reload");
                return true;
            }

            plugin.loadYamls();
            sender.sendMessage(ChatColor.GREEN + "FarmAssistReboot has been reloaded.");
            plugin.logger.warning("FarmAssistReboot Reloaded.");
            return true;
        }
        return false;
    }
}
