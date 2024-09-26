package com.github.sarhatabaot.farmassistreboot.deserializer;


import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.google.gson.*;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CropDeserializer implements JsonDeserializer<Crop> {
    @Override
    public Crop deserialize(@NotNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final Material cropMaterial = XMaterial.matchXMaterial(jsonObject.get("crop").getAsString()).orElse(XMaterial.AIR).parseMaterial();
        final Material seedMaterial = XMaterial.matchXMaterial(jsonObject.get("seed").getAsString()).orElse(XMaterial.AIR).parseMaterial();
        final Material[] plantedOn = getPlantedOn(jsonObject);

        if (cropMaterial == Material.AIR || seedMaterial == Material.AIR || plantedOn.length == 0) {
            //Log and throw an error or something
            return null;
        }
        return new Crop(cropMaterial,seedMaterial,plantedOn);
    }

    private Material @NotNull [] getPlantedOn(@NotNull JsonObject jsonObject) {
        final List<Material> plantedOn = new ArrayList<>();
        for (JsonElement element : jsonObject.get("plantedOn").getAsJsonArray()) {
            plantedOn.add(XMaterial.matchXMaterial(element.getAsString()).orElse(XMaterial.AIR).parseMaterial());
        }

        return plantedOn.stream().filter(m -> m != Material.AIR).toArray(Material[]::new);
    }
}
