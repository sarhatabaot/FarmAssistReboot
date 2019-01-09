package io.github.sarhatabaot.commands;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandUpdate implements CommandExecutor {
    private FarmAssist plugin;

    public CommandUpdate(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("FarmAssist update")){
            if(!plugin.getConfig().getBoolean("Check for updates")){
                sender.sendMessage("Check for updates is disabled.");
                return true;
            }
            if(sender.hasPermission("farmassist.update")){
                sender.sendMessage("New update found: "+plugin.getNewVersion()+" You are running:"+plugin.getDescription().getVersion());
                sender.sendMessage("https://github.com/sarhatabaot/FarmAssistReboot/releases");
                return true;
            }
        }

        return false;
    }
}
