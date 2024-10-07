package com.github.sarhatabaot.farmassistreboot.utils;


import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.crop.CropManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ReplantUtil {
    private static FarmAssistReboot plugin;
    private static CropManager cropManager;

    private ReplantUtil() {
        throw new UnsupportedOperationException();
    }

    public static void init(final FarmAssistReboot plugin, final CropManager cropManager) {
        ReplantUtil.plugin = plugin;
        ReplantUtil.cropManager = cropManager;
    }

    public static void replant(final Player player, final Crop crop, final Location location) {
        //Run checks
        replant(crop, location);
    }

    public static void replant(final Crop crop, final Location location) {

    }



}
