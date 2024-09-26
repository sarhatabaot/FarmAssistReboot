package com.github.sarhatabaot.farmassistreboot.crop;


import com.github.sarhatabaot.farmassistreboot.MainConfig;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import com.github.sarhatabaot.farmassistreboot.deserializer.CropDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.bukkit.Material;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CropManager {
    private final Logger logger = Logger.getLogger("FarmAssistReboot");
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Crop.class, new CropDeserializer())
            .setPrettyPrinting()
            .create();
    private final MainConfig config;

    private final List<Crop> crops;

    private CropCache cropCache;


    public CropManager(final MainConfig config) {
        this.crops = new ArrayList<>();
        this.config = config;
    }

    public void load() {
        loadCrops();
        loadCache();

        logger.info(() -> "Loaded " + crops.size() + " crops");
    }

    // Fallthrough is on purpose. If the version is 1.20, we also want to execute the code from 1.8. If anyone has a better idea, feel free to submit a PR.
    private void loadCrops() {
        final String jsonCropVersion = Util.getJsonCropVersionFromMinecraftVersion();
        switch (jsonCropVersion) {
            case "1.20": {
                this.crops.addAll(readFile("1.20.json"));
                // load 1.0, 1.8, 1.20
            }
            case "1.8": {
                this.crops.addAll(readFile("1.8.json"));
                // load 1.0, 1.8
            }
            case "1.0": {
                this.crops.addAll(readFile("1.0.json"));
                // load 1.0
                break;
            }

            default: {
                logger.warning(() -> "Unknown/Unspported Minecraft version: " + jsonCropVersion);
            }
        }
    }

    private void loadCache() {
        this.cropCache = new CropCache(config.getMaxCacheSize(), crops);
    }

    private List<Crop> readFile(final String filename) {
        try (JsonReader reader = new JsonReader(new FileReader(filename))) {
            return gson.fromJson(reader, Crop.class);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public Crop getCropFromItem(final Material cropMaterial) {
        return cropCache.getCropByItem(cropMaterial);
    }

    public List<Crop> getCrops() {
        return crops;
    }

    public boolean isNotSupportedCrop(final Material material) {
        return !cropCache.hasCropItem(material);
    }
}
