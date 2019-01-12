package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.ReplantTask;
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

    public PlayerInteractionListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    /**
     * on till
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!plugin.isGlobalEnabled()
                || !(isHoe(event.getPlayer().getInventory().getItemInMainHand().getType()) && isPlayerBlockFarmable(event)))
            return;
        Player player = event.getPlayer();
        if (isWorldEnabled(event)
                && isPlayerBlockFarmable(event)
                && isWheatEnabled()
                && checkPermission(player)) {

                Material material = player.getInventory().getItemInMainHand().getType();
                if (isHoe(material) && player.getInventory().contains(Material.WHEAT_SEEDS)) {
                   replant(player,event.getClickedBlock(),Material.WHEAT_SEEDS);
                }
        }
    }

    /**
     *
     * @param player
     * @param block
     * @param material      Material to replant
     */
    private void replant(Player player, Block block, Material material){
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

    /**
     * Checks if wheat is enabled in the config
     * and if plant on till is enabled
     * @return
     */
    private boolean isWheatEnabled() {
        return plugin.getConfig().getBoolean("Wheat.Enabled") && plugin.getConfig().getBoolean("Wheat.Plant on till");
    }

    private boolean checkPermission(Player player) {
        return !this.plugin.getConfig().getBoolean("Main.Use Permissions") || player.hasPermission("farmassist.till");
    }

    private boolean isPlayerBlockFarmable(PlayerInteractEvent event) {
        boolean isGrassOrDirt = event.getClickedBlock().getType() == Material.GRASS_BLOCK || event.getClickedBlock().getType() == Material.DIRT;
        boolean isRightClick = event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
        boolean isTopBlockAir = event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR;
        return !this.plugin.disabledPlayerList.contains(event.getPlayer().getName())
                && event.hasBlock()
                && isRightClick
                && isGrassOrDirt
                && isTopBlockAir;
    }

    private boolean isWorldEnabled(PlayerInteractEvent event) {
        boolean worldEnabled = plugin.getConfig().getBoolean("Worlds.Enable Per World");
        boolean isPlayerWorld = plugin.getConfig().getList("Worlds.Enabled Worlds").contains(event.getPlayer().getWorld());
        return !worldEnabled || isPlayerWorld;
    }
}
