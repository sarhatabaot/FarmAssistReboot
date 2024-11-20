package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractionListener implements Listener {
    private final FarmAssistReboot plugin;

    public PlayerInteractionListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    /**
     * On till event
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.isGlobalEnabled()
                || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || plugin.getDisabledPlayerList().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        final Material itemInMainHandType = event.getPlayer().getInventory().getItemInMainHand().getType();
        if (!(isHoe(itemInMainHandType) && isPlayerBlockFarmable(event))) {
            debug(Debug.OnPlayerInteract.BLOCK_FARMABLE, isPlayerBlockFarmable(event));
            debug(Debug.OnPlayerInteract.IS_ITEM_HOE, isHoe(itemInMainHandType));
            return;
        }
        // Permission Checks
        if (doesNotHaveWheatAndTillPermissions(event.getPlayer())) {
            return;
        }

        final Player player = event.getPlayer();
        debug(Debug.OnPlayerInteract.CONFIG_WHEAT, plugin.getAssistConfig().getEnabled(Material.WHEAT));
        debug(Debug.OnPlayerInteract.CONFIG_PLANT_ON_TILL, plugin.getAssistConfig().plantWheatOnTill());

        if (Util.isWorldDisabled(event.getPlayer().getWorld().getName())) {
            debug(Debug.OnPlayerInteract.WORLD_DISABLED, event.getPlayer().getWorld().getName());
            return;
        }
        if (!plugin.getAssistConfig().getEnabled(Material.WHEAT) || !plugin.getAssistConfig().plantWheatOnTill()) {
            debug(Debug.OnPlayerInteract.WHEAT_ENABLED, plugin.getAssistConfig().getEnabled(Material.WHEAT));
            debug(Debug.OnPlayerInteract.TILL_ENABLED, plugin.getAssistConfig().plantWheatOnTill());
            return;
        }

        if (Util.checkSeedsOrNoSeedsInInventory(event.getPlayer(), Material.WHEAT)) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block != null) {
            event.getClickedBlock().setType(Material.FARMLAND);
        }

        Util.replant(player, event.getClickedBlock(), Material.WHEAT);
    }

    private boolean doesNotHaveWheatAndTillPermissions(final Player player) {
        return plugin.getAssistConfig().usePermissions() && (!player.hasPermission(Permissions.WHEAT)) || !player.hasPermission(Permissions.TILL);
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
        if (!event.hasBlock() || event.getClickedBlock() == null) {
            return false;
        }

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


