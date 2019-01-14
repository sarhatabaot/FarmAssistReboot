package io.github.sarhatabaot.farmassistreboot;

import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sarhatabaot
 */
public class Util {
    private static FarmAssistConfig config = FarmAssistConfig.getInstance();

    public static boolean inventoryContains(Player player, Material material){
        switch (material){
            case WHEAT:
                FarmAssistReboot.debug("WHEAT_SEEDS:"+player.getInventory().contains(Material.WHEAT_SEEDS));
                return player.getInventory().contains(Material.WHEAT_SEEDS);
            case BEETROOT:
                FarmAssistReboot.debug("BEETROOT_SEEDS:"+player.getInventory().contains(Material.BEETROOT_SEEDS));
                return player.getInventory().contains(Material.BEETROOT_SEEDS);
            case PUMPKIN:
                FarmAssistReboot.debug("PUMPKIN_SEEDS:"+player.getInventory().contains(Material.PUMPKIN_SEEDS));
                return player.getInventory().contains(Material.PUMPKIN_SEEDS);
            case MELON:
                FarmAssistReboot.debug("MELON_SEEDS:"+player.getInventory().contains(Material.MELON_SEEDS));
                return player.getInventory().contains(Material.MELON_SEEDS);
            default: {
                FarmAssistReboot.debug(material.name()+":"+player.getInventory().contains(material));
                return player.getInventory().contains(material);
            }
        }
    }
    public static boolean isWorldEnabled(World world) {
        FarmAssistReboot.debug("test");
        FarmAssistReboot.debug(config.getWorldEnabled()+String.valueOf(config.getWorlds().contains(world))+world.getName());
        return !config.getWorldEnabled() || config.getWorlds().contains(world);
    }

    public static boolean checkPermission(Player player, String permission) {
        return !config.getPermission() || player.hasPermission("farmassist."+permission);
    }

}
