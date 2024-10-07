package com.github.sarhatabaot.farmassistreboot.updater;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;

public class GithubUpdate extends BukkitRunnable {
    private final FarmAssistReboot plugin;

    public GithubUpdate(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    private static final String GITHUB_API = "https://api.github.com/repos/<owner>/<repo>/releases/latest";

    @Override
    public void run() {
        try {
            URL url = new URL(GITHUB_API);
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

                JsonObject release = JsonParser.parseString(response.toString()).getAsJsonObject();
                String latestVersion = release.get("tag_name").getAsString();

                if (!plugin.getDescription().getVersion().equals(latestVersion)) {
                    System.out.println("Update available! Latest version: " + latestVersion);
                } else {
                    System.out.println("No updates available.");
                }
            } else {
                System.out.println("Failed to fetch GitHub data. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

