package io.github.sarhatabaot.farmassistreboot;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sarhatabaot
 */
public class Util {
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
}
