package io.github.sarhatabaot.farmassistreboot.listeners;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import io.github.sarhatabaot.farmassistreboot.Util;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import io.github.sarhatabaot.farmassistreboot.config.FarmAssistCrops;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static io.github.sarhatabaot.farmassistreboot.Util.*;

public class BlockBreakListener implements Listener {
    private FarmAssistReboot plugin;
    private FarmAssistConfig config;

    public BlockBreakListener(FarmAssistReboot plugin) {
        config = FarmAssistConfig.getInstance();
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGlobalEnabled())
            return;
        if (this.plugin.getDisabledPlayerList().contains(event.getPlayer().getName()))
            return;
        if (config.isPermissionEnabled() && !hasMaterialPermission(event)){
            String playerName = "Player: "+event.getPlayer().getDisplayName();
            String permission = "farmassist."+getMaterialFromCrops(event.getBlock().getType()).name();
            FarmAssistReboot.debug(playerName+", "+"doesn't have permission "+"\u001b[36m"+permission+"\u001b[0m");
            return;
        }
        if (Util.isWorldEnabled(event.getPlayer().getWorld())) {
            FarmAssistReboot.debug("Is"+event.getPlayer().getWorld().getName()+" enabled: " + Util.isWorldEnabled(event.getPlayer().getWorld()));
            applyReplant(event);
        }
    }

    private boolean hasMaterialPermission(BlockBreakEvent event){
        Material material = event.getBlock().getType();
        material = getMaterialFromCrops(material);
        return event.getPlayer().hasPermission("farmassist."+material.name());
    }


    private void applyReplant(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        FarmAssistReboot.debug("Block broken: "+material.name());

        if (!FarmAssistCrops.getCropList().contains(material)) {
            FarmAssistReboot.debug("Crop List doesn't contain: " + material.name());
            return;
        }

        FarmAssistReboot.debug("Crop List contains: " + material.name());

        if (config.getEnabled(getMaterialFromCrops(material))) {
            if (!Util.inventoryContains(event.getPlayer(), material)) {
                FarmAssistReboot.debug("Player doesn't have the correct seeds/material to replant");
                return;
            }
            if (material == Material.SUGAR_CANE || material == Material.CACTUS) {
                replant(event.getPlayer(), event.getBlock(), material);
                return;
            }
            if (!config.getRipe(material) || isRipe(event.getBlock())) {
                replant(event.getPlayer(), event.getBlock(), material);
                return;
            }
        }
        FarmAssistReboot.debug("Fallthrough, shouldn't even get here.");
    }



    private boolean isRipe(Block block) {
        Ageable age = (Ageable) block.getBlockData();
        return (age.getAge() == age.getMaximumAge());
    }
}
