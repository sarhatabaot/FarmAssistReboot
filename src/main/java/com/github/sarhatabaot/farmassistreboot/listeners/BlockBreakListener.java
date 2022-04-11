package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {
	private final ImmutableList<Material> cropList = ImmutableList.of(
			Material.WHEAT,
			Material.SUGAR_CANE, //doesn't work - the only one, others are all tested
			Material.NETHER_WART,
			Material.COCOA,
			Material.CARROTS,
			Material.POTATOES,
			Material.BEETROOTS,
			Material.CACTUS
	);

	private final FarmAssistReboot plugin;

	@EventHandler(
			priority = EventPriority.HIGHEST,
			ignoreCancelled = true
	)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!plugin.isGlobalEnabled())
			return;

		if (this.plugin.getDisabledPlayerList().contains(event.getPlayer().getUniqueId()))
			return;


		if (!cropList.contains(event.getBlock().getType())) {
			debug("Crop List doesn't contain: " + event.getBlock().getType().name());
			return;
		}

		if (FarmAssistConfig.USE_PERMISSIONS && !hasMaterialPermission(event)) {
			String playerName = "Player: " + event.getPlayer().getDisplayName();
			String permission = "farmassist." + event.getBlock().getType().name();
			debug(playerName + ", " + "doesn't have permission " + permission);
			return;
		}

		if (Util.isWorldEnabled(event.getPlayer().getWorld())) {
			debug("Is" + event.getPlayer().getWorld().getName() + " enabled: " + Util.isWorldEnabled(event.getPlayer().getWorld()));
			applyReplant(event);
		}
	}

	private boolean hasMaterialPermission(@NotNull BlockBreakEvent event) {
		return event.getPlayer().hasPermission("farmassist." + event.getBlock().getType().name());
	}

	private void applyReplant(@NotNull BlockBreakEvent event) {
		Material material = event.getBlock().getType();
		debug("Block broken: " + material.name());
		if (!cropList.contains(material)) {
			debug("Crop List doesn't contain: " + material.name());
			return;
		}
		debug("Crop List contains: " + material.name());
		if (!FarmAssistConfig.getEnabled(material)) {
			debug("Material="+material.name()+" is disabled.");
			return;
		}


		if (!Util.inventoryContainsSeeds(event.getPlayer().getInventory(), material)) {
			debug("Player doesn't have the correct seeds/material to replant");
			return;
		}

		if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
			Util.replant(event.getPlayer(), event.getBlock(), material);
			return;
		}

		if (!FarmAssistConfig.getRipe(material) || isRipe(event.getBlock())) {
			Util.replant(event.getPlayer(), event.getBlock(), material);
		}
	}


	private boolean isRipe(@NotNull Block block) {
		Ageable age = (Ageable) block.getBlockData();
		return (age.getAge() == age.getMaximumAge());
	}


	private void debug(final String message) {
		plugin.debug(BlockBreakListener.class,message);
	}
}
