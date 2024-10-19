package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.DebugUtil;
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
            plugin.debug(DebugUtil.WORLD_NOT_ENABLED, event.getBlock().getWorld().getName());
            return;
        }

        final Block block = event.getBlock();
        if (this.cropManager.isNotSupportedCrop(block.getType())) {
            plugin.trace(DebugUtil.BLOCK_NOT_SUPPORTED, block.getType());
            return;
        }

        final Crop crop = cropManager.getCropFromItem(block.getType());
        if (isDisabled(crop)) {
            plugin.debug(DebugUtil.CROP_DISABLED, block.getType());
            return;
        }


        if (!hasPermissionForCrop(event.getPlayer(), crop)) {
            plugin.debug("Player " + event.getPlayer() + "does not have permission to replant crop " + crop);
            return;
        }

        if (mainConfig.isReplantWhenRipe() && isNotFullyGrownCrop(block, crop)) {
            plugin.debug("Block " + block.getType() + " is not a fully grown crop.");
            plugin.debug("Current age: " + XBlock.getAge(block) + ", Fully grown age: " + crop.getMaximumAge());
            return;
        }

        plugin.trace("Block " + block.getType());
        plugin.trace("Current age: " + XBlock.getAge(block) + ", Fully grown age: " + crop.getMaximumAge());

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

    // TODO
    // https://github.com/sarhatabaot/FarmAssistReboot/issues/101
    private boolean isNotFullyGrownCrop(final Block block, final Crop crop) {
        return XBlock.getAge(block) < crop.getMaximumAge();
    }

    public void replant(final Block blockBroken) {
        final Crop crop = cropManager.getCropFromItem(blockBroken.getType());
        // Schedule the replanting after 1 tick (to ensure the block has been broken)
        new ReplantTask(plugin, crop, blockBroken).runTaskLater(plugin, 1L);
    }

}
