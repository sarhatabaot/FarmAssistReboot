package com.github.sarhatabaot.farmassistreboot;


import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Crop {
    WHEAT(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.WHEAT, XMaterial.WHEAT_SEEDS),
    SUGAR_CANE(new XMaterial[]{XMaterial.GRASS_BLOCK, XMaterial.SAND, XMaterial.DIRT}, XMaterial.SUGAR_CANE, XMaterial.SUGAR_CANE),
    NETHER_WART(new XMaterial[]{XMaterial.SOUL_SAND}, XMaterial.NETHER_WART, XMaterial.NETHER_WART),
    COCOA(new XMaterial[]{XMaterial.JUNGLE_LOG, XMaterial.STRIPPED_JUNGLE_LOG, XMaterial.STRIPPED_JUNGLE_WOOD, XMaterial.JUNGLE_WOOD}, XMaterial.COCOA, XMaterial.COCOA_BEANS),
    CARROTS(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.CARROTS, XMaterial.CARROT),
    POTATOES(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.POTATOES, XMaterial.POTATO),
    BEETROOTS(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.BEETROOTS, XMaterial.BEETROOT_SEEDS),
    CACTUS(new XMaterial[]{XMaterial.SAND}, XMaterial.CACTUS, XMaterial.CACTUS),
    TORCHFLOWER(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.TORCHFLOWER, XMaterial.TORCHFLOWER_SEEDS),
    PITCHER_PLANT(new XMaterial[]{XMaterial.FARMLAND}, XMaterial.PITCHER_PLANT, XMaterial.PITCHER_POD);

    private final XMaterial[] plantedOn;
    private final XMaterial planted;
    private final XMaterial seed;


    Crop(final XMaterial[] plantedOn, final XMaterial planted, final XMaterial seed) {
        this.plantedOn = plantedOn;
        this.planted = planted;
        this.seed = seed;
    }

    public static List<Material> getCropList() {
        return Arrays.stream(Crop.values()).map(crop -> crop.planted.parseMaterial()).collect(Collectors.toList());
    }


}
