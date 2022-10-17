package com.github.sarhatabaot.farmassistreboot.tasks;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReplantTask extends BukkitRunnable {
    private final FarmAssistReboot plugin;
    private final Block block;
    private final Material material;

    private Cocoa cocoa;

    public ReplantTask(@NotNull Block block, final FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.block = block;
        this.material = block.getType();

        if (block.getType() == Material.COCOA) {
            this.cocoa = (Cocoa) block.getBlockData().clone();
            this.cocoa.setAge(0);
        }
    }

    @Override
    public void run() {
        plugin.debug(ReplantTask.class, String.format(Debug.ReplantTask.RUN, block.getType().name(), material.name()));
        switch (material) {
            case COCOA:
                setCocoaOrDropSeed();
                break;
            case FARMLAND:
                if (this.block.getRelative(BlockFace.UP).getType() == Material.AIR)
                    this.block.getRelative(BlockFace.UP).setType(Material.WHEAT);
                break;
            default:
                setBlockAndDropItem(material);
                break;

        }
    }

    private void setBlockAndDropItem(final @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        if (isBottomBlock(crop.getPlantedOn()) && block.getType() == Material.AIR) {
            setBlock(crop.getPlanted());
        } else {
            dropItem(material);
        }
    }

    private void setCocoaOrDropSeed() {
        if (this.block.getType() != Material.AIR) {
            return;
        }

        final Material relativeType = this.block.getRelative(cocoa.getFacing()).getType();
        if(matchedRelativeType(Crop.COCOA.getPlantedOn(),relativeType)) {
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
        this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(crop.getSeed()));
    }

    private boolean matchedRelativeType(final Material[] materials, final Material relativeType) {
        return Arrays.stream(materials).anyMatch(m -> m == relativeType);
    }

    private boolean isBottomBlock(Material @NotNull [] materials) {
        for (Material bottomMaterial : materials) {
            if (isBottomBlock(bottomMaterial))
                return true;
        }
        return false;
    }

    private boolean isBottomBlock(Material material) {
        return this.block.getRelative(BlockFace.DOWN).getType() == material;
    }

    private @NotNull BlockData setCropAge() {
        Ageable age = (Ageable) this.block.getBlockData();
        age.setAge(0);
        return age;
    }
}
