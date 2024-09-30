package com.github.sarhatabaot.farmassistreboot.crop;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Logger;

public class CropCache {
    private final Cache<Material, Crop> cache;
    private final FarmAssistReboot plugin;

    public CropCache(final FarmAssistReboot plugin, final int maxCacheSize,final Map<String, Crop> crops) {
        this.plugin = plugin;
        if (crops == null) {
            throw new IllegalArgumentException("Crops map cannot be null.");
        }
        // Create the cache without any expiration policies or size limits
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxCacheSize)
                .build();  // no expiration
        loadCrops(crops);
    }

    public void loadCrops(@NotNull Map<String, Crop> crops) {
        for (Crop crop : crops.values()) {
            plugin.debug("Loaded crop: " + crop);
            cache.put(crop.getCropItem(), crop);
        }
    }

    public Crop getCropByItem(Material cropItem) {
        return cache.getIfPresent(cropItem);
    }

    public void addCrop(Crop crop) {
        cache.put(crop.getCropItem(), crop);
    }

    public void removeCrop(Material cropItem) {
        cache.invalidate(cropItem);
    }

    public void clearCache() {
        // Invalidate all entries in the cache (clearing the cache)
        cache.invalidateAll();
    }

    public boolean hasCropItem(Material cropItem) {
        return cache.getIfPresent(cropItem) != null;
    }

    public long size() {
        return this.cache.estimatedSize();
    }
}
