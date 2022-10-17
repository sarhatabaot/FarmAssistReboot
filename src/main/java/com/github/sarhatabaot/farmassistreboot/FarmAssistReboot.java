package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.command.FarmAssistCommand;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.lang.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.JoinListener;
import com.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
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
    private LanguageManager languageManager;
    private List<UUID> disabledPlayerList = new ArrayList<>();
    private FarmAssistConfig assistConfig;

    private boolean globalEnabled;

    private boolean needsUpdate;
    private String newVersion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.assistConfig = new FarmAssistConfig(this);
        this.globalEnabled = true;

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new FarmAssistCommand(this));
        registerListeners();
        Util.init(this);
        if (FarmAssistConfig.CHECK_FOR_UPDATES) {
            new SimpleUpdateCheckerTask(this).runTaskAsynchronously(this);
        }

        new Metrics(this,3885);

        languageManager = new LanguageManager(this);
    }

    public void debug(final Class<?> clazz,final String message) {
        if(FarmAssistConfig.DEBUG) {
            getLogger().info(() -> "DEBUG " + clazz.getSimpleName() + " " + message);
        }
    }

    /**
     * Register Listeners
     */
    private void registerListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInteractionListener(this),this);
        pluginManager.registerEvents(new BlockBreakListener(this),this);
        pluginManager.registerEvents(new JoinListener(this),this);
    }
}
