package io.github.sarhatabaot.farmassistreboot.tasks;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.json.JSONObject;

import java.net.URL;
import java.util.Scanner;

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
        try {
            URL updateTag = new URL(latest);
            Scanner scanner = new Scanner(updateTag.openStream());
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }
            scanner.close();
            String str = stringBuilder.toString();

            JSONObject obj = new JSONObject(str);
            String remoteVer = obj.get("tag_name").toString();
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
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
