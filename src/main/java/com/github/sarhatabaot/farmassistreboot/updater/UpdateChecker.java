package com.github.sarhatabaot.farmassistreboot.updater;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class UpdateChecker extends BukkitRunnable {
    protected final FarmAssistReboot plugin;

    protected UpdateChecker(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    protected abstract String getApiUrl();
    protected abstract JsonElement extractLatestVersion(JsonElement jsonElement);

    @Override
    public void run() {
        try {
            URL url = new URL(getApiUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonElement jsonElement = JsonParser.parseString(response.toString());
                String latestVersion = extractLatestVersion(jsonElement).getAsString();

                if (!plugin.getDescription().getVersion().equals(latestVersion)) {
                    plugin.getLogger().info(() -> "Update available! Latest version: " + latestVersion);
                } else {
                    plugin.getLogger().info(() -> "No updates available.");
                }
            } else {
                plugin.getLogger().severe(() -> "Failed to fetch update data. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

