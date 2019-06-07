package com.github.sarhatabaot.farmassist.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class FarmAssistCrops {
    private FarmAssistCrops() {
        throw new IllegalStateException("Utility class");
    }

    private static List<Material> cropList = createCropList();

    private static List<Material> createCropList(){
        ArrayList<Material> cropList = new ArrayList<>();
        cropList.add(Material.WHEAT);
        cropList.add(Material.SUGAR_CANE);
        cropList.add(Material.NETHER_WART);
        cropList.add(Material.COCOA);
        cropList.add(Material.CARROTS);
        cropList.add(Material.POTATOES);
        cropList.add(Material.ATTACHED_MELON_STEM);
        cropList.add(Material.ATTACHED_PUMPKIN_STEM);
        cropList.add(Material.PUMPKIN_STEM);
        cropList.add(Material.MELON_STEM);
        cropList.add(Material.BEETROOTS);
        cropList.add(Material.CACTUS);
        cropList.add(Material.SWEET_BERRY_BUSH);
        return cropList;
    }

    public static List<Material> getCropList() {
        return cropList;
    }
}
