package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.BukkitCommandManager;
import com.github.sarhatabaot.farmassistreboot.command.FarmAssistCommand;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */

public class FarmAssistReboot extends JavaPlugin {

    public List<String> disabledPlayerList = new ArrayList<>();

    // State
    private boolean enabled;

    private boolean needsUpdate;
    private String newVersion;

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(final boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(final String newVersion) {
        this.newVersion = newVersion;
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();
        new FarmAssistConfig(this);

        this.enabled = true;

        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new FarmAssistCommand(this));
        registerListeners();

        if (FarmAssistConfig.getInstance().getCheckForUpdates()) {
           Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateCheckerTask(this));
        }

        Metrics metrics = new Metrics(this,3885);

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


    public static FarmAssistReboot getInstance(){
        return instance;
    }

    public boolean isGlobalEnabled() {
        return enabled;
    }

}
