package com.github.sarhatabaot.farmassist.listeners;

import com.github.sarhatabaot.farmassist.FarmAssist;
import com.github.sarhatabaot.farmassist.Util;
import com.github.sarhatabaot.farmassist.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassist.config.FarmAssistCrops;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private FarmAssist plugin;
    private FarmAssistConfig config;

    public BlockBreakListener(FarmAssist plugin) {
        config = FarmAssistConfig.getInstance();
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGlobalEnabled() || this.plugin.getDisabledPlayerList().contains(event.getPlayer().getName()))
            return;
        if (!checkPermissions(event)) return;
        if (Util.isWorldEnabled(event.getPlayer().getWorld())) {
            FarmAssist.debug("Is"+event.getPlayer().getWorld().getName()+" enabled: " + Util.isWorldEnabled(event.getPlayer().getWorld()));
            applyReplant(event);
        }
    }

    private boolean checkPermissions(BlockBreakEvent event){
        if (config.isPermissionEnabled() && !hasMaterialPermission(event)){
            String playerName = "Player: "+event.getPlayer().getDisplayName();
            String permission = "farmassist."+ Util.getMaterialFromCrops(event.getBlock().getType()).name();
            FarmAssist.debug(playerName+", "+"doesn't have permission "+"\u001b[36m"+permission+"\u001b[0m");
            return false;
        }
        return true;
    }

    private boolean hasMaterialPermission(BlockBreakEvent event){
        Material material = event.getBlock().getType();
        material = Util.getMaterialFromCrops(material);
        return event.getPlayer().hasPermission("farmassist."+material.name());
    }

    private void applyReplant(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        FarmAssist.debug("Block broken: "+material.name());

        if (!FarmAssistCrops.getCropList().contains(material)) {
            FarmAssist.debug("Crop List doesn't contain: " + material.name());
            return;
        }

        FarmAssist.debug("Crop List contains: " + material.name());

        if (config.getEnabled(Util.getMaterialFromCrops(material))) {
            if (!Util.inventoryContains(event.getPlayer(), material)) {
                FarmAssist.debug("Player doesn't have the correct seeds/material to replant");
                return;
            }
            if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
                Util.replant(event.getPlayer(), event.getBlock(), material);
                return;
            }
            if (!config.getRipe(material) || isRipe(event.getBlock())) {
                Util.replant(event.getPlayer(), event.getBlock(), material);
                return;
            }
        }
        FarmAssist.debug("Fallthrough, shouldn't even get here.");
    }

    private boolean isRipe(Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
