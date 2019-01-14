package io.github.sarhatabaot.farmassistreboot.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class FarmAssistCrops {
    private static List<Material> cropList;

    public FarmAssistCrops() {
        cropList = createCropList();
    }

    private List<Material> createCropList(){
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
        return cropList;
    }

    public static List<Material> getCropList() {
        return cropList;
    }
}
