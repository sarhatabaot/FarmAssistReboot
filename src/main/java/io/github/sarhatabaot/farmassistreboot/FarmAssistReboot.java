package io.github.sarhatabaot.farmassistreboot;

import io.github.sarhatabaot.farmassistreboot.command.CommandManager;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandGlobal;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandReload;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandToggle;
import io.github.sarhatabaot.farmassistreboot.command.commands.CommandUpdate;
import io.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import io.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sarhatabaot
 */

public class FarmAssistReboot extends JavaPlugin {
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
        if(!setupConfig()){
            logger.severe("Couldn't setup config.yml, plugin loading aborted.");
            return;
        }

        if (Config.isDebug()) {
            logger.setLevel(Level.ALL);
        }


        registerCommands();
        registerListeners();

        if (Config.isCheckForUpdates()) {
           // Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateChecker());
        }


        logger.info("FarmAssistReboot Enabled!");
    }

    private void registerListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractionListener(this),this);
        pluginManager.registerEvents(new BlockBreakListener(this),this);
    }

    /**
     *
     * @return
     */
    private boolean setupConfig(){
        try {
            Config.initConfig(getDataFolder());
            logger.fine("config initialized");
        } catch (IOException e){
            logger.severe("Could not make config file!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
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

    /*
     *
     *
    public void loadYamls() {
        try {
            this.config.load(this.configFile);
        } catch (Exception exception) {
            logger.severe(exception.getMessage());
        }
    }*/


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

    public boolean isGlobalEnabled() {
        return enabled;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    private static class SimpleUpdateChecker implements Runnable {

        private FarmAssistReboot plugin;
        private String versionNumber;

        private String latest = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";

        public SimpleUpdateChecker() {
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
                    plugin.getLogger().info("You are running the latest version: " + versionNumber);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


}