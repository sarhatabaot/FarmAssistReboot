package io.github.sarhatabaot.farmassistreboot.tasks;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author sarhatabaot
 */
public class SimpleUpdateCheckerTask implements Runnable {
    private FarmAssistReboot plugin;
    private String versionNumber;

    private final String latest = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";

    public SimpleUpdateCheckerTask(FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.versionNumber = plugin.getDescription().getVersion();
    }

    @Override
    public void run() {
        JSONParser parser = new JSONParser();
        try {
            URL url = new URL(latest);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Object obj = parser.parse(in);
            JSONObject jsonObject = (JSONObject) obj;
            String remoteVer = (String) jsonObject.get("tag_name");
            remoteVer = remoteVer.replace("v","");
            int remoteVal = Integer.valueOf(remoteVer.replace(".", ""));
            int localVer = Integer.valueOf(versionNumber.replace(".", ""));
            if (remoteVal > localVer) {
                plugin.setNeedsUpdate(true);
                plugin.setNewVersion(remoteVer);
                plugin.getLogger().info("New update: " + remoteVer + " Current version: " + versionNumber);
            } else {
                plugin.setNeedsUpdate(false);
                plugin.getLogger().info("You are running the latest version: " + versionNumber);
            }
        } catch (Throwable t){
            plugin.getLogger().info("Could not get new version.");
            t.printStackTrace();
        }
    }
}
