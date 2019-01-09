package io.github.sarhatabaot.commands;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {
    private FarmAssist plugin;

    public CommandReload(FarmAssist plugin) {
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
            sender.sendMessage(ChatColor.GREEN + "FarmAssist has been reloaded.");
            plugin.logger.warning("FarmAssist Reloaded.");
            return true;
        }
        return false;
    }
}
