package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.Crop;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class BlockBreakListener implements Listener {
	private final ImmutableList<Material> cropList = ImmutableList.of(
			Material.WHEAT,
			Material.SUGAR_CANE,
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

		if (FarmAssistConfig.USE_PERMISSIONS && !hasMaterialPermission(event)) {
			String playerName = "Player: " + event.getPlayer().getDisplayName();
			String permission = "farmassist." + event.getBlock().getType().name();
			FarmAssistReboot.debug(playerName + ", " + "doesn't have permission " + "\u001b[36m" + permission + "\u001b[0m");
			return;
		}

		if (Util.isWorldEnabled(event.getPlayer().getWorld())) {
			FarmAssistReboot.debug("Is" + event.getPlayer().getWorld().getName() + " enabled: " + Util.isWorldEnabled(event.getPlayer().getWorld()));
			applyReplant(event);
		}
	}

	private boolean hasMaterialPermission(BlockBreakEvent event) {
		return event.getPlayer().hasPermission("farmassist." + event.getBlock().getType().name());
	}

	/**
	 * @param player
	 * @param block    Block broken
	 * @param material Material to remove from inventory
	 */
	private void replant(@NotNull Player player, Block block, Material material) {
		Crop crop = Crop.valueOf(material.name());
		int spot = player.getInventory().first(crop.getSeed());
		if (spot >= 0) {
			removeOrSubtractItem(player, spot);
			new ReplantTask(block).runTaskLater(this.plugin, 5L);
		}
	}

	private void removeOrSubtractItem(Player player, int spot) {
		ItemStack next = player.getInventory().getItem(spot);
		if (next.getAmount() > 1) {
			next.setAmount(next.getAmount() - 1);
			player.getInventory().setItem(spot, next);
		} else {
			player.getInventory().setItem(spot, new ItemStack(Material.AIR));
		}
	}

	private void applyReplant(BlockBreakEvent event) {
		Material material = event.getBlock().getType();
		FarmAssistReboot.debug("Block broken: " + material.name());
		if (!cropList.contains(material)) {
			FarmAssistReboot.debug("Crop List doesn't contain: " + material.name());
			return;
		}
		FarmAssistReboot.debug("Crop List contains: " + material.name());
		if (!FarmAssistConfig.getEnabled(material)) {
			FarmAssistReboot.debug("Material="+material.name()+" is disabled.");
			return;
		}


		if (!Util.inventoryContains(event.getPlayer().getInventory(), material)) {
			FarmAssistReboot.debug("Player doesn't have the correct seeds/material to replant");
			return;
		}

		if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
			replant(event.getPlayer(), event.getBlock(), material);
			return;
		}

		if (!FarmAssistConfig.getRipe(material) || isRipe(event.getBlock())) {
			replant(event.getPlayer(), event.getBlock(), material);
		}


	}


	private boolean isRipe(@NotNull Block block) {
		Ageable age = (Ageable) block.getBlockData();
		return (age.getAge() == age.getMaximumAge());
	}
}
