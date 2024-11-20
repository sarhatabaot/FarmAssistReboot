package com.github.sarhatabaot.farmassistreboot.config;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class FarmAssistConfig {
    private final FarmAssistReboot plugin;
    private YamlDocument config;

    public void createAndLoad() {
        try {
            this.config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"),
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build());
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

    public FarmAssistConfig(final @NotNull FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.createAndLoad();
    }

    public boolean disableLatestVersion() {
        return config.getBoolean(Route.from("disable-latest-version"), false);
    }
    public boolean ignoreNbt() {
        return config.getBoolean(Route.from("ignore-seeds", "nbt"), false);
    }

    public boolean ignoreRenamed() {
        return config.getBoolean(Route.from("ignore-seeds", "renamed"), false);
    }

    public String activeLanguage() {
        return config.getString(Route.from("language"), "en");
    }

    public boolean isWorldEnabled(final String world) {
        return getWorlds().contains(world);
    }

    public boolean enabledPerWorld() {
        return config.getBoolean(Route.from("worlds", "enable-per-world"), false);
    }

    public boolean plantWheatOnTill() {
        return config.getBoolean(Route.from("wheat", "plant-on-till"), true);
    }
    public boolean checkForUpdates() {
        return config.getBoolean(Route.from("check-for-updates"), true);
    }

    public boolean debug() {
        return config.getBoolean(Route.from("debug"), false);
    }

    public boolean usePermissions() {
        return config.getBoolean(Route.from("use-permissions"), true);
    }

    public boolean getEnabled(@NotNull Material material) {
        return config.getBoolean(Route.from(material.name().toLowerCase(), "enabled"), true);
    }

    public boolean noSeeds() {
        return config.getBoolean(Route.from("no-seeds", false));
    }

    private @NotNull Set<String> getWorlds() {
        HashSet<String> enableWorlds = new HashSet<>();
        for (String world: config.getStringList(Route.from("worlds", "enabled-worlds"))) {
            if (Bukkit.getWorld(world) != null) {
                enableWorlds.add(world);
            }
        }
        return enableWorlds;
    }

    public boolean getRipe(@NotNull Material material) {
        return config.getBoolean(Route.from(material.name().toLowerCase(), "replant-when-ripe"), false);
    }

    //test, does this need a reload?
    public void setActiveLanguage(final String locale) {
        config.set(Route.from("language"), locale);
//        oldConfig.set("language",locale);
//        reloadConfig();
//        ACTIVE_LANGUAGE = oldConfig.getString("language", "en");
    }
}
