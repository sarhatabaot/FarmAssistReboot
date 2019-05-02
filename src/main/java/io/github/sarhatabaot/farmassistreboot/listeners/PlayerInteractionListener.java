package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.Util;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.sarhatabaot.farmassistreboot.FarmAssistReboot.debug;
import static io.github.sarhatabaot.farmassistreboot.Util.*;

public class PlayerInteractionListener implements Listener {
    private FarmAssistReboot plugin;
    private FarmAssistConfig config = FarmAssistConfig.getInstance();

    public PlayerInteractionListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    /**
     * On till event
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!plugin.isGlobalEnabled() && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if(this.plugin.getDisabledPlayerList().contains(event.getPlayer().getName()))
            return;
        if (!(isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event))) {
            return;
        }
        // Permission Checks
        if (config.isPermissionEnabled() && (!event.getPlayer().hasPermission("farmassist.wheat")) || !event.getPlayer().hasPermission("farmassist.till")) {
            String wheatPermission = "\u001b[36m farmassist.wheat\u001b[0m";
            String tillPermission ="\u001b[36m farmassist.till\u001b[0m";
            String playerName = "Player: "+event.getPlayer().getDisplayName();
            debug(playerName+","+tillPermission+": "+event.getPlayer().hasPermission("farmassist.till"));
            debug(playerName+","+wheatPermission+": "+event.getPlayer().hasPermission("farmassist.wheat"));
            return;
        }
        Player player = event.getPlayer();
        debug("Config.Wheat: "+config.getEnabled(Material.WHEAT));
        debug("Config.Plant on Till: "+config.getPlantOnTill());

        if (isWorldEnabled(event.getPlayer().getWorld()) && config.getEnabled(Material.WHEAT) && config.getPlantOnTill()) {
            if (inventoryContains(event.getPlayer(), Material.WHEAT)) {
                // override block type TODO: Better way to implement this
                Block block = event.getClickedBlock();
                block.setType(Material.FARMLAND);
                replant(player, block, Material.WHEAT_SEEDS);
            }
        }
    }


    /**
     * Checks if the material is a hoe
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
     * @param event PlayerInteractEvent
     * @return Returns if the block is farmable.
     */
    private boolean isPlayerBlockFarmable(PlayerInteractEvent event) {
        boolean isGrassOrDirt = event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT;
        boolean isTopBlockAir = event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
        return event.hasBlock() && isGrassOrDirt && isTopBlockAir;
    }
}


