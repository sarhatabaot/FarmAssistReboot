package com.github.sarhatabaot.farmassistreboot.utils;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sarhatabaot
 */
public class ReplantTask extends BukkitRunnable {
    private final Crop crop;
    private final Block block;

    public ReplantTask(Crop crop, Block block) {
        this.crop = crop;
        this.block = block;
    }

    @Override
    public void run() {
        block.setType(crop.getCropItem());
        BlockState state = block.getState();
        XBlock.setType(block, XMaterial.matchXMaterial(crop.getCropItem()));
        XBlock.setAge(block, 0);
        state.update(true);
    }
}
