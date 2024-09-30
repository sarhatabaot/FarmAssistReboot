package com.github.sarhatabaot.farmassistreboot.utils;


import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;

public class NbtUtil {
    public static final String FAR_COMPOUND = "farm-assist-reboot";
    public static final String TILL_CROP = "till-crop";

    private NbtUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getCurrentTillCrop(final ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> {
            if (!nbt.hasTag(FAR_COMPOUND)) {
                return null;
            }

            ReadableNBT farCompound = nbt.getCompound(FAR_COMPOUND);
            if (farCompound == null || !farCompound.hasTag(TILL_CROP)) {
                return null;
            }

            return farCompound.getString(TILL_CROP);
        });
    }


}
