package com.github.sarhatabaot.farmassistreboot.config;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class FarmAssistCrops {
    private static ImmutableList<Material> cropList = ImmutableList.of(
            Material.WHEAT,
            Material.SUGAR_CANE,
            Material.NETHER_WART,
            Material.COCOA,
            Material.CARROTS,
            Material.POTATOES,
            Material.ATTACHED_MELON_STEM,
            Material.ATTACHED_PUMPKIN_STEM,
            Material.PUMPKIN_STEM,
            Material.MELON_STEM,
            Material.BEETROOTS,
            Material.CACTUS
    );


    public static ImmutableList<Material> getCropList() {
        return cropList;
    }
}
