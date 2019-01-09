package io.github.sarhatabaot.farminassistreboot;

import io.github.sarhatabaot.farminassistreboot.commands.CommandReload;
import io.github.sarhatabaot.farminassistreboot.commands.CommandToggle;
import io.github.sarhatabaot.farminassistreboot.commands.CommandUpdate;
import io.github.sarhatabaot.farminassistreboot.listeners.PlayerInteractionListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
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

public class FarmAssist extends JavaPlugin {
    public List<String> disabledPlayerList = new ArrayList<>();
    public Logger logger = getLogger();

    // Config
    private File configFile;
    private FileConfiguration config;

    // State
    private boolean enabled;

    // Update Info
    private boolean needsUpdate;
    private String newVersion;


    @Override
    public void onLoad() {
        logger.info("FarmAssist Loaded");
    }

    @Override
    public void onDisable() {
        logger.info("FarmAssist Disabled!");
    }

    @Override
    public void onEnable() {
        if(!setupConfig()){
            logger.severe("Couldn't setup config.yml, plugin loading aborted.");
            return;
        }

        if (config.getBoolean("debug")) {
            logger.setLevel(Level.ALL);
        }

        new PlayerInteractionListener(this);

        if (config.getBoolean("Check for updates")) {
            Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateChecker());
        }


        registerCommands();

        logger.info("FarmAssist Enabled!");
    }

    /**
     *
     * @return
     */
    private boolean setupConfig(){
        try {
            Config.initConfig(getDataFolder());
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
        this.getCommand("FarmAssist reload").setExecutor(new CommandReload(this));
        this.getCommand("FarmAssist toggle").setExecutor(new CommandToggle(this));
        this.getCommand("FarmAssist global").setExecutor(new CommandToggle(this));
        this.getCommand("FarmAssist update").setExecutor(new CommandUpdate(this));
        logger.fine("Commands registered.");
    }

    /**
     *
     */
    public void loadYamls() {
        try {
            this.config.load(this.configFile);
        } catch (Exception exception) {
            logger.severe(exception.getMessage());
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("FarmAssist") && args.length > 0) {

            return true;
        } else {
            showHelpByPermissions(sender);
        }
        return false;
    }

    public void setGlobalEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //TODO:
    private void showHelpByPermissions(CommandSender sender){
        List<Permission> permissionsList = this.getDescription().getPermissions();
        StringBuilder sb = new StringBuilder();
        for(Permission permission : permissionsList){
            if(sender.hasPermission(permission)){
                this.getDescription().getCommands();
            }
        }

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
                } else if (player.hasPermission("farmassist.toggle.global")) {
                    commands.add("global");
                } else if (player.hasPermission("farmassist.toggle")) {
                    commands.add("toggle");
                } else if (player.hasPermission("farmassist.update")) {
                    commands.add("update");
                }
            }
            return commands;
        }
        return null;
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
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

        private FarmAssist plugin;
        private String versionNumber;

        private String latest = "https://api.github.com/repos/sarhatabaot/FarmAssist/releases/latest";

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
