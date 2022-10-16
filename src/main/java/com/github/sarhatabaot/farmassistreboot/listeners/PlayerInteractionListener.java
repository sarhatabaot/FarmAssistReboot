package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        if (!plugin.isGlobalEnabled()
                || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || plugin.getDisabledPlayerList().contains(event.getPlayer().getUniqueId()))
            return;

        if (!(isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event))) {
            debug(Debug.OnPlayerInteract.BLOCK_FARMABLE, isPlayerBlockFarmable(event));
            debug(Debug.OnPlayerInteract.IS_ITEM_HOE, isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()));
            return;
        }
        // Permission Checks
        if (FarmAssistConfig.USE_PERMISSIONS && (!event.getPlayer().hasPermission(Permissions.WHEAT)) || !event.getPlayer().hasPermission(Permissions.TILL)) {
            return;
        }

        final Player player = event.getPlayer();
        debug(Debug.OnPlayerInteract.CONFIG_WHEAT, FarmAssistConfig.getEnabled(Material.WHEAT));
        debug(Debug.OnPlayerInteract.CONFIG_PLANT_ON_TILL, FarmAssistConfig.PLANT_WHEAT_ON_TILL);

        if (!Util.isWorldEnabled(event.getPlayer().getWorld())) {
            debug(Debug.OnPlayerInteract.WORLD_DISABLED, event.getPlayer().getWorld().getName());
            return;
        }
        if (!FarmAssistConfig.getEnabled(Material.WHEAT) || !FarmAssistConfig.PLANT_WHEAT_ON_TILL) {
            debug(Debug.OnPlayerInteract.WHEAT_ENABLED, FarmAssistConfig.getEnabled(Material.WHEAT));
            debug(Debug.OnPlayerInteract.TILL_ENABLED, FarmAssistConfig.PLANT_WHEAT_ON_TILL);
            return;
        }

        if (Util.inventoryContainsSeeds(event.getPlayer().getInventory(), Material.WHEAT)) {
            event.getClickedBlock().setType(Material.FARMLAND); //checked if null @isPlayerBlockFarmable
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
        if(!event.hasBlock() || event.getClickedBlock() == null)
            return false;

        final Block clickedBlock = event.getClickedBlock();

        return event.hasBlock() && isGrassOrDirt(clickedBlock) && isTopBlockAir(clickedBlock);
    }

    private boolean isGrassOrDirt(final @NotNull Block block) {
        return block.getType() == Material.GRASS_BLOCK || block.getType() == Material.DIRT;
    }

    private boolean isTopBlockAir(final @NotNull Block block) {
        return block.getRelative(BlockFace.UP).getType() == Material.AIR;
    }

    private void debug(final String message) {
        plugin.debug(PlayerInteractionListener.class, message);
    }

    private void debug(final String message, Object... args) {
        debug(String.format(message, args));
    }
}


