package com.github.sarhatabaot.farmassistreboot.config;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class FarmAssistConfig {
    private static FarmAssistConfig instance;
    private FarmAssistReboot plugin;
    private FileConfiguration config;

    public FarmAssistConfig(final FarmAssistReboot plugin) {
        instance = this;
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        FarmAssistReboot.debug("Config reloaded ");
    }


    public boolean isPermissionEnabled() {
        return config.getBoolean("Use Permissions");
    }

    public boolean getDebug() {
        return config.getBoolean("debug");
    }

    public boolean getCheckForUpdates() {
        return config.getBoolean("Check for updates");
    }

    public static FarmAssistConfig getInstance() {
        return instance;
    }

    public boolean getEnabled(Material material) {
        return config.getBoolean(material.name().toLowerCase() + ".Enabled");
    }

    public boolean getWorldEnabled() {
        return config.getBoolean("Worlds.Enable per world");
    }

    //TODO:
    public List<World> getWorlds() {
        ArrayList<World> worldsList = new ArrayList<>();
        List<?> configList = config.getList("Worlds.Enabled Worlds");
        for (Object obj : configList) {
            World tmp = Bukkit.getWorld((String) obj);
            worldsList.add(tmp);
        }
        return worldsList;
    }

    public boolean getRipe(Material material) {
        return config.getBoolean(material.name().toLowerCase() + ".Replant when ripe");
    }

    public boolean getPlantOnTill() {
        return config.getBoolean("wheat.Plant on till");
    }
}
