package com.github.sarhatabaot.farmassistreboot.crop;


import org.bukkit.Material;

import java.util.Arrays;

public class Crop {
    private final Material cropItem; //Once planted
    private final Material seeds; //Item to plant
    private final Material[] plantedOn;
    private final int maximumAge;

    public Crop(Material cropItem, Material seeds, Material[] plantedOn, int maximumAge) {
        this.cropItem = cropItem;
        this.seeds = seeds;
        this.plantedOn = plantedOn;
        this.maximumAge = maximumAge;
    }


    public Material getCropItem() {
        return cropItem;
    }

    public Material getSeeds() {
        return seeds;
    }

    public Material[] getPlantedOn() {
        return plantedOn;
    }

    public int getMaximumAge() {
        return maximumAge;
    }


    @Override
    public String toString() {
        return "Crop{" +
                "cropItem=" + cropItem +
                ", seeds=" + seeds +
                ", plantedOn=" + Arrays.toString(plantedOn) +
                ", maximumAge=" + maximumAge +
                '}';
    }
}
