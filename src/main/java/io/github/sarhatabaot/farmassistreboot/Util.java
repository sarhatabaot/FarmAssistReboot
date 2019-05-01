package io.github.sarhatabaot.farmassistreboot;

import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static io.github.sarhatabaot.farmassistreboot.FarmAssistReboot.debug;

/**
 * @author sarhatabaot
 */
public class Util {
    private Util() {
        throw new IllegalStateException("Utility class");
    }

    private static FarmAssistConfig config = FarmAssistConfig.getInstance();

    public static boolean inventoryContains(Player player, Material material){
        switch (material){
            case COCOA:
                debug("COCOA_BEANS:"+player.getInventory().contains(Material.COCOA_BEANS));
                return player.getInventory().contains(Material.COCOA_BEANS);
            case POTATOES:
                debug("POTATO:"+player.getInventory().contains(Material.POTATO));
                return player.getInventory().contains(Material.POTATO);
            case CARROTS:
                debug("CARROT"+player.getInventory().contains(Material.CARROT));
                return player.getInventory().contains(Material.CARROT);
            case WHEAT:
                debug("WHEAT_SEEDS:"+player.getInventory().contains(Material.WHEAT_SEEDS));
                return player.getInventory().contains(Material.WHEAT_SEEDS);
            case BEETROOTS:
                debug("BEETROOT_SEEDS:"+player.getInventory().contains(Material.BEETROOT_SEEDS));
                return player.getInventory().contains(Material.BEETROOT_SEEDS);
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                debug("PUMPKIN_SEEDS:"+player.getInventory().contains(Material.PUMPKIN_SEEDS));
                return player.getInventory().contains(Material.PUMPKIN_SEEDS);
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                debug("MELON_SEEDS:"+player.getInventory().contains(Material.MELON_SEEDS));
                return player.getInventory().contains(Material.MELON_SEEDS);
            case SWEET_BERRY_BUSH:
                debug("SWEET_BERRIES"+player.getInventory().contains(Material.SWEET_BERRIES));
                return player.getInventory().contains(Material.SWEET_BERRIES);
            default:
                debug(material.name()+":"+player.getInventory().contains(material));
                return player.getInventory().contains(material);
        }
    }

    public static boolean isWorldEnabled(World world) {
        String globalWorld = "Config.Enabled per World:"+config.getWorldEnabled();
        String localWorld = "Is "+"\u001b[36m"+world.getName()+"\u001b[0m enabled: "+config.getWorlds().contains(world);
        debug(globalWorld);
        debug(localWorld);
        return !config.getWorldEnabled() || config.getWorlds().contains(world);
    }

}
