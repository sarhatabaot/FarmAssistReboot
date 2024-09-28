package com.github.sarhatabaot.farmassistreboot.utils;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;


public class ReplantTask extends BukkitRunnable {
    private final Crop crop;
    private final Block block;

    public ReplantTask(Crop crop, Block block) {
        this.crop = crop;
        this.block = block;
    }

    @Override
    public void run() {
        if (crop.getCropItem() == XMaterial.COCOA_BEANS.parseMaterial()) {
            //do stuff related to cocoa
        }

        block.setType(crop.getCropItem());
        BlockState state = block.getState();
        XBlock.setType(block, XMaterial.matchXMaterial(crop.getCropItem()));
        XBlock.setAge(block, 0);
        state.update(true);
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

//    private boolean matchedRelativeType(final Material[] materials, final Material relativeType) {
//        return Arrays.stream(materials).anyMatch(m -> m == relativeType);
//    }
}
