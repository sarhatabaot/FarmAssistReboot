package com.github.sarhatabaot.farmassistreboot;


import org.bukkit.Material;

public enum Crop {
	WHEAT(Material.FARMLAND, Material.WHEAT, Material.WHEAT_SEEDS),
	SUGAR_CANE(Material.GRASS, Material.SUGAR_CANE, Material.SUGAR_CANE),
	NETHER_WART(Material.SOUL_SAND, Material.NETHER_WART, Material.NETHER_WART),
	COCOA(Material.JUNGLE_LOG, Material.COCOA, Material.COCOA),
	CARROTS(Material.FARMLAND, Material.CARROTS, Material.CARROT),
	POTATOES(Material.FARMLAND, Material.POTATOES, Material.POTATO),
	BEETROOT(Material.FARMLAND,Material.BEETROOTS,Material.BEETROOT_SEEDS),
	CACTUS(Material.SAND,Material.CACTUS,Material.CACTUS)
	;

	private final Material plantedOn;
	private final Material planted;
	private final Material seed;


	Crop(final Material plantedOn, final Material planted, final Material seed) {
		this.plantedOn = plantedOn;
		this.planted = planted;
		this.seed = seed;
	}


	public Material getPlantedOn() {
		return plantedOn;
	}

	public Material getPlanted() {
		return planted;
	}

	public Material getSeed() {
		return seed;
	}
}
