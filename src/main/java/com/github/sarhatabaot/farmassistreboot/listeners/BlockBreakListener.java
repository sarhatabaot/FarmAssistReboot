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


        ReplantUtil.replant(block);
    }


    private boolean isFullyGrownCrop(@NotNull Block block) {
        return XBlock.getAge(block) == cropManager.getCropFromItem(block.getType()).getMaximumAge();
    }


}
