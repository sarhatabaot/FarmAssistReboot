package com.github.sarhatabaot.farmassistreboot.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import com.github.sarhatabaot.farmassistreboot.utils.NbtUtil;
import com.github.sarhatabaot.farmassistreboot.utils.ReplantUtil;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TillListener implements Listener {
    private final FarmAssistReboot plugin;
    private final CropManager cropManager;
    private final MainConfig mainConfig;


    public TillListener(FarmAssistReboot plugin, CropManager cropManager, MainConfig mainConfig) {
        this.plugin = plugin;
        this.cropManager = cropManager;
        this.mainConfig = mainConfig;
    }

    @EventHandler
    public void onPlayerInteract(final @NotNull InventoryClickEvent event) {
        if (checkForNotHoeItem(event.getWhoClicked().getItemOnCursor())) {
            plugin.trace("Item: " + event.getWhoClicked().getItemOnCursor());
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            plugin.trace("Not a player.");
            return;
        }

        final ItemStack handItem = event.getCurrentItem(); // the item the player is "holding"
        if (this.cropManager.isNotSupportedCrop(handItem.getType())) {
            plugin.trace("Crop not supported or is not a crop: " + handItem.getType());
            return;
        }

        final ItemStack clickedItem = event.getCursor(); // The item/block clicked, the hoe item

        NBT.modify(clickedItem, nbt -> {
            final Crop crop = this.cropManager.getCropFromItem(handItem.getType());
            final String cropName = this.cropManager.getCropName(crop);
            nbt.getOrCreateCompound(NbtUtil.FAR_COMPOUND).setString(NbtUtil.TILL_CROP, cropName);
            plugin.trace("Added nbt to hoe itemstack.");
            nbt.modifyMeta((readableNBT, itemMeta) -> {
                        List<String> lore = itemMeta.getLore();
                        if (lore == null) {
                            lore = new ArrayList<>();
                        }
                        lore.addAll(fillEmptyLoreLines(lore));
                        if (lore.size() == mainConfig.getLoreModifyPosition()) {
                            lore.add("Tilling Crop: " + cropName);
                        } else {
                            lore.set(mainConfig.getLoreModifyPosition(), "Tilling Crop: " + cropName);
                        }

                        itemMeta.setLore(lore);

                        plugin.trace("Set lore on hoe itemstack.");
                    }
            );

            plugin.debug("Modified hoe with crop: " + cropName);
        });
    }

    @Contract("_ -> param1")
    private @NotNull List<String> fillEmptyLoreLines(@NotNull List<String> lore) {
        if (lore.size() < mainConfig.getLoreModifyPosition()) {
            for (int i = 0; i < mainConfig.getLoreModifyPosition(); i++) {
                lore.add(" ");
            }
        }
        return lore;
    }

    @EventHandler
    public void onPlayerTill(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || checkForNotHoeItem(event.getItem())) {
            return;
        }

        final ItemStack hoe = event.getItem();
        final String tillCrop = NbtUtil.getCurrentTillCrop(hoe);
        if (tillCrop == null) {
            plugin.trace("Could not find till crop.");
            return;
        }

        Optional<XMaterial> potentialTillCrop = XMaterial.matchXMaterial(tillCrop);
        if (!potentialTillCrop.isPresent()) {
            plugin.trace("Could not match material to till crop: " + tillCrop);
            return;
        }


        ReplantUtil.replant(event.getPlayer(), this.cropManager.getCropFromItem(potentialTillCrop.get()), event.getClickedBlock().getLocation());
    }

    private boolean checkForNotHoeItem(final @NotNull ItemStack itemStack) {
        return !itemStack.getType().name().contains("HOE");
    }
}
