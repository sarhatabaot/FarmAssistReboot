package com.github.sarhatabaot.farmassistreboot.crop;


import com.cryptomorin.xseries.XMaterial;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.MainConfig;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import com.github.sarhatabaot.farmassistreboot.deserializer.CropDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.bukkit.Material;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class CropManager {
    private final FarmAssistReboot plugin;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Crop.class, new CropDeserializer())
            .setPrettyPrinting()
            .create();
    private final MainConfig config;

    private final Map<String, Crop> crops;

    private CropCache cropCache;

    private Cache<Crop, String> nameCache;


    public CropManager(final FarmAssistReboot plugin, final MainConfig config) {
        this.plugin = plugin;
        this.crops = new HashMap<>();
        this.config = config;

        load();
    }

    private void load() {
        loadCrops();
        loadCache();

        plugin.getLogger().info(() -> "Loaded " + crops.size() + " crops");
    }

    // Fallthrough is on purpose. If the version is 1.20, we also want to execute the code from 1.8. If anyone has a better idea, feel free to submit a PR.
    private void loadCrops() {
        final String jsonCropVersion = Util.getJsonCropVersionFromMinecraftVersion();
        switch (jsonCropVersion) {
            case "1.20": {
                this.crops.putAll(readFileFromJar("1.20.json"));
                // load 1.0, 1.8, 1.20
            }
            case "1.14": {
                this.crops.putAll(readFileFromJar("1.14.json"));

            }
            case "1.9": {
                this.crops.putAll(readFileFromJar("1.9.json"));
                // load 1.0, 1.9
            }
            case "1.0": {
                this.crops.putAll(readFileFromJar("1.0.json"));
                // load 1.0
                break;
            }

            default: {
                plugin.getLogger().warning(() -> "Unknown/Unspported Minecraft version: " + jsonCropVersion);
            }
        }
    }

    private void loadCache() {
        this.cropCache = new CropCache(plugin, config.getMaxCacheSize(), crops);
        this.nameCache = Caffeine.newBuilder()
                .maximumSize(config.getMaxCacheSize())
                .build();

        for (Map.Entry<String, Crop> entry: crops.entrySet()) {
            this.nameCache.put(entry.getValue(),entry.getKey());
        }
    }

    //For later todo
    private List<Crop> readCustomFile(final String filename) {
        try (JsonReader reader = new JsonReader(new FileReader("versions" + File.separator + filename))) {
            return gson.fromJson(reader, Crop.class);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Map<String, Crop> readFileFromJar(final String filename) {
        // Load the JSON file as an InputStream from the JAR
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("versions/" + filename);

        if (inputStream == null) {
            // Return an empty list if the file could not be found
            return Collections.emptyMap();
        }

        // Use JsonReader with InputStreamReader
        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
            // Parse the JSON into a Map of Crop objects
            return gson.fromJson(reader, new TypeToken<Map<String, Crop>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Crop getCropFromItem(final Material cropMaterial) {
        return cropCache.getCropByItem(XMaterial.matchXMaterial(cropMaterial).parseMaterial());
    }

    public Crop getCropFromItem(final XMaterial cropMaterial) {
        return cropCache.getCropByItem(cropMaterial.parseMaterial());
    }

    public Collection<Crop> getCrops() {
        return crops.values();
    }

    public String getCropName(final Crop crop) {
        return nameCache.getIfPresent(crop);
    }

    public boolean isNotSupportedCrop(final Material material) {
        return !cropCache.hasCropItem(XMaterial.matchXMaterial(material).parseMaterial());
    }

    public boolean isNotSupportedCrop(final XMaterial material) {
        return !cropCache.hasCropItem(material.parseMaterial());
    }
}
