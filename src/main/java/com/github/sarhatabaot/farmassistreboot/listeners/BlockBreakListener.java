package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.Crop;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {
    private final FarmAssistReboot plugin;

    public BlockBreakListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
		if (plugin.isGlobalDisabled()) {
            debug("Plugin is globally disabled.");
			return;
		}

		if (this.plugin.getDisabledPlayerList().contains(event.getPlayer().getUniqueId())) {
            debug("Player %s with uuid %s has the far functionality disabled.", event.getPlayer().getName(), event.getPlayer().getUniqueId());
			return;
		}

        if (plugin.getAssistConfig().usePermissions() && !hasMaterialPermission(event)) {
            final String permission = Permissions.BASE_PERMISSION + event.getBlock().getType().name();
            debug(Debug.OnBlockBreak.PLAYER_NO_PERMISSION, event.getPlayer().getDisplayName(), permission);
            return;
        }

        if (Util.isWorldDisabled(event.getPlayer().getWorld().getName())) {
            return;
        }

        Material material = event.getBlock().getType();
        debug(Debug.OnBlockBreak.BLOCK_BROKEN, material.name());
        if (!Crop.getCropList().contains(material)) {
            debug(Debug.OnBlockBreak.CROP_LIST_NO_MATERIAL, material.name());
            return;
        }

        debug(Debug.OnBlockBreak.CROP_LIST_CONTAINS, material.name());
        if (!plugin.getAssistConfig().getEnabled(material)) {
            debug(Debug.OnBlockBreak.MATERIAL_DISABLED, material.name());
            return;
        }


        applyReplant(event);
    }

    private boolean hasMaterialPermission(@NotNull BlockBreakEvent event) {
        return event.getPlayer().hasPermission(Permissions.BASE_PERMISSION + event.getBlock().getType().name());
    }

    private void applyReplant(@NotNull BlockBreakEvent event) {
        Material material = event.getBlock().getType();

        int slot = Util.inventoryContainsSeeds(event.getPlayer().getInventory(), material);
        if (Util.checkSeedsOrNoSeedsInInventory(event.getPlayer(), slot)) {
            debug(Debug.OnBlockBreak.NO_SEEDS, event.getPlayer().getName());
            debug("Material: %s, Seed: %s", material.name(), Crop.valueOf(material.name()).getSeed());
            return;
        }


        if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
            Util.replant(event.getPlayer(), event.getBlock(), material);
            return;
        }

        if (!plugin.getAssistConfig().getRipe(material) || isRipe(event.getBlock())) {
            debug(String.format("isRipeConfig %s: ", material) + plugin.getAssistConfig().getRipe(material));
            debug(String.format("isRipe %s: ", material) + isRipe(event.getBlock()));
            Util.replant(event.getPlayer(), event.getBlock(), slot);
        }
    }


    private boolean isRipe(@NotNull Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }

    private void debug(final String message, Object... args) {
        plugin.debug(BlockBreakListener.class, String.format(message, args));
    }
}
