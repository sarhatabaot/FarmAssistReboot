package com.github.sarhatabaot.farmassistreboot;


import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum Crop {
	WHEAT(new Material[]{Material.FARMLAND}, Material.WHEAT, Material.WHEAT_SEEDS),
	SUGAR_CANE(new Material[]{Material.GRASS_BLOCK,Material.SAND,Material.DIRT}, Material.SUGAR_CANE, Material.SUGAR_CANE),
	NETHER_WART(new Material[]{Material.SOUL_SAND}, Material.NETHER_WART, Material.NETHER_WART),
	COCOA(new Material[]{Material.JUNGLE_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_JUNGLE_WOOD, Material.JUNGLE_WOOD}, Material.COCOA, Material.COCOA_BEANS),
	CARROTS(new Material[]{Material.FARMLAND}, Material.CARROTS, Material.CARROT),
	POTATOES(new Material[]{Material.FARMLAND}, Material.POTATOES, Material.POTATO),
	BEETROOTS(new Material[]{Material.FARMLAND},Material.BEETROOTS,Material.BEETROOT_SEEDS),
	CACTUS(new Material[]{Material.SAND},Material.CACTUS,Material.CACTUS)
	;

	private final Material[] plantedOn;
	private final Material planted;
	private final Material seed;


	Crop(final Material[] plantedOn, final Material planted, final Material seed) {
		this.plantedOn = plantedOn;
		this.planted = planted;
		this.seed = seed;
	}


}
