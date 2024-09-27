package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.commands.FarmAssistRebootCommand;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.language.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarmAssistReboot extends JavaPlugin {
    private MainConfig mainConfig;
    private LanguageManager languageManager;

    private CropManager cropManager;

    @Override
    public void onEnable() {
        this.mainConfig = new MainConfig(this);
        this.mainConfig.createAndLoad();

        this.cropManager = new CropManager(mainConfig);
        this.cropManager.load();

        this.languageManager = new LanguageManager(this, mainConfig);
        // Command Logic

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new FarmAssistRebootCommand(this, languageManager));

        // Plugin startup logic

        logBetaVersion();
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
