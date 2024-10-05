package com.github.sarhatabaot.farmassistreboot.utils;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class ReplantTask extends BukkitRunnable {
    private final FarmAssistReboot plugin;
    private final Crop crop;
    private final Block block;

    public ReplantTask(FarmAssistReboot plugin, Crop crop, Block block) {
        this.plugin = plugin;
        this.crop = crop;
        this.block = block;
    }


    @Override
    public void run() {
        XMaterial xMaterial = XMaterial.matchXMaterial(crop.getCropItem());
        switch (xMaterial) {
            case COCOA_BEANS: {
                plugin.trace("Replanting cocoa.");

                final BlockFace direction = XBlock.getDirection(block);
                final Material relativeType = block.getRelative(direction).getType();

                if (matchedRelativeType(crop.getPlantedOn(), relativeType)) {
                    setTypeAndAge(block,XMaterial.COCOA_BEANS);
                    XBlock.setDirection(block, direction);
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(XMaterial.COCOA_BEANS.parseMaterial()));
                }
                break;
            }
            case WHEAT: {
                if (Util.getJsonCropVersionFromMinecraftVersion().equals("1.0")) {
                    plugin.trace("Replanting legacy wheat.");
                    setLegacyWheat(block);
                } else {
                    setTypeAndAge(block, XMaterial.WHEAT);
                }
                break;
            }
            case SUGAR_CANE: {
                if (Util.getJsonCropVersionFromMinecraftVersion().equals("1.0")) {
                    plugin.trace("Replanting legacy sugar cane.");
                    setLegacySugarCane(block);
                } else {
                    setTypeAndAge(block, XMaterial.SUGAR_CANE);
                }
            }
            default: {
                setTypeAndAge(block, xMaterial);
                break;
            }
        }
        plugin.trace("Replanted crop " + crop);
    }

    private void setTypeAndAge(Block block, XMaterial material) {
        XBlock.setType(block, material);
        setAge(block);
    }

    private void setLegacyWheat(@NotNull Block block) {
        block.setType(Material.matchMaterial("CROPS"));
        setAge(block);
    }

    private void setLegacySugarCane(@NotNull Block block) {
        block.setType(Material.matchMaterial("SUGAR_CANE_BLOCK"));
        setAge(block);
    }

    private void setAge(Block block) {
        XBlock.setAge(block, 0);
    }

    private boolean matchedRelativeType(final Material[] materials, final Material relativeType) {
        return Arrays.stream(materials).anyMatch(m -> m == relativeType);
    }
}
