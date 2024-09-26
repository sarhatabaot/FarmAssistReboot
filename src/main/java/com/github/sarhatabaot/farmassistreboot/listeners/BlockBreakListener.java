package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
        if (!this.cropManager.isNotSupportedCrop(block.getType())) {
            return;
        }

        if (!isFullyGrownCrop(block)) {
            return;
        }


        replant(block);
    }

    //this should just replant, not checks or anything
    private void replant(final @NotNull Block block) {
        final Crop crop = cropManager.getCropFromItem(block.getType());

        // Schedule the replanting after 1 tick (to ensure the block has been broken)
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(crop.getCropItem());
                BlockState state = block.getState();
                XBlock.setType(block, XMaterial.matchXMaterial(crop.getCropItem()));
                XBlock.setAge(block, 0);
                state.update(true);
            }
        }.runTaskLater(plugin, 1L);
    }

    private boolean isFullyGrownCrop(@NotNull Block block) {
        return XBlock.getAge(block) == cropManager.getCropFromItem(block.getType()).getMaximumAge();
    }


    //todo implement later
//    private void setCocoaOrDropSeed(final Block block) {
//        final Crop cocoa;
//        final Material relativeType = block.getRelative(cocoa.getFacing()).getType();
//        if (matchedRelativeType(cocoa.getPlantedOn(), relativeType)) {
//            block.setType(cocoa.getCropItem());
//            block.setBlockData(block.getBlockData());
//        } else {
//            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(XMaterial.COCOA_BEANS.parseMaterial()));
//        }
//    }

    private boolean matchedRelativeType(final Material[] materials, final Material relativeType) {
        return Arrays.stream(materials).anyMatch(m -> m == relativeType);
    }

}
