package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.Config;
import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.ReplantTask;
import io.github.sarhatabaot.farmassistreboot.Util;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistCrops;
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
    private FarmAssistReboot plugin;
    private FarmAssistConfig config;

    public BlockBreakListener(FarmAssistReboot plugin) {
        config = FarmAssistConfig.getInstance();
        this.plugin = plugin;
    }

    @EventHandler (
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGlobalEnabled())
            return;
        if(this.plugin.disabledPlayerList.contains(event.getPlayer().getName()))
            return;
        if (isWorldEnabled(event.getPlayer().getWorld())) {
            applyReplant(event);
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

            ReplantTask b = new ReplantTask(this.plugin, block);
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, b, 20L);
        }
    }

    private boolean applyReplant(BlockBreakEvent event){
        Material material = event.getBlock().getType();
        if(!FarmAssistCrops.getCropList().contains(material))
            return false;
        if(config.getEnabled(material) && checkPermission(event.getPlayer(),material.name().toLowerCase())){
            if(!Util.inventoryContains(event.getPlayer(),event.getBlock().getType()))
                return false;
            // case sugar cane
            if(material == Material.SUGAR_CANE){
                replant(event.getPlayer(),event.getBlock(),material);
                return true;
            }
            // case ripe
            if(!config.getRipe(material) || isRipe(event.getBlock())){
                replant(event.getPlayer(), event.getBlock(), material);
                return true;
                // case wheat
                // config.getPlantOnTill should be in playerinteractionlistener
                /*
                if(material == Material.WHEAT && config.getPlantOnTill()) {
                    replant(event.getPlayer(), event.getBlock(), material);
                    return true;
                } */
            }
        }
        return false;
    }

    /*
    private boolean applyReplant(BlockBreakEvent event){
        Block block = event.getBlock();
        Material material = block.getType();
        Player player = event.getPlayer();

        switch (material){
            case WHEAT: {

                if(!(Config.isWheat()
                        && (!Config.isWheatRipe() || isRipe(block))
                        && checkPermission(player,"wheat")
                        && player.getInventory().contains(Material.WHEAT_SEEDS)))
                    return false;
                replant(player,block,Material.WHEAT_SEEDS);
                return true;
            }
            case SUGAR_CANE: {
                if(!(Config.isSugarCane()
                        && checkPermission(player,"reeds")
                        && player.getInventory().contains(Material.SUGAR_CANE)))
                    return false;
                replant(player,block,material);
                return true;
            }
            case NETHER_WART:{
                if(!(Config.isNetherWart()
                        && (!Config.isNetherWartRipe() || isRipe(block))
                        && checkPermission(player, "wart")
                        && player.getInventory().contains(Material.NETHER_WART)))
                    return false;
                replant(player,block,material);
                return true;
            }
            case COCOA_BEANS:{
                if(!(Config.isCocoaBeans()
                        && (!Config.isCocoaRipe() || isRipe(block))
                        && checkPermission(player,"cocoa")
                        && player.getInventory().contains(Material.COCOA_BEANS)))
                    return false;
                replant(player,block,material);
                return true;
            }
            case CARROTS: {
                if(!(Config.isCarrots()
                        && (!Config.isCarrotsRipe() || isRipe(block))
                        && checkPermission(player,"carrots")
                        && player.getInventory().contains(Material.CARROT)))
                    return false;
                replant(player,block,Material.CARROT);
                return true;
            }
            case POTATOES:{
                if(!(Config.isPotatoes()
                        && (!Config.isPotatoesRipe() || isRipe(block))
                        && checkPermission(player,"potatoes")
                        && player.getInventory().contains(Material.POTATO)))
                    return false;
                replant(player,block,Material.POTATO);
                return true;
            }
            case BEETROOTS:{
                if(!(Config.isBeetroot()
                        && (!Config.isBeetrootRipe() || isRipe(block))
                        && checkPermission(player,"beetroot")
                        && player.getInventory().contains(Material.BEETROOT_SEEDS)))
                    return false;
                replant(player,block,Material.BEETROOT_SEEDS);
                return true;
            }
            case MELON_STEM:
            case ATTACHED_MELON_STEM: {
                if(!(Config.isMelon()
                        && (!Config.isMelonRipe() || isRipe(block))
                        && checkPermission(player,"melon")
                        && player.getInventory().contains(Material.MELON_SEEDS)))
                    return false;
                replant(player,block,Material.MELON_SEEDS);
                return true;
            }
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM: {
                if(!(Config.isPumpkin()
                        && (!Config.isPumpkinRipe() || isRipe(block))
                        && checkPermission(player,"pumpkin")
                        && player.getInventory().contains(Material.PUMPKIN_SEEDS)))
                    return false;
                replant(player,block,Material.PUMPKIN_SEEDS);
                return true;
            }
        }
        return false;
    }*/

    private boolean isWorldEnabled(World world) {
        return !config.getWorldEnabled() || config.getWorlds().contains(world);

    }

    private boolean checkPermission(Player player, String permission) {
        return !config.getPermission() || player.hasPermission("farmassist."+permission);
    }

    private boolean isRipe(@NotNull Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
