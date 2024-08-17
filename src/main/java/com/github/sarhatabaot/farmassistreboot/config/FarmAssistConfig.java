package com.github.sarhatabaot.farmassistreboot.config;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FarmAssistConfig {
    private final FarmAssistReboot plugin;
    private static FileConfiguration config;

    public static boolean USE_PERMISSIONS;
    public static boolean DEBUG;
    public static boolean CHECK_FOR_UPDATES;
    public static boolean PLANT_WHEAT_ON_TILL;
    public static boolean ENABLED_PER_WORLD;
    public static List<World> ENABLED_WORLDS;

    public static boolean IGNORE_RENAMED;

    public static boolean IGNORE_NBT;
    public static String ACTIVE_LANGUAGE;

    public static boolean NO_SEEDS;

    public static boolean DISABLE_LATEST_VERSION;


    public FarmAssistConfig(final @NotNull FarmAssistReboot plugin) {
        this.plugin = plugin;
        FarmAssistConfig.config = plugin.getConfig();

        USE_PERMISSIONS = config.getBoolean("use-permissions", true);
        DEBUG = config.getBoolean("debug",false);
        CHECK_FOR_UPDATES = config.getBoolean("check-for-updates",true);
        PLANT_WHEAT_ON_TILL = config.getBoolean("wheat.plant-on-till",true);
        ENABLED_PER_WORLD = config.getBoolean("worlds.enable-per-world",false);
        ENABLED_WORLDS = getWorlds();
        ACTIVE_LANGUAGE = config.getString("language", "en");
        IGNORE_RENAMED = config.getBoolean("ignore-seeds.renamed", true);
        IGNORE_NBT = config.getBoolean("ignore-seeds.nbt", true);
        NO_SEEDS = config.getBoolean("no-seeds", false);
        DISABLE_LATEST_VERSION = config.getBoolean("disable-latest-version", false);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static boolean getEnabled(@NotNull Material material) {
        return config.getBoolean(material.name().toLowerCase() + ".enabled", true);
    }


    private @NotNull List<World> getWorlds() {
        ArrayList<World> worldsList = new ArrayList<>();
        for(String world: config.getStringList("worlds.enabled-worlds")){
            if(Bukkit.getWorld(world) != null)
                worldsList.add(Bukkit.getWorld(world));
        }
        return worldsList;
    }

    public static boolean getRipe(@NotNull Material material) {
        return config.getBoolean(material.name().toLowerCase() + ".replant-when-ripe", false);
    }

    public void setActiveLanguage(final String locale) {
        config.set("language",locale);
        reloadConfig();
        ACTIVE_LANGUAGE = config.getString("language", "en");
    }
}
