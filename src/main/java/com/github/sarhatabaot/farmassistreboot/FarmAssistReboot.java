package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.BukkitCommandManager;
import com.github.sarhatabaot.farmassistreboot.command.FarmAssistCommand;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sarhatabaot
 */
@Getter @Setter
public class FarmAssistReboot extends JavaPlugin {
    private List<UUID> disabledPlayerList = new ArrayList<>();
    private FarmAssistConfig assistConfig;

    // State
    private boolean globalEnabled;

    private boolean needsUpdate;
    private String newVersion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.assistConfig = new FarmAssistConfig(this);
        this.globalEnabled = true;

        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new FarmAssistCommand(this));
        registerListeners();

        if (FarmAssistConfig.CHECK_FOR_UPDATES) {
           Bukkit.getScheduler().runTaskAsynchronously(this, new SimpleUpdateCheckerTask(this));
        }

        Metrics metrics = new Metrics(this,3885);

    }

    /**
     * Sends colored debug message.
     * @param msg Message to send
     */
    public static void debug(String msg) {
        if(FarmAssistConfig.DEBUG)
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
}
