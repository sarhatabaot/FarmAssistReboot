package com.github.sarhatabaot.farmassistreboot.crop;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CropCache {
    private int maxCacheSize;
    private final Cache<Material, Crop> cache;

    public CropCache(final int maxCacheSize, final List<Crop> crops) {
        // Create the cache without any expiration policies or size limits
        this.maxCacheSize = maxCacheSize;
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxCacheSize)
                .build();  // no expiration
        loadCrops(crops);
    }

    public void loadCrops(@NotNull List<Crop> crops) {
        for (Crop crop : crops) {
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

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }
}
