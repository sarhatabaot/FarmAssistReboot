package com.github.sarhatabaot.farmassistreboot.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.scheduler.BukkitRunnable;

public class ModrinthUpdate extends BukkitRunnable {

    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/<project_id>/version";
    private static final String CURRENT_VERSION = "1.0.0"; // Your current version

    @Override
    public void run() {
        try {
            URL url = new URL(MODRINTH_API);
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

                JsonArray versions = JsonParser.parseString(response.toString()).getAsJsonArray();
                JsonElement latestVersion = versions.get(0).getAsJsonObject().get("version_number");

                if (!CURRENT_VERSION.equals(latestVersion.getAsString())) {
                    System.out.println("Update available! Latest version: " + latestVersion.getAsString());
                } else {
                    System.out.println("No updates available.");
                }
            } else {
                System.out.println("Failed to fetch Modrinth data. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

