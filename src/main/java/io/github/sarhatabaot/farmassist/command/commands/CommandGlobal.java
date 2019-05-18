package io.github.sarhatabaot.farmassist.command.commands;

import io.github.sarhatabaot.farmassist.FarmAssist;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandGlobal {
    private FarmAssist plugin;

    public CommandGlobal(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = "global",
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
