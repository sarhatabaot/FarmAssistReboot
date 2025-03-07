package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.command.FarmAssistCommand;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.lang.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.JoinListener;
import com.github.sarhatabaot.farmassistreboot.listeners.PlayerInteractionListener;
import com.github.sarhatabaot.farmassistreboot.tasks.SimpleUpdateCheckerTask;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sarhatabaot
 */
public class FarmAssistReboot extends JavaPlugin {
    private final MorePaperLib paperLib = new MorePaperLib(this);
    private final List<UUID> disabledPlayerList = new ArrayList<>();
    private FarmAssistConfig assistConfig;
    private LanguageManager languageManager;

    private boolean globalEnabled;

    private boolean needsUpdate;
    private String newVersion;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.assistConfig = new FarmAssistConfig(this);
        this.languageManager = new LanguageManager(this);

        this.globalEnabled = true;

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new FarmAssistCommand(this));
        commandManager.getCommandCompletions().registerCompletion("supported-lang", c -> languageManager.getSupportedLanguages());

        registerListeners();
        Util.init(this);
        if (assistConfig.checkForUpdates()) {
            this.paperLib.scheduling().asyncScheduler().run(new SimpleUpdateCheckerTask(this));
        }

        new Metrics(this,3885);
        registerPapi();
    }

    public void debug(final Class<?> clazz,final String message) {
        if(assistConfig.debug()) {
            getLogger().info(() -> "DEBUG " + clazz.getSimpleName() + " " + message);
        }
    }

    private void registerPapi() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new FarmAssistPlaceholderExpansion(this).register();
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

    public MorePaperLib getPaperLib() {
        return paperLib;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public List<UUID> getDisabledPlayerList() {
        return disabledPlayerList;
    }

    public FarmAssistConfig getAssistConfig() {
        return assistConfig;
    }

    public boolean isGlobalEnabled() {
        return globalEnabled;
    }

    public boolean isGlobalDisabled() {
        return !globalEnabled;
    }

    public boolean doesNotNeedUpdate() {
        return !needsUpdate;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setGlobalEnabled(boolean globalEnabled) {
        this.globalEnabled = globalEnabled;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
