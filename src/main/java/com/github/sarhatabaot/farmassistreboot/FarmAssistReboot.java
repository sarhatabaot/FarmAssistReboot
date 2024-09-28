package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.commands.FarmAssistRebootCommand;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.listeners.BlockBreakListener;
import com.github.sarhatabaot.farmassistreboot.listeners.TillListener;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarmAssistReboot extends JavaPlugin {
    private MainConfig mainConfig;
    private LanguageManager languageManager;

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

        this.cropManager = new CropManager(mainConfig);
        this.languageManager = new LanguageManager(this, mainConfig);
        // Command Logic

        ReplantUtil.init(this,cropManager);

        registerListeners();

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new FarmAssistRebootCommand(this, languageManager));

        // Plugin startup logic

        logBetaVersion();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(this,cropManager), this);
        pluginManager.registerEvents(new TillListener(cropManager), this);
    }

    public void reload() {
        this.mainConfig.reload();
        this.languageManager.reload();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void logBetaVersion() {
        this.getLogger().info("====================");
        this.getLogger().info("=   BETA VERSION   =");
        this.getLogger().info("====================");
    }

}
