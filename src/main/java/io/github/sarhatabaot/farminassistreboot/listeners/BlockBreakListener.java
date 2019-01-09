package io.github.sarhatabaot.farminassistreboot.listeners;

import io.github.sarhatabaot.farminassistreboot.FarmAssist;
import io.github.sarhatabaot.farminassistreboot.ReplantTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {
    private FarmAssist plugin;

    public BlockBreakListener(FarmAssist plugin) {
        this.plugin = plugin;
    }

    @EventHandler (
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        if(!plugin.isGlobalEnabled())
            return;
        Block block = event.getBlock();
        Player player = event.getPlayer();
        World world = player.getWorld();
        Material material = block.getType();
        if (!isWorldEnabled(plugin, world)) {
            // ?
            if (!this.plugin.disabledPlayerList.contains(event.getPlayer().getName())) {
                if (material == Material.WHEAT &&
                        this.plugin.getConfig().getBoolean("Wheat.Enabled")
                        && checkPermission(player,"farmassist.wheat")
                        && player.getInventory().contains(Material.WHEAT_SEEDS)
                        && (!this.plugin.getConfig().getBoolean("Wheat.Only Replant When Fully Grown") || isRipe(block))) {
                   replant(player,block,Material.WHEAT_SEEDS);
                }

                if (material == Material.SUGAR_CANE
                        && this.plugin.getConfig().getBoolean("Reeds.Enabled")
                        && checkPermission(player,"farmassist.reeds")
                        && player.getInventory().contains(Material.SUGAR_CANE)) {
                    replant(player,block,Material.SUGAR_CANE);
                }

                if (material == Material.NETHER_WART
                        && this.plugin.getConfig().getBoolean("Netherwart.Enabled")
                        && checkPermission(player,"farmassist.wart")
                        && player.getInventory().contains(Material.NETHER_WART)
                        && (!this.plugin.getConfig().getBoolean("Netherwart.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.NETHER_WART);
                }

                if (material == Material.COCOA_BEANS
                        && this.plugin.getConfig().getBoolean("Cocoa Beans.Enabled")
                        && checkPermission(player,"farmassist.cocoa")
                        && player.getInventory().contains(Material.COCOA_BEANS)
                        && (!this.plugin.getConfig().getBoolean("Cocoa Beans.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.COCOA_BEANS);
                }

                if (material == Material.CARROTS
                        && this.plugin.getConfig().getBoolean("Carrots.Enabled")
                        && checkPermission(player,"farmassist.carrots")
                        && player.getInventory().contains(Material.CARROT)
                        && (!this.plugin.getConfig().getBoolean("Carrots.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.CARROT);
                }

                if (material == Material.POTATOES
                        && this.plugin.getConfig().getBoolean("Potatoes.Enabled")
                        && checkPermission(player,"farmassist.potatoes")
                        && player.getInventory().contains(Material.POTATO) &&
                        (!this.plugin.getConfig().getBoolean("Potatoes.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.POTATO);
                }

                if (material == Material.PUMPKIN_STEM
                        && this.plugin.getConfig().getBoolean("Pumpkin.Enabled")
                        && checkPermission(player,"farmassist.pumpkin")
                        && player.getInventory().contains(Material.PUMPKIN_SEEDS)
                        && (!this.plugin.getConfig().getBoolean("Pumpkin.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.PUMPKIN_SEEDS);
                }

                if (material == Material.MELON_STEM
                        && this.plugin.getConfig().getBoolean("Melon.Enabled")
                        && checkPermission(player,"farmassist.melon")
                        && player.getInventory().contains(Material.MELON_SEEDS)
                        && (!this.plugin.getConfig().getBoolean("Melon.Only Replant When Fully Grown") || isRipe(block))) {
                    replant(player,block,Material.MELON_SEEDS);
                }

                if(material == Material.BEETROOTS
                        &&this.plugin.getConfig().getBoolean("Beetroot.Enabled")
                        &&checkPermission(player,"farmassist.beetroot")
                        && player.getInventory().contains(Material.BEETROOT_SEEDS)
                        &&(!this.plugin.getConfig().getBoolean("Beetroot.Only Replant When Fully Grown") || isRipe(block))){
                    replant(player,block,Material.BEETROOT);
                }

            }
        }
    }

    /**
     *
     * @param player
     * @param block
     * @param material      Material to replant
     */
    private void replant(@NotNull Player player, Block block, Material material){
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

            ReplantTask b = new ReplantTask(this.plugin, block, material);
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, b, 20L);
        }
    }

    private boolean isWorldEnabled(@NotNull FarmAssist plugin, @NotNull World world) {
        boolean worldEnabled = plugin.getConfig().getBoolean("Worlds.Enable Per World");
        boolean isPlayerWorld = plugin.getConfig().getList("Worlds.Enabled Worlds").contains(world.getName());
        return !worldEnabled || isPlayerWorld;

    }

    private boolean checkPermission(Player player,String permission){
        return !plugin.getConfig().getBoolean("Main.Use Permissions") || player.hasPermission(permission);
    }

    private boolean isRipe(@NotNull Block block){
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
