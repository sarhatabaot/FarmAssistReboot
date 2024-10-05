package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XBlock;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantTask;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class BlockBreakListener implements Listener {
    private final FarmAssistReboot plugin;
    private final CropManager cropManager;
    private final MainConfig mainConfig;

    public BlockBreakListener(FarmAssistReboot plugin, CropManager cropManager, MainConfig mainConfig) {
        this.plugin = plugin;
        this.cropManager = cropManager;
        this.mainConfig = mainConfig;
    }

    @EventHandler
    public void onBlockBreak(final @NotNull BlockBreakEvent event) {
        if (!mainConfig.getEnabledWorlds().contains(event.getBlock().getWorld().getName())) {
            plugin.debug("World " + event.getBlock().getWorld().getName() + " is not enabled.");
            return;
        }

        final Block block = event.getBlock();
        if (this.cropManager.isNotSupportedCrop(block.getType())) {
            plugin.trace("Block " + block.getType() + " is not supported");
            return;
        }

        final Crop crop = cropManager.getCropFromItem(block.getType());
        if (isDisabled(crop)) {
            plugin.debug("Crop " + block.getType() + " is disabled.");
            return;
        }


        if (!hasPermissionForCrop(event.getPlayer(), crop)) {
            plugin.debug("Player " + event.getPlayer() + "does not have permission to replant crop " + crop);
            return;
        }

        if (!isFullyGrownCrop(block)) {
            plugin.debug("Block " + block.getType() + " is not a fully grown crop.");
            plugin.debug("Current age: " + XBlock.getAge(block) + ", Fully grown age: " + cropManager.getCropFromItem(block.getType()).getMaximumAge());
            return;
        }

        plugin.trace("Replanting crop from " + block.getType());
        event.setCancelled(true);
        dropItem(block, event.getPlayer().getItemInHand());
        replant(block);
    }

    private void dropItem(final @NotNull Block block, final ItemStack itemStack) {
        block.getDrops(itemStack).forEach(item -> block.getWorld().dropItemNaturally(block.getLocation(), item));
    }

    private boolean isDisabled(final @NotNull Crop crop) {
        return mainConfig.getDisabledCrops().contains(cropManager.getCropName(crop));
    }

    private boolean hasPermissionForCrop(final @NotNull Player player, final Crop crop) {
        return player.hasPermission(Util.getCropPermission(cropManager.getCropName(crop)));
    }

    private boolean isFullyGrownCrop(@NotNull Block block) {
        return XBlock.getAge(block) >= cropManager.getCropFromItem(block.getType()).getMaximumAge();
    }

    public void replant(final Block blockBroken) {
        final Crop crop = cropManager.getCropFromItem(blockBroken.getType());
        // Schedule the replanting after 1 tick (to ensure the block has been broken)
        new ReplantTask(plugin, crop, blockBroken).runTaskLater(plugin, 1L);
    }

}
