package io.github.sarhatabaot.farmassistreboot;

import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author sarhatabaot
 */
public class Util {
    private static FarmAssistConfig config = FarmAssistConfig.getInstance();

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
        FarmAssistReboot.debug(config.getWorldEnabled()+","+config.getWorlds().contains(world)+","+world.getName());
        return !config.getWorldEnabled() || config.getWorlds().contains(world);
    }

    public static boolean checkPermission(Player player, String permission) {
        FarmAssistReboot.debug("has permission:"+player.hasPermission(permission)+"enabled:"+config.getPermission());
        return !config.getPermission() || player.hasPermission("farmassist."+permission);
    }

}
