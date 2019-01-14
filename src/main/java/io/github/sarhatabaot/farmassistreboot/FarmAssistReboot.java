package io.github.sarhatabaot.farmassistreboot;

import io.github.sarhatabaot.farmassistreboot.command.CommandManager;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandGlobal;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandReload;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandToggle;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandUpdate;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistCrops;
import io.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import io.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author sarhatabaot
 */

public class FarmAssistReboot extends JavaPlugin {
    private static FarmAssistReboot instance;
    public List<String> disabledPlayerList = new ArrayList<>();
    public Logger logger = getLogger();

    // Config
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

        //Config
        saveDefaultConfig();
        new FarmAssistConfig();
        new FarmAssistCrops();

        this.enabled = true;

        registerCommands();
        registerListeners();

        if (FarmAssistConfig.getInstance().getCheckForUpdates()) {
           // Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateChecker(this));
        }


        logger.info("FarmAssistReboot Enabled!");
    }
    public static void debug(String msg) {
        if(FarmAssistConfig.getInstance().getDebug())
            Bukkit.getPluginManager().getPlugin("FarmAssistReboot").getLogger().warning("\u001B[33m"+"[DEBUG] "+msg+"\u001B[0m");
    }

    private void registerListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractionListener(this),this);
        pluginManager.registerEvents(new BlockBreakListener(this),this);
        logger.fine("Registered Listeners");
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
        logger.fine("Commands registered.");
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

    private void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    private void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    private static class SimpleUpdateChecker implements Runnable {

        private FarmAssistReboot plugin;
        private String versionNumber;

        private String latest = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";

        public SimpleUpdateChecker(FarmAssistReboot plugin) {
            this.plugin = plugin;
            this.versionNumber = plugin.getDescription().getVersion();
        }


        @Override
        public void run() {
            try {
                URL updateTag = new URL(latest);
                Scanner scanner = new Scanner(updateTag.openStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    stringBuilder.append(scanner.nextLine());
                }
                scanner.close();
                String str = stringBuilder.toString();

                JSONObject obj = new JSONObject(str);
                String remoteVer = obj.get("tag_name").toString();
                int remoteVal = Integer.valueOf(remoteVer.replace(".", ""));
                int localVer = Integer.valueOf(versionNumber.replace(".", ""));
                if (remoteVal > localVer) {
                    plugin.setNeedsUpdate(true);
                    plugin.setNewVersion(remoteVer);
                    plugin.getLogger().info("New update: " + remoteVer + " Current version: " + versionNumber);
                } else {
                    plugin.setNeedsUpdate(false);
                    plugin.getLogger().info("You are running the latest version: " + versionNumber);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


}
