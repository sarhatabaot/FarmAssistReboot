package com.github.sarhatabaot.farmassist;

import com.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassist.tasks.ReplantTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                FarmAssist.debug("COCOA_BEANS:"+player.getInventory().contains(Material.COCOA_BEANS));
                return player.getInventory().contains(Material.COCOA_BEANS);
            case POTATOES:
                FarmAssist.debug("POTATO:"+player.getInventory().contains(Material.POTATO));
                return player.getInventory().contains(Material.POTATO);
            case CARROTS:
                FarmAssist.debug("CARROT:"+player.getInventory().contains(Material.CARROT));
                return player.getInventory().contains(Material.CARROT);
            case WHEAT:
                FarmAssist.debug("WHEAT_SEEDS:"+player.getInventory().contains(Material.WHEAT_SEEDS));
                return player.getInventory().contains(Material.WHEAT_SEEDS);
            case BEETROOTS:
                FarmAssist.debug("BEETROOT_SEEDS:"+player.getInventory().contains(Material.BEETROOT_SEEDS));
                return player.getInventory().contains(Material.BEETROOT_SEEDS);
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                FarmAssist.debug("PUMPKIN_SEEDS:"+player.getInventory().contains(Material.PUMPKIN_SEEDS));
                return player.getInventory().contains(Material.PUMPKIN_SEEDS);
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                FarmAssist.debug("MELON_SEEDS:"+player.getInventory().contains(Material.MELON_SEEDS));
                return player.getInventory().contains(Material.MELON_SEEDS);
            case SWEET_BERRY_BUSH:
                FarmAssist.debug("SWEET_BERRIES:"+player.getInventory().contains(Material.SWEET_BERRIES));
                return player.getInventory().contains(Material.SWEET_BERRIES);
            default:
                FarmAssist.debug(material.name()+":"+player.getInventory().contains(material));
                return player.getInventory().contains(material);
        }
    }

    public static boolean isWorldEnabled(World world) {
        String globalWorld = "Config.Enabled per World:"+config.getWorldEnabled();
        String localWorld = "Is "+"\u001b[36m"+world.getName()+"\u001b[0m enabled: "+config.getWorlds().contains(world);
        FarmAssist.debug(globalWorld+localWorld);
        return !config.getWorldEnabled() || config.getWorlds().contains(world);
    }


    public static Material getMaterialFromCrops(Material material) {
        switch (material) {
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                return Material.MELON;
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                return Material.PUMPKIN;
            case SWEET_BERRY_BUSH:
                return Material.SWEET_BERRIES;
            default:
                return material;
        }
    }

    /**
     * Gets the plantable version of a material
     *
     * @param material
     * @return - The material that the player can plant
     */
    public static Material getCrop(Material material) {
        switch (material) {
            case COCOA:
                return Material.COCOA_BEANS;
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                return Material.PUMPKIN_SEEDS;
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                return Material.MELON_SEEDS;
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case SWEET_BERRIES:
                return Material.SWEET_BERRY_BUSH;
            default:
                return material;
        }
    }

    /**
     * @param player
     * @param block    Block broken
     * @param material Material to remove from inventory
     */
    public static void replant(Player player, Block block, Material material) {
        int spot = player.getInventory().first(getCrop(material));
        ItemStack next;
        if (spot >= 0) {
            next = player.getInventory().getItem(spot);
            if (next.getAmount() > 1) {
                next.setAmount(next.getAmount() - 1);
                player.getInventory().setItem(spot, next);
            } else {
                player.getInventory().setItem(spot, new ItemStack(Material.AIR));
            }
            ReplantTask b = new ReplantTask(block);
            FarmAssist.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(FarmAssist.getInstance(), b, 20L);
        }
    }

}
