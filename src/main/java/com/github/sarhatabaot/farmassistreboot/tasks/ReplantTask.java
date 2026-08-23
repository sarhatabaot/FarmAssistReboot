package com.github.sarhatabaot.farmassistreboot.tasks;

import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.Crop;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.github.sarhatabaot.farmassistreboot.Util.debug;

public class ReplantTask implements Runnable {
    private final FarmAssistReboot plugin;
    private final Block block;
    private final Material material;

    private Cocoa cocoa;

    public ReplantTask(@NotNull Block block, final FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.block = block;
        this.material = block.getType();

        if (XMaterial.matchXMaterial(block.getType()) == XMaterial.COCOA) {
            this.cocoa = (Cocoa) block.getBlockData().clone();
            this.cocoa.setAge(0);
        }
    }

    @Override
    public void run() {
        plugin.debug(ReplantTask.class, String.format(Debug.ReplantTask.RUN, block.getType().name(), material.name()));
        if (material == Material.COCOA) {
            setCocoaOrDropSeed();
        } else {
            setBlockAndDropItem(material);
        }
    }

    private void setBlockAndDropItem(final @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        if (isOnAnyOf(crop.getPlantedOn()) && block.getType() == Material.AIR) {
            setBlock(crop.getPlanted().get());
        } else {
            dropItem(material);
        }
    }

    private void setCocoaOrDropSeed() {
        if (this.block.getType() != Material.AIR) {
            return;
        }

        if (cocoa == null) {
            this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
            return;
        }

        final Material relativeType = this.block.getRelative(cocoa.getFacing()).getType();
        if(matchedRelativeType(Crop.COCOA.getPlantedOn(), XMaterial.matchXMaterial(relativeType))) {
            this.block.setType(material);
            this.block.setBlockData(cocoa);
        } else {
            this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
        }
    }

    private void setBlock(final Material material) {
        this.block.setType(material);
        this.block.setBlockData(setCropAge());
    }

    private void dropItem(final @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        final ItemStack seedItem = crop.getSeed().parseItem();
        if (seedItem == null) {
            debug("There was a problem parsing the crop or the seed item for: %s", material.name());
            return;
        }

        this.block.getWorld().dropItemNaturally(block.getLocation(), seedItem);
    }

    private boolean matchedRelativeType(final XMaterial[] materials, final XMaterial relativeType) {
        return Arrays.stream(materials).anyMatch(m -> m == relativeType);
    }

    /**
     * Returns {@code true} if the block directly below this task's block is
     * one of the given {@link XMaterial} values. C7: renamed from
     * {@code isBottomBlock} for clarity; the previous name described the
     * implementation (the block below), not the contract (is this block
     * "on" one of these materials?).
     *
     * @param materials the materials the block below may match
     * @return {@code true} if {@code block.getRelative(BlockFace.DOWN)} is
     *         any of the given materials
     */
    private boolean isOnAnyOf(XMaterial @NotNull [] materials) {
        Material below = this.block.getRelative(BlockFace.DOWN).getType();
        for (XMaterial candidate : materials) {
            if (candidate.get() == below) {
                return true;
            }
        }
        return false;
    }

    private @NotNull BlockData setCropAge() {
        Ageable age = (Ageable) this.block.getBlockData();
        age.setAge(0);
        return age;
    }
}
