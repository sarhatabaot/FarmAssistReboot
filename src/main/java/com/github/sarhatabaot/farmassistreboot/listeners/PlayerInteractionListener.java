package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlayerInteractionListener implements Listener {
	private final FarmAssistReboot plugin;

	/**
	 * On till event
	 *
	 * @param event PlayerInteractEvent
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!plugin.isGlobalEnabled())
			return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if (plugin.getDisabledPlayerList().contains(event.getPlayer().getUniqueId()))
			return;
		if (!(isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event))) {
			debug("Is Block Farm-able: " + isPlayerBlockFarmable(event));
			debug("Is Item Hoe: " + isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()));
			return;
		}
		// Permission Checks
		if (FarmAssistConfig.USE_PERMISSIONS && (!event.getPlayer().hasPermission("farmassist.wheat")) || !event.getPlayer().hasPermission("farmassist.till")) {
			return;
		}

		final Player player = event.getPlayer();
		debug("config.wheat: " + FarmAssistConfig.getEnabled(Material.WHEAT));
		debug("config.plant-on-till: " + FarmAssistConfig.PLANT_WHEAT_ON_TILL);
		if (!Util.isWorldEnabled(event.getPlayer().getWorld())) {
			debug("world=" + event.getPlayer().getWorld().getName() + " is disabled.");
			return;
		}
		if (!FarmAssistConfig.getEnabled(Material.WHEAT) || !FarmAssistConfig.PLANT_WHEAT_ON_TILL) {
			debug("Wheat is=" + FarmAssistConfig.getEnabled(Material.WHEAT));
			debug("Till is" + !FarmAssistConfig.PLANT_WHEAT_ON_TILL);
			return;
		}

		if (Util.inventoryContainsSeeds(event.getPlayer().getInventory(), Material.WHEAT)) {
			event.getClickedBlock().setType(Material.FARMLAND);
			Util.replant(player, event.getClickedBlock(), Material.WHEAT);
		}

	}


	/**
	 * Checks if the material is a hoe
	 *
	 * @param material Material
	 * @return Return if material is a hoe
	 */
	private boolean isHoe(@NotNull Material material) {
		return material.name().contains("_HOE");
	}

	/**
	 * Checks if the block is farmable.
	 *
	 * @param event PlayerInteractEvent
	 * @return true if block is farmable
	 */
	private boolean isPlayerBlockFarmable(@NotNull PlayerInteractEvent event) {
		boolean isGrassOrDirt = event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT;
		boolean isTopBlockAir = event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
		return event.hasBlock() && isGrassOrDirt && isTopBlockAir;
	}

	private void debug(final String message) {
		plugin.debug(PlayerInteractionListener.class,message);
	}
}


