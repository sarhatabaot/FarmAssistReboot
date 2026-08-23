package com.github.sarhatabaot.farmassistreboot.tasks;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.jetbrains.annotations.Contract;
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
    private final String currentVersion;

    public SimpleUpdateCheckerTask(@NotNull FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void run() {
        if (currentVersion.contains("BETA") || currentVersion.contains("SNAPSHOT") || currentVersion.contains("DEV")) {
            plugin.setNeedsUpdate(false);
            plugin.getLogger().log(Level.INFO, "You are running a development version of FarmAssistReboot. Not checking for updates.");
            return;
        }

        JSONParser parser = new JSONParser();
        try {
            final String latest = "https://api.github.com/repos/sarhatabaot/FarmAssistReboot/releases/latest";
            URL url = new URL(latest);
            URLConnection urlConnection = url.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                Object obj;
                try {
                    obj = parser.parse(in);
                } catch (org.json.simple.parser.ParseException pe) {
                    plugin.setNeedsUpdate(false);
                    plugin.getLogger().log(Level.WARNING, "Could not parse GitHub release JSON", pe);
                    return;
                }
                JSONObject jsonObject = (JSONObject) obj;
                final String remoteVer = ((String) jsonObject.get("tag_name")).replace("v", "");

                if (needsUpdate(currentVersion, remoteVer)) {
                    plugin.setNeedsUpdate(true);
                    plugin.setNewVersion(remoteVer);
                    plugin.getLogger().info(() -> String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateNew(), remoteVer, currentVersion));
                } else {
                    plugin.setNeedsUpdate(false);
                    plugin.getLogger().info(() -> String.format(plugin.getLanguageManager().getActiveLanguage().getUpdateLatestVersion(), currentVersion));
                }

            }
        } catch (java.io.IOException io) {
            plugin.setNeedsUpdate(false);
            plugin.getLogger().info(() -> plugin.getLanguageManager().getActiveLanguage().getUpdateNewVersionFail());
            plugin.getLogger().log(Level.WARNING, "Update check failed: " + io.getMessage(), io);
        } catch (Exception t) {
            plugin.setNeedsUpdate(false);
            plugin.getLogger().log(Level.SEVERE, "Unexpected error during update check", t);
        }
    }

    /**
     * Compares the current version & the remote version to determine if the user needs to update.
     * Version segments are compared numerically (segment by segment) so that
     * {@code 1.4.9} is correctly identified as older than {@code 1.5.0}.
     *
     * @param currentVersion Current version (e.g. {@code "1.4.9.0"})
     * @param remoteVersion  Remote version from github (e.g. {@code "1.5.0"})
     * @return true if the user needs to update
     */
    static boolean needsUpdate(final @NotNull String currentVersion, final @NotNull String remoteVersion) {
        final String[] currentParts = currentVersion.split("\\.");
        final String[] remoteParts  = remoteVersion.split("\\.");
        final int length = Math.max(currentParts.length, remoteParts.length);

        for (int i = 0; i < length; i++) {
            int currentSegment = getSegment(currentParts, i);
            int remoteSegment  = getSegment(remoteParts, i);

            if (remoteSegment > currentSegment) {
                return true;   // remote is newer
            }
            if (remoteSegment < currentSegment) {
                return false;  // current is newer
            }
            // equal — continue to next segment
        }
        return false; // identical
    }

    @Contract(pure = true)
    private static int getSegment(final String @NotNull [] parts, final int index) {
        if (index >= parts.length) {
            return 0; // missing segment is treated as 0 (e.g. "1.5" → 1.5.0)
        }
        try {
            return Integer.parseInt(parts[index]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
