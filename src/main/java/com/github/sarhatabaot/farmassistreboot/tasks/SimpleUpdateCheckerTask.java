package com.github.sarhatabaot.farmassistreboot.tasks;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SimpleUpdateCheckerTask extends BukkitRunnable {
    private final FarmAssistReboot plugin;
    private final String versionNumber;

    public SimpleUpdateCheckerTask(FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.versionNumber = plugin.getDescription().getVersion();
    }

    @Override
    public void run() {
        JSONParser parser = new JSONParser();
        try {
            final String latest = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";
            URL url = new URL(latest);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Object obj = parser.parse(in);
            JSONObject jsonObject = (JSONObject) obj;
            String remoteVer = (String) jsonObject.get("tag_name");
            remoteVer = remoteVer.replace("v","");
            int remoteVal = Integer.parseInt(remoteVer.replace(".", ""));
            int localVer = Integer.parseInt(versionNumber.replace(".", ""));
            if (remoteVal > localVer) {
                plugin.setNeedsUpdate(true);
                plugin.setNewVersion(remoteVer);
                plugin.getLogger().info(String.format("New update: %s Current version: %s",remoteVer,versionNumber));
            } else {
                plugin.setNeedsUpdate(false);
                plugin.getLogger().info(String.format("You are running the latest version: %s" ,versionNumber));
            }
        } catch (Exception t){
            plugin.getLogger().info("Could not get new version.");
            t.printStackTrace();
        }
    }
}
