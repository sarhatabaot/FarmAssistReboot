package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author sarhatabaot
 */
public class Util {
    public static boolean inventoryContains(Player player, Material material){
        switch (material){
            case COCOA:
                FarmAssistReboot.debug("COCOA_BEANS:"+player.getInventory().contains(Material.COCOA_BEANS));
                return player.getInventory().contains(Material.COCOA_BEANS);
            case POTATOES:
                FarmAssistReboot.debug("POTATO:"+player.getInventory().contains(Material.POTATO));
                return player.getInventory().contains(Material.POTATO);
            case CARROTS:
                FarmAssistReboot.debug("CARROT"+player.getInventory().contains(Material.CARROT));
                return player.getInventory().contains(Material.CARROT);
            case WHEAT:
                FarmAssistReboot.debug("WHEAT_SEEDS:"+player.getInventory().contains(Material.WHEAT_SEEDS));
                return player.getInventory().contains(Material.WHEAT_SEEDS);
            case BEETROOTS:
                FarmAssistReboot.debug("BEETROOT_SEEDS:"+player.getInventory().contains(Material.BEETROOT_SEEDS));
                return player.getInventory().contains(Material.BEETROOT_SEEDS);
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                FarmAssistReboot.debug("PUMPKIN_SEEDS:"+player.getInventory().contains(Material.PUMPKIN_SEEDS));
                return player.getInventory().contains(Material.PUMPKIN_SEEDS);
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                FarmAssistReboot.debug("MELON_SEEDS:"+player.getInventory().contains(Material.MELON_SEEDS));
                return player.getInventory().contains(Material.MELON_SEEDS);
            default: {
                FarmAssistReboot.debug(material.name()+":"+player.getInventory().contains(material));
                return player.getInventory().contains(material);
            }
        }
    }

    public static boolean isWorldEnabled(World world) {
        String globalWorld = "Config.Enabled per World:"+ FarmAssistConfig.getWorldEnabled();
        String localWorld = "Is "+"\u001b[36m"+world.getName()+"\u001b[0m enabled: "+FarmAssistConfig.ENABLED_WORLDS.contains(world);
        FarmAssistReboot.debug(globalWorld);
        FarmAssistReboot.debug(localWorld);
        return !FarmAssistConfig.getWorldEnabled() || FarmAssistConfig.ENABLED_WORLDS.contains(world);
    }


}
