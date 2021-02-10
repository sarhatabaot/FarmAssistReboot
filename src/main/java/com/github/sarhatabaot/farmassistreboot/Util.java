package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public class Util {
    private static FarmAssistReboot plugin;
    public static void init(final FarmAssistReboot plugin){
        Util.plugin= plugin;
    }
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
            default:
                FarmAssistReboot.debug(material.name()+":"+playerInventory.contains(material));
                return playerInventory.contains(material);

        }
    }

    public static boolean isWorldEnabled(World world) {
        String globalWorld = "Config.Enabled per World:"+ FarmAssistConfig.ENABLED_PER_WORLD;
        String localWorld = "Is "+"\u001b[36m"+world.getName()+"\u001b[0m enabled: "+FarmAssistConfig.ENABLED_WORLDS.contains(world);
        FarmAssistReboot.debug(globalWorld);
        FarmAssistReboot.debug(localWorld);
        return !FarmAssistConfig.ENABLED_PER_WORLD || FarmAssistConfig.ENABLED_WORLDS.contains(world);
    }

    public static void replant(@NotNull Player player, Block block, Material material) {
        Crop crop = Crop.valueOf(material.name());
        int spot = player.getInventory().first(crop.getSeed());
        if (spot >= 0) {
            removeOrSubtractItem(player, spot);
            new ReplantTask(block).runTaskLater(plugin, 5L);
        }
    }

    public static void removeOrSubtractItem(Player player, int spot) {
        ItemStack next = player.getInventory().getItem(spot);
        if (next.getAmount() > 1) {
            next.setAmount(next.getAmount() - 1);
            player.getInventory().setItem(spot, next);
        } else {
            player.getInventory().setItem(spot, new ItemStack(Material.AIR));
        }
    }


}
