package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.NbtUtil;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;


public class TillListener implements Listener {
    private final FarmAssistReboot plugin;
    private final CropManager cropManager;

    public TillListener(FarmAssistReboot plugin, CropManager cropManager) {
        this.plugin = plugin;
        this.cropManager = cropManager;
    }


    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (checkForNotRightClickAndHoeItem(event)) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack handItem = player.getInventory().getItemInHand(); // The item in player's hand
        if (this.cropManager.isNotSupportedCrop(handItem.getType())) {
            plugin.trace("Crop not supported or is not a crop: " + handItem.getType());
            return;
        }

        final ItemStack clickedItem = event.getItem(); // The item/block clicked

        NBT.modify(clickedItem, nbt -> {
            final Crop crop = this.cropManager.getCropFromItem(handItem.getType());
            nbt.getOrCreateCompound(NbtUtil.FAR_COMPOUND).setString(NbtUtil.TILL_CROP, crop.getName());
            nbt.modifyMeta((readableNBT, itemMeta) -> itemMeta.setLore(
                    Collections.singletonList(itemMeta.getLore().set(1, "Tilling Crop: " + crop.getName()))
            ));
        });
    }

    @EventHandler
    public void onPlayerTill(PlayerInteractEvent event) {
        if (checkForNotRightClickAndHoeItem(event)) {
            return;
        }

        final ItemStack hoe = event.getItem();
        final String tillCrop = NbtUtil.getCurrentTillCrop(hoe);
        if (tillCrop == null) {
            return;
        }

        XMaterial.matchXMaterial(tillCrop).ifPresent(cropMaterial -> {
            ReplantUtil.replant(event.getPlayer(), this.cropManager.getCropFromItem(cropMaterial), event.getClickedBlock().getLocation());
        });
    }

    private boolean checkForNotRightClickAndHoeItem(final @NotNull PlayerInteractEvent event) {
        return event.getAction() != Action.RIGHT_CLICK_BLOCK || !event.getItem().getType().name().contains("HOE");
    }
}
