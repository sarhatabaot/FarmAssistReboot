package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
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
import org.bukkit.inventory.ItemStack;

public class PlayerInteractionListener implements Listener {
    private FarmAssistReboot plugin;
    private FarmAssistConfig config = FarmAssistConfig.getInstance();

    public PlayerInteractionListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    /**
     * On till event
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!plugin.isGlobalEnabled())
            return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if(this.plugin.disabledPlayerList.contains(event.getPlayer().getName()))
            return;
        if (!(isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event))) {
            FarmAssistReboot.debug("isHoe: "+isHoe(event.getPlayer().getInventory().getItemInMainHand().getType())+",farmable:"+isPlayerBlockFarmable(event));
            return;
        }
        Player player = event.getPlayer();
        FarmAssistReboot.debug("Wheat: "+config.getEnabled(Material.WHEAT)+"Plant on till: "+config.getPlantOnTill());
        if (Util.isWorldEnabled(event.getPlayer().getWorld())
                && isPlayerBlockFarmable(event)
                && config.getEnabled(Material.WHEAT)
                && config.getPlantOnTill()
                && Util.checkPermission(player, "till")) {
            FarmAssistReboot.debug("initial checks");
            if (Util.inventoryContains(event.getPlayer(), Material.WHEAT)) {
                FarmAssistReboot.debug("planting..");
                // override block type TODO: Better way to implement this
                Block block = event.getClickedBlock();
                block.setType(Material.FARMLAND);
                replant(player, block, Material.WHEAT_SEEDS);
            }
        }
    }

    /**
     * @param player
     * @param block
     * @param material Material to replant
     */
    private void replant(Player player, Block block, Material material) {
        int spot = player.getInventory().first(material);
        ItemStack next;
        if (spot >= 0) {
            next = player.getInventory().getItem(spot);
            if (next.getAmount() > 1) {
                next.setAmount(next.getAmount() - 1);
                player.getInventory().setItem(spot, next);
            } else {
                player.getInventory().setItem(spot, new ItemStack(Material.AIR));
            }

            ReplantTask b = new ReplantTask(this.plugin, block);
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, b, 20L);
        }
    }

    /**
     * Checks if the material is a hoe
     *
     * @param material
     * @return if material is a hoe
     */
    private boolean isHoe(Material material) {
        return material == Material.WOODEN_HOE
                || material == Material.STONE_HOE
                || material == Material.IRON_HOE
                || material == Material.GOLDEN_HOE
                || material == Material.DIAMOND_HOE;
    }



    private boolean isPlayerBlockFarmable(PlayerInteractEvent event) {
        boolean isGrassOrDirt = event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT;
        boolean isTopBlockAir = event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
        return event.hasBlock() && isGrassOrDirt && isTopBlockAir;
    }
}


