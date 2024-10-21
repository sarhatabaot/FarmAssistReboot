package com.github.sarhatabaot.farmassistreboot.updater;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GithubUpdate extends UpdateChecker {
    private static final String GITHUB_API = "https://api.github.com/repos/<owner>/<repo>/releases/latest";

    public GithubUpdate(FarmAssistReboot plugin) {
        super(plugin);
    }

    @Override
    protected String getApiUrl() {
        return GITHUB_API;
    }

    @Override
    protected JsonElement extractLatestVersion(JsonElement jsonElement) {
        JsonObject release = jsonElement.getAsJsonObject();
        return release.get("tag_name");
    }
}
