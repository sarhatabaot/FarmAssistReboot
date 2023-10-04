package com.github.sarhatabaot.farmassistreboot.tasks;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.Crop;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReplantTask implements Runnable {
    private final FarmAssistReboot plugin;
    private final Block block;
    private final XMaterial material;

    public ReplantTask(@NotNull Block block, final FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.block = block;
        this.material = XMaterial.matchXMaterial(block.getType());

        if (block.getType() == Material.COCOA) {
            XBlock.setAge(this.block, 0);
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
                setBlockAndDropItem(material.parseMaterial());
                break;

        }
    }

    private void setBlockAndDropItem(final @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        if (isBottomBlock(crop.getPlantedOn()) && block.getType() == Material.AIR) {
            setBlock(crop.getPlanted().parseMaterial());
        } else {
            dropItem(material);
        }
    }

    private void setCocoaOrDropSeed() {
        if (this.block.getType() != Material.AIR) {
            return;
        }

        final Material relativeType = this.block.getRelative(XBlock.getDirection(this.block)).getType();
        if(matchedRelativeType(Crop.COCOA.getPlantedOn(),relativeType)) {
            this.block.setType(material.parseMaterial());
            XBlock.setAge(this.block, 0);
        } else {
            this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(XMaterial.COCOA_BEANS.parseMaterial()));
        }
    }

    private void setBlock(final Material material) {
        this.block.setType(material);
        XBlock.setAge(this.block,0);
    }

    private void dropItem(final @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(crop.getSeed().parseMaterial()));
    }

    private boolean matchedRelativeType(final XMaterial[] materials, final Material relativeType) {
        return Arrays.stream(materials).anyMatch(m -> m.parseMaterial() == relativeType);
    }

    private boolean isBottomBlock(XMaterial @NotNull [] materials) {
        for (XMaterial bottomMaterial : materials) {
            if (isBottomBlock(bottomMaterial))
                return true;
        }
        return false;
    }

    private boolean isBottomBlock(@NotNull XMaterial material) {
        return this.block.getRelative(BlockFace.DOWN).getType() == material.parseMaterial();
    }
}
