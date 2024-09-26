package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.NbtUtil;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public class TillListener implements Listener {
    private final CropManager cropManager;

    public TillListener(CropManager cropManager) {
        this.cropManager = cropManager;
    }

    //Apply Till Crop
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        // Player right clicks on another item with a crop item.
        // Check if the item is a hoe
        // False, do nothing
        // True
        // Apply nbt data to hoe
        // Add "Tilling Crop: <type>" to lore
    }

    @EventHandler
    public void onPlayerTill(PlayerInteractEvent event) {
        if (!checkForRightClickAndHoeItem(event)) {
            return;
        }

        final ItemStack hoe = event.getItem();
        final String tillCrop = NbtUtil.getCurrentTillCrop(hoe);
        if (tillCrop == null) {
            return;
        }

        final Material cropMaterial = Material.matchMaterial(tillCrop);
        ReplantUtil.replant(event.getPlayer(), this.cropManager.getCropFromItem(cropMaterial), event.getClickedBlock().getLocation());
    }

    private boolean checkForRightClickAndHoeItem(final @NotNull PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem().getType().name().contains("HOE");
    }
}
