package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.BukkitCommandManager;
import com.github.sarhatabaot.farmassistreboot.command.CommandManager;
import com.github.sarhatabaot.farmassistreboot.command.FarmAssistCommand;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandGlobal;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandReload;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandToggle;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author sarhatabaot
 */

public class FarmAssistReboot extends JavaPlugin {
    private static FarmAssistReboot instance;
    private Logger logger = getLogger();

    public List<String> disabledPlayerList = new ArrayList<>();

    // State
    private boolean enabled;



    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        new FarmAssistConfig();

        this.enabled = true;

        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new FarmAssistCommand(this));
        registerListeners();

        if (FarmAssistConfig.getInstance().getCheckForUpdates()) {
           Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateCheckerTask(this));
        }

        Metrics metrics = new Metrics(this);

    }

    /**
     * Sends colored debug message.
     * @param msg Message to send
     */
    public static void debug(String msg) {
        if(FarmAssistConfig.getInstance().getDebug())
            Bukkit.getPluginManager().getPlugin("FarmAssistReboot").getLogger().warning("\u001B[33m"+"[DEBUG] "+msg+"\u001B[0m");
    }

    /**
     * Register Listeners
     */
    private void registerListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractionListener(this),this);
        pluginManager.registerEvents(new BlockBreakListener(this),this);
        getLogger().info("Registered listeners");
    }

    public void setGlobalEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("farmassist")) {
            List<String> commands = new ArrayList<>();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("farmassist.reload")) {
                    commands.add("reload");
                }
                if (player.hasPermission("farmassist.toggle.global")) {
                    commands.add("global");
                }
                if (player.hasPermission("farmassist.toggle")) {
                    commands.add("toggle");
                }
                if (player.hasPermission("farmassist.update")) {
                    commands.add("update");
                }
            }
            return commands;
        }
        return null;
    }

    public static FarmAssistReboot getInstance(){
        return instance;
    }

    public boolean isGlobalEnabled() {
        return enabled;
    }

}
