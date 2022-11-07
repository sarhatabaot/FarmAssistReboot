package com.github.sarhatabaot.farmassistreboot.messages;

public final class Debug {
    public static class Worlds {
        public static final String IS_WORLD_ENABLED = "Is %s enabled: %s";
        public static final String CONFIG_PER_WORLD = "config.enabled-per-world: %s";

        private Worlds() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class OnBlockBreak {
        public static final String CROP_LIST_NO_MATERIAL = "Crop List doesn't contain: %s";
        public static final String PLAYER_NO_PERMISSION = "Player: %s, doesn't have permission %s";
        public static final String BLOCK_BROKEN = "Block broken: %s";
        public static final String CROP_LIST_CONTAINS = "Crop List contains: %s";
        public static final String MATERIAL_DISABLED = "Material: %s is disabled";
        public static final String NO_SEEDS = "Player: %s, doesn't have the correct seeds/material to replant";

        private OnBlockBreak() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class OnPlayerInteract {
        public static final String BLOCK_FARMABLE = "Is block farmable: %s";
        public static final String IS_ITEM_HOE = "Is item hoe: %s";
        public static final String CONFIG_WHEAT = "config.wheat: %s";
        public static final String CONFIG_PLANT_ON_TILL = "config.plant-on-till: %s";
        public static final String WORLD_DISABLED = "World: %s disabled";
        public static final String WHEAT_ENABLED = "Wheat enabled: %s";
        public static final String TILL_ENABLED = "Till enabled: %s";

        private OnPlayerInteract() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static class ReplantTask {
        public static final String RUN = "Block: %s, Material: %s";

        private ReplantTask() {
            throw new UnsupportedOperationException(Debug.UTIL_CLASS);
        }
    }

    public static final String UTIL_CLASS = "Util class.";

    private Debug() {
        throw new UnsupportedOperationException(Debug.UTIL_CLASS);
    }
}