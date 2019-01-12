package io.github.sarhatabaot.farmassistreboot.config;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class FarmAssistCrops {
    private List<Material> cropList;

    public FarmAssistCrops() {
        this.cropList = createCropList();
    }

    private List<Material> createCropList(){
        ArrayList<Material> cropList = new ArrayList<>();
        cropList.add(Material.WHEAT);
        cropList.add(Material.SUGAR_CANE);
        cropList.add(Material.NETHER_WART);
        cropList.add(Material.COCOA_BEANS);
        cropList.add(Material.CARROT);
        cropList.add(Material.POTATO);
        cropList.add(Material.PUMPKIN);
        cropList.add(Material.MELON);
        cropList.add(Material.BEETROOT);
        return cropList;
    }

    public List<Material> getCropList() {
        return cropList;
    }
}
