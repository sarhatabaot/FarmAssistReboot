package com.github.sarhatabaot.farmassistreboot.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
/**
 * @author sarhatabaot
 * Extracted from KrakenCore, since it only supports JDK 16+
 */
public class ConfigFile<T extends JavaPlugin> {
    private final String resourcePath;

    protected final T plugin;
    protected final String fileName;
    protected final File folder;

    protected File file;
    protected FileConfiguration config;

    protected ConfigFile(final @NotNull T plugin, final String resourcePath, final String fileName, final String folder) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.resourcePath = resourcePath;
        this.folder = new File(plugin.getDataFolder().getPath() + File.separator + folder);
    }

    public void saveDefaultConfig() {
        if (this.file == null) {
            this.file = new File(folder, fileName);
        }

        if (!this.file.exists()) {
            plugin.saveResource(resourcePath + fileName, false);
        }

        reloadConfig();
    }


    public void reloadConfig() {
        if (file == null) {
            file = new File(folder, fileName);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @NotNull
    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return this.config;
    }
}