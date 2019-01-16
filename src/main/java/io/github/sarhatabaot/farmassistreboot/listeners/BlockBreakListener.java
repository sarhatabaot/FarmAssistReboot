package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import io.github.sarhatabaot.farmassistreboot.Util;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistCrops;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {
    private FarmAssistReboot plugin;
    private FarmAssistConfig config;

    public BlockBreakListener(FarmAssistReboot plugin) {
        config = FarmAssistConfig.getInstance();
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGlobalEnabled())
            return;
        if (this.plugin.disabledPlayerList.contains(event.getPlayer().getName()))
            return;
        if (Util.isWorldEnabled(event.getPlayer().getWorld())) {
            FarmAssistReboot.debug("isWorldEnabled:" + Util.isWorldEnabled(event.getPlayer().getWorld()));
            applyReplant(event);
        }
    }

    /**
     * @param player
     * @param block    Block broken
     * @param material Material to remove from inventory
     */
    private void replant(@NotNull Player player, Block block, Material material) {
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
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, b, 20L);
        }
    }

    private boolean applyReplant(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        FarmAssistReboot.debug(material.name());
        if (!FarmAssistCrops.getCropList().contains(material)) {
            FarmAssistReboot.debug("CropList doesn't contain: " + material.name());
            return false;
        }
        FarmAssistReboot.debug("CropList contains: " + material.name());

        if (config.getEnabled(getMaterialFromCrops(material)) && checkPermission(event.getPlayer(), material.name().toLowerCase())) {

            if (!Util.inventoryContains(event.getPlayer(), material)) {
                FarmAssistReboot.debug("Player doesn't have the correct seeds/material to replant");
                return false;
            }
            if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
                replant(event.getPlayer(), event.getBlock(), material);
                return true;
            }
            if (!config.getRipe(material) || isRipe(event.getBlock())) {
                replant(event.getPlayer(), event.getBlock(), material);
                return true;
            }
        }
        FarmAssistReboot.debug("Fallthrough");
        return false;
    }


    private Material getMaterialFromCrops(Material material) {
        switch (material) {
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
                return Material.MELON;
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
                return Material.PUMPKIN;
            case BEETROOTS:
                return Material.BEETROOT;
            default:
                return material;
        }
    }

    /**
     * Gets the plantable version of a material
     *
     * @param material
     * @return
     */
    private Material getCrop(Material material) {
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
            default:
                return material;
        }
    }

    private boolean checkPermission(Player player, String permission) {
        FarmAssistReboot.debug("MainPermissions: "+config.getPermission() +",hasPermission: "+ player.hasPermission("farmassist." + permission));
        return !config.getPermission() || player.hasPermission("farmassist." + permission);
    }

    private boolean isRipe(@NotNull Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
