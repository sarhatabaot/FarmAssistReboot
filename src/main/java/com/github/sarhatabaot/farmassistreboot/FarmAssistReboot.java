package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.commands.AdminCommands;
import com.github.sarhatabaot.farmassistreboot.commands.PlayerCommands;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.TillListener;
import com.github.sarhatabaot.farmassistreboot.placeholders.Placeholder;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarmAssistReboot extends JavaPlugin {
    private MainConfig mainConfig;
    private LanguageManager languageManager;
    private ToggleManager toggleManager;

    private CropManager cropManager;

    @Override
    public void onEnable() {
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.mainConfig = new MainConfig(this);
        this.mainConfig.createAndLoad();

        this.cropManager = new CropManager(this, mainConfig);
        this.languageManager = new LanguageManager(this, mainConfig);
        this.toggleManager = new ToggleManager();

        ReplantUtil.init(this, cropManager);

        registerListeners();

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.getCommandReplacements().addReplacement("far_main_command","far|farmassistreboot");
        commandManager.registerCommand(new PlayerCommands(this, languageManager));
        commandManager.registerCommand(new AdminCommands(this, languageManager));

        registerPlaceholders();
        logBetaVersion();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(this, cropManager, mainConfig), this);
        pluginManager.registerEvents(new TillListener(this, cropManager, mainConfig), this);
    }

    public void reload() {
        this.mainConfig.reload();
        this.languageManager.reload();
    }

    @Override
    public void onDisable() {
        this.languageManager.save();
        this.toggleManager.save();
        // Plugin shutdown logic
    }

    private void logBetaVersion() {
        this.getLogger().info(() -> "====================");
        this.getLogger().info(() -> "=   BETA VERSION   =");
        this.getLogger().info(() -> "====================");
    }

    public void debug(String message, Object... args) {
        if (mainConfig.isDebug()) {
            this.getLogger().info(() -> "DEBUG " + String.format(message, args));
        }
    }

    public void trace(String message, Object... args) {
        if (mainConfig.isDebug() && mainConfig.getDebugLevel() == Util.DebugLevel.TRACE) {
            this.getLogger().info(() -> "TRACE " + String.format(message, args));
        }
    }

    public ToggleManager getToggleManager() {
        return toggleManager;
    }

    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new Placeholder(this).register(); //
        }
    }
}
