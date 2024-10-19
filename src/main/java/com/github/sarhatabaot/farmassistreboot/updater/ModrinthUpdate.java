package com.github.sarhatabaot.farmassistreboot.updater;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ModrinthUpdate extends UpdateChecker {
    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/356a06e/version";

    public ModrinthUpdate(FarmAssistReboot plugin) {
        super(plugin);
    }

    @Override
    protected String getApiUrl() {
        return MODRINTH_API;
    }

    @Override
    protected JsonElement extractLatestVersion(JsonElement jsonElement) {
        JsonArray versions = jsonElement.getAsJsonArray();
        return versions.get(0).getAsJsonObject().get("version_number");
    }
}
