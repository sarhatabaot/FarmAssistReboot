package com.github.sarhatabaot.farmassistreboot.utils;


import java.util.ResourceBundle;

public class DebugUtil {
    private DebugUtil() {
        throw new UnsupportedOperationException();
    }
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("debug");

    public static final String CROP_DISABLED = BUNDLE.getString("cropDisabled");
    public static final String BLOCK_NOT_SUPPORTED = BUNDLE.getString("blockNotSupported");
    public static final String CROP_NOT_FULLY_GROWN = BUNDLE.getString("cropNotFullyGrown");
    public static final String CROP_CURRENT_AGE_AND_FULLY_GROWN = BUNDLE.getString("cropCurrentAgeAndFullyGrown");
    public static final String CROP_REPLANTING_FROM =  BUNDLE.getString("cropReplantingFrom");
    public static final String WORLD_NOT_ENABLED = BUNDLE.getString("worldNotEnabled");
    public static final String PLAYER_NO_PERMISSION = BUNDLE.getString("playerNoPermission");
}
