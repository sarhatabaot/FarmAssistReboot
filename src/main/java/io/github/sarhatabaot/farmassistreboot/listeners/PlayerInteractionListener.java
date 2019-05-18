package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.Material;
import org.bukkit.World;
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
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.isGlobalEnabled() ||
                !event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ||
                plugin.getDisabledPlayerList().contains(event.getPlayer().getName()) ||
                !canPlayerFarmBlock(event))
            return;
        if (!checkPermissions(event)) return;

        Player player = event.getPlayer();
        debug("Config.Wheat: " + config.getEnabled(Material.WHEAT));
        debug("Config.Plant on Till: " + config.getPlantOnTill());

        if (isPlantOnTillEnabled(player.getWorld()) && inventoryContains(player, Material.WHEAT)) {
            Block block = event.getClickedBlock();
            block.setType(Material.FARMLAND); //should be covered by minecraft
            replant(player, block, Material.WHEAT_SEEDS);
        }
    }

    private boolean checkPermissions(PlayerInteractEvent event){
        if (config.isPermissionEnabled() &&
                (!event.getPlayer().hasPermission("farmassist.wheat")) || !event.getPlayer().hasPermission("farmassist.till")) {
            debug(event.getPlayer().getName() + " has no permission");
            return false;
        }
        return true;
    }

    private boolean isPlantOnTillEnabled(World world) {
        return isWorldEnabled(world) && config.getEnabled(Material.WHEAT) && config.getPlantOnTill();
    }

    private boolean canPlayerFarmBlock(PlayerInteractEvent event) {
        return isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event);
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
     * @return Returns if the block is farmable.
     */
    private boolean isPlayerBlockFarmable(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        boolean isTopBlockAir = block.getRelative(BlockFace.UP).getType() == Material.AIR;
        return event.hasBlock() && isBlockFarmable(block) && isTopBlockAir;
    }

    private boolean isBlockFarmable(Block block) {
        switch (block.getType()) {
            case GRASS_BLOCK:
            case DIRT:
            case FARMLAND:
                return true;
            default:
                return false;
        }
    }
}


