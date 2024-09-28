package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantTask;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class BlockBreakListener implements Listener {
    private final FarmAssistReboot plugin;
    private final CropManager cropManager;

    public BlockBreakListener(FarmAssistReboot plugin, CropManager cropManager) {
        this.plugin = plugin;
        this.cropManager = cropManager;
    }

    @EventHandler
    public void onBlockBreak(final @NotNull BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (this.cropManager.isNotSupportedCrop(block.getType())) {
            plugin.getLogger().info(() -> "Block " + block.getType() + " is not supported");
            return;
        }

        if (!isFullyGrownCrop(block)) {
            plugin.getLogger().info(() -> "Block " + block.getType() + " is not a fully grown crop.");
            plugin.getLogger().info(() -> "Current age: " + XBlock.getAge(block) + ", Fully grown age: " + cropManager.getCropFromItem(block.getType()).getMaximumAge());
            return;
        }

        //        if (isDisabled(block)) {
        //            plugin.getLogger().info(() -> "Block "+ block.getType() +" is disabled.");
        //            return;
        //        }

        event.setCancelled(true);
        dropItem(block ,event.getPlayer().getItemInHand());
        ReplantUtil.replant(block);
    }

    private void dropItem(final Block block, final ItemStack itemStack) {
        block.getDrops(itemStack).forEach( item -> block.getWorld().dropItemNaturally(block.getLocation(), item));
    }

    //todo
    private boolean isDisabled(final @NotNull Block block) {
        final Material material = block.getType();
        return true;
    }


    private boolean isFullyGrownCrop(@NotNull Block block) {
        return XBlock.getAge(block) >= cropManager.getCropFromItem(block.getType()).getMaximumAge();
    }


}
