package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.Config;
import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.ReplantTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {
    private FarmAssistReboot plugin;

    public BlockBreakListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGlobalEnabled())
            return;
        Block block = event.getBlock();
        Player player = event.getPlayer();
        World world = player.getWorld();
        Material material = block.getType();
        if (!isWorldEnabled(plugin, world)) {
            plugin.logger.fine("!isWorldEnabled");
            if (!this.plugin.disabledPlayerList.contains(event.getPlayer().getName())) {
                plugin.logger.fine("Player isn't in disabled player list");
                if (material == Material.WHEAT &&
                        Config.isWheat()
                        && checkPermission(player, "farmassist.wheat")
                        && player.getInventory().contains(Material.WHEAT_SEEDS)
                        && (!Config.isWheatRipe() || isRipe(block))) {
                    replant(player, block, Material.WHEAT_SEEDS);
                    plugin.logger.fine("Planted wheat at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.SUGAR_CANE
                        && Config.isSugarCane()
                        && checkPermission(player, "farmassist.reeds")
                        && player.getInventory().contains(Material.SUGAR_CANE)) {
                    replant(player, block, Material.SUGAR_CANE);
                    plugin.logger.fine("Planted sugarcane at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.NETHER_WART
                        && Config.isNetherWart()
                        && checkPermission(player, "farmassist.wart")
                        && player.getInventory().contains(Material.NETHER_WART)
                        && (!Config.isNetherWartRipe() || isRipe(block))) {
                    replant(player, block, Material.NETHER_WART);
                    plugin.logger.fine("Planted wart at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.COCOA_BEANS
                        && Config.isCocoaBeans()
                        && checkPermission(player, "farmassist.cocoa")
                        && player.getInventory().contains(Material.COCOA_BEANS)
                        && (!Config.isCocoaRipe() || isRipe(block))) {
                    replant(player, block, Material.COCOA_BEANS);
                    plugin.logger.fine("Planted cocoa at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.CARROTS
                        && Config.isCarrots()
                        && checkPermission(player, "farmassist.carrots")
                        && player.getInventory().contains(Material.CARROT)
                        && (!Config.isCarrotsRipe() || isRipe(block))) {
                    replant(player, block, Material.CARROT);
                    plugin.logger.fine("Planted carrot at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.POTATOES
                        && Config.isPotatoes()
                        && checkPermission(player, "farmassist.potatoes")
                        && player.getInventory().contains(Material.POTATO) &&
                        (!Config.isPotatoesRipe() || isRipe(block))) {
                    replant(player, block, Material.POTATO);
                    plugin.logger.fine("Planted potatoe at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.PUMPKIN_STEM
                        && Config.isPumpkin()
                        && checkPermission(player, "farmassist.pumpkin")
                        && player.getInventory().contains(Material.PUMPKIN_SEEDS)
                        && (!Config.isPumpkinRipe() || isRipe(block))) {
                    replant(player, block, Material.PUMPKIN_SEEDS);
                    plugin.logger.fine("Planted pumpkin at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.MELON_STEM
                        && Config.isMelon()
                        && checkPermission(player, "farmassist.melon")
                        && player.getInventory().contains(Material.MELON_SEEDS)
                        && (!Config.isMelonRipe() || isRipe(block))) {
                    replant(player, block, Material.MELON_SEEDS);
                    plugin.logger.fine("Planted melon at"+block.getLocation().toString());
                    return;
                }

                if (material == Material.BEETROOTS
                        && Config.isBeetroot()
                        && checkPermission(player, "farmassist.beetroot")
                        && player.getInventory().contains(Material.BEETROOT_SEEDS)
                        && (!Config.isBeetrootRipe() || isRipe(block))) {
                    replant(player, block, Material.BEETROOT);
                    plugin.logger.fine("Planted beetroot at"+block.getLocation().toString());
                    return;
                }

            }
        }
    }

    /**
     * @param player
     * @param block
     * @param material Material to replant
     */
    private void replant(@NotNull Player player, Block block, Material material) {
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

    private boolean isWorldEnabled(@NotNull FarmAssistReboot plugin, @NotNull World world) {
        boolean worldEnabled = plugin.getConfig().getBoolean("Worlds.Enable Per World");
        boolean isPlayerWorld = plugin.getConfig().getList("Worlds.Enabled Worlds").contains(world.getName());
        return !worldEnabled || isPlayerWorld;

    }

    private boolean checkPermission(Player player, String permission) {
        return !plugin.getConfig().getBoolean("Main.Use Permissions") || player.hasPermission(permission);
    }

    private boolean isRipe(@NotNull Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
