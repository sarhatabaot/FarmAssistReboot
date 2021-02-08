package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author sarhatabaot
 */
public class Util {
    public static boolean inventoryContains(PlayerInventory playerInventory, Material material){
        switch (material){
            case COCOA:
                return playerInventory.contains(Material.COCOA_BEANS);
            case POTATOES:
                return playerInventory.contains(Material.POTATO);
            case CARROTS:
                return playerInventory.contains(Material.CARROT);
            case WHEAT:
                return playerInventory.contains(Material.WHEAT_SEEDS);
            case BEETROOTS:
                return playerInventory.contains(Material.BEETROOT_SEEDS);
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                return playerInventory.contains(Material.PUMPKIN_SEEDS);
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                return playerInventory.contains(Material.MELON_SEEDS);
            default: {
                FarmAssistReboot.debug(material.name()+":"+playerInventory.contains(material));
                return playerInventory.contains(material);
            }
        }
    }

    public static boolean isWorldEnabled(World world) {
        String globalWorld = "Config.Enabled per World:"+ FarmAssistConfig.ENABLED_PER_WORLD;
        String localWorld = "Is "+"\u001b[36m"+world.getName()+"\u001b[0m enabled: "+FarmAssistConfig.ENABLED_WORLDS.contains(world);
        FarmAssistReboot.debug(globalWorld);
        FarmAssistReboot.debug(localWorld);
        return !FarmAssistConfig.ENABLED_PER_WORLD || FarmAssistConfig.ENABLED_WORLDS.contains(world);
    }


}
