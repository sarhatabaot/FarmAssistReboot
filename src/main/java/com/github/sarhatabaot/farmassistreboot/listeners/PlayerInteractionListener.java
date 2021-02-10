package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
			FarmAssistReboot.debug("Is Block Farmable: " + isPlayerBlockFarmable(event));
			FarmAssistReboot.debug("Is Item Hoe: " + isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()));
			return;
		}
		// Permission Checks
		if (FarmAssistConfig.USE_PERMISSIONS && (!event.getPlayer().hasPermission("farmassist.wheat")) || !event.getPlayer().hasPermission("farmassist.till")) {
			return;
		}

		final Player player = event.getPlayer();
		FarmAssistReboot.debug("config.wheat: " + FarmAssistConfig.getEnabled(Material.WHEAT));
		FarmAssistReboot.debug("config.plant-on-till: " + FarmAssistConfig.PLANT_WHEAT_ON_TILL);
		if (!Util.isWorldEnabled(event.getPlayer().getWorld())) {
			FarmAssistReboot.debug("world=" + event.getPlayer().getWorld().getName() + " is disabled.");
			return;
		}
		if (!FarmAssistConfig.getEnabled(Material.WHEAT) || !FarmAssistConfig.PLANT_WHEAT_ON_TILL) {
			FarmAssistReboot.debug("Wheat is=" + FarmAssistConfig.getEnabled(Material.WHEAT));
			FarmAssistReboot.debug("Till is" + !FarmAssistConfig.PLANT_WHEAT_ON_TILL);
			return;
		}

		if (Util.inventoryContains(event.getPlayer().getInventory(), Material.WHEAT)) {
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
	private boolean isHoe(Material material) {
		return material == Material.WOODEN_HOE
				|| material == Material.STONE_HOE
				|| material == Material.IRON_HOE
				|| material == Material.GOLDEN_HOE
				|| material == Material.DIAMOND_HOE;
	}

	/**
	 * Checks if the block is farmable.
	 *
	 * @param event PlayerInteractEvent
	 * @return true if block is farmable
	 */
	private boolean isPlayerBlockFarmable(PlayerInteractEvent event) {
		boolean isGrassOrDirt = event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT;
		boolean isTopBlockAir = event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
		return event.hasBlock() && isGrassOrDirt && isTopBlockAir;
	}
}


