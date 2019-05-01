package io.github.sarhatabaot.farmassistreboot.tasks;

import com.google.gson.JsonObject;
import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author sarhatabaot
 */
public class SimpleVersionChecker implements Runnable {
    private FarmAssistReboot plugin;
    private String versionNumber;

    private final static String LATEST = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";

    public SimpleVersionChecker(FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.versionNumber = plugin.getDescription().getVersion();
    }

    @Override
    public void run() {
        JsonParser parser = new JsonParser();
        try {
            URL url = new URL(LATEST);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Object obj = parser.parse(in);
            JsonObject jsonObject = (JsonObject) obj;
            String remoteVer = jsonObject.get("tag_name").getAsString();
            remoteVer = remoteVer.replace("v","");
            int remoteVal = Integer.parseInt(remoteVer.replace(".", ""));
            int localVer = Integer.parseInt(versionNumber.replace(".", ""));
            if (remoteVal > localVer) {
                plugin.setNeedsUpdate(true);
                plugin.setNewVersion(remoteVer);
                plugin.getLogger().info("New update: " + remoteVer + " Current version: " + versionNumber);
            } else {
                plugin.setNeedsUpdate(false);
                plugin.getLogger().info("You are running the latest version: " + versionNumber);
            }
        } catch (Exception e){
            plugin.getLogger().info("Could not get new version.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

}
