package com.github.sarhatabaot.farmassistreboot.tasks;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class SimpleUpdateCheckerTask implements Runnable {
    private final FarmAssistReboot plugin;
    private final String versionNumber;

    public SimpleUpdateCheckerTask(@NotNull FarmAssistReboot plugin) {
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
            try(BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                Object obj = parser.parse(in);
                JSONObject jsonObject = (JSONObject) obj;
                final String remoteVer = ((String) jsonObject.get("tag_name")).replace("v", "");
                int remoteVal = Integer.parseInt(remoteVer.replace(".", ""));
                int localVer = Integer.parseInt(versionNumber.replace(".", ""));
                if (remoteVal > localVer) {
                    plugin.setNeedsUpdate(true);
                    plugin.setNewVersion(remoteVer);
                    plugin.getLogger().info(() -> String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateNew(), remoteVer, versionNumber));
                } else {
                    plugin.setNeedsUpdate(false);
                    plugin.getLogger().info(() -> String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateLatestVersion(), versionNumber));
                }
            }
        } catch (Exception t){
            plugin.getLogger().info(() -> plugin.getLanguageManager().getActiveLanguage().getUpdateNewVersionFail());
            plugin.getLogger().log(Level.WARNING,t.getMessage(), t.getCause());
        }
    }
}
