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


    public FarmAssistConfig(final @NotNull FarmAssistReboot plugin) {
        this.plugin = plugin;
        FarmAssistConfig.config = plugin.getConfig();

        USE_PERMISSIONS = config.getBoolean("use-permissions", true);
        DEBUG = config.getBoolean("debug",false);
        CHECK_FOR_UPDATES = config.getBoolean("check-for-updates",true);
        PLANT_WHEAT_ON_TILL = config.getBoolean("wheat.plant-on-till",true);
        ENABLED_PER_WORLD = config.getBoolean("worlds.enable-per-world",true);
        ENABLED_WORLDS = getWorlds();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static boolean getEnabled(@NotNull Material material) {
        return config.getBoolean(material.name().toLowerCase() + ".enabled");
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
        return config.getBoolean(material.name().toLowerCase() + ".replant-when-ripe");
    }
}
