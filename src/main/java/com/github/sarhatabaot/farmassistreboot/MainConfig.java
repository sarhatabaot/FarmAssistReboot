package com.github.sarhatabaot.farmassistreboot;


import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainConfig {
    private YamlDocument config;
    private final JavaPlugin plugin;

    public MainConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createAndLoad() {
        try {
            this.config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to load config.yml");
        }
    }

    public void reload() {
        try {
            this.config.reload();
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to reload config.yml");
        }
    }

    public List<String> getIgnoredNbt() {
        return this.config.getStringList(Route.from("ignore-nbt"));
    }

    public List<String> getDisabledCrops() {
        return this.config.getStringList(Route.from("disabled-crops"));
    }

    public boolean isCheckForUpdates() {
        return this.config.getBoolean(Route.from("check-for-updates"));
    }

    public int getMaxCacheSize() {
        return this.config.getInt(Route.from("advanced-settings", "max-cache-size"));
    }

    public boolean isDebug() {
        return this.config.getBoolean(Route.from("advanced-settings", "debug"));
    }

    public String getVersion() {
        return this.config.getString(Route.from("config-version"));
    }
}
