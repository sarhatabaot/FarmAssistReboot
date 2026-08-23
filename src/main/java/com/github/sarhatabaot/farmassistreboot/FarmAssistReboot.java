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

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sarhatabaot
 */
public class FarmAssistReboot extends JavaPlugin {
    private final MorePaperLib paperLib = new MorePaperLib(this);
    private final Set<UUID> disabledPlayerList = ConcurrentHashMap.newKeySet();
    private FarmAssistConfig assistConfig;
    private LanguageManager languageManager;

    private boolean globalEnabled;

    private boolean needsUpdate;
    private String newVersion;

    @Override
    public void onEnable() {
        this.needsUpdate = false;
        this.newVersion = this.getDescription().getVersion();

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

    /**
     * H11: returns an unmodifiable view of the disabled-player set. Callers
     * that need to mutate it should use {@link #disablePlayer(UUID)} or
     * {@link #enablePlayer(UUID)} instead.
     *
     * @return an unmodifiable view of the disabled-player set
     */
    public Set<UUID> getDisabledPlayerList() {
        return Collections.unmodifiableSet(disabledPlayerList);
    }

    /**
     * Add a player to the disabled set. Returns {@code true} if the player
     * was not already disabled.
     */
    public boolean disablePlayer(UUID playerId) {
        return disabledPlayerList.add(playerId);
    }

    /**
     * Remove a player from the disabled set. Returns {@code true} if the
     * player was previously disabled.
     */
    public boolean enablePlayer(UUID playerId) {
        return disabledPlayerList.remove(playerId);
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

    /**
     * @return {@code true} if the update checker has determined that a newer
     *         version is available.
     * @see #doesNotNeedUpdate()
     */
    public boolean needsUpdate() {
        return needsUpdate;
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
