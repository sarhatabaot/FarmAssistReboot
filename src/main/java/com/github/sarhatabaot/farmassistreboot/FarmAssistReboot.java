package com.github.sarhatabaot.farmassistreboot;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.farmassistreboot.commands.FarmAssistRebootCommand;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarmAssistReboot extends JavaPlugin {
    private MainConfig mainConfig;
    private CropManager cropManager;

    @Override
    public void onEnable() {
        this.mainConfig = new MainConfig(this);
        this.mainConfig.createAndLoad();

        this.cropManager = new CropManager(mainConfig);
        this.cropManager.load();
        // Command Logic

        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new FarmAssistRebootCommand());

        // Plugin startup logic

        logBetaVersion();
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
