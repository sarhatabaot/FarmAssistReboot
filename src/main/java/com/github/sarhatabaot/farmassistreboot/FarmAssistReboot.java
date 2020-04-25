package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.command.CommandManager;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandGlobal;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandReload;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandToggle;
import com.github.sarhatabaot.farmassistreboot.command.commands.CommandUpdate;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.config.CropsUtil;
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

    // Commands
    private CommandManager commandManager;

    // State
    private boolean enabled;

    // Update Info
    private boolean needsUpdate;
    private String newVersion;

    @Override
    public void onLoad() {
        logger.info("FarmAssistReboot Loaded");
    }

    @Override
    public void onDisable() {
        logger.info("FarmAssistReboot Disabled!");
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        new FarmAssistConfig();
        new CropsUtil();

        this.enabled = true;

        registerCommands();
        registerListeners();

        if (FarmAssistConfig.getInstance().getCheckForUpdates()) {
           Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateCheckerTask(this));
        }

        Metrics metrics = new Metrics(this);

        logger.info("FarmAssistReboot Enabled!");
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

    /**
     * Registers commands
     */
    private void registerCommands() {
        commandManager = new CommandManager();
        commandManager.register(CommandManager.class,commandManager);
        commandManager.register(CommandGlobal.class,new CommandGlobal(this));
        commandManager.register(CommandReload.class,new CommandReload(this));
        commandManager.register(CommandToggle.class,new CommandToggle(this));
        commandManager.register(CommandUpdate.class,new CommandUpdate(this));
        getLogger().info("Registered commands");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("FarmAssist") && args.length > 0) {
            //Spoof args array to account for the initial sub-command specification
            String[] spoofedArgs = new String[args.length - 1];
            System.arraycopy(args, 1, spoofedArgs, 0, args.length - 1);
            commandManager.callCommand(args[0], sender, spoofedArgs);
            return true;
        }
        return false;
    }

    public void setGlobalEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public String getNewVersion() {
        return newVersion;
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

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
