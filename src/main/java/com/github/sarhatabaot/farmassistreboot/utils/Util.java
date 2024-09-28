package com.github.sarhatabaot.farmassistreboot.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Util {
    private Util() {
        throw new UnsupportedOperationException("Utility class");
    }

    @Contract(pure = true)
    public static @NotNull String formatVersion(final @NotNull String version) {
        // Split the version string on the dot (.) and dash (-)
        String[] split = version.split("[.-]");

        if (split.length >= 2) {
            // Return major version as "1.x" (e.g., "1.16")
            return split[0] + "." + split[1];
        } else {
            // Return unknown if the version is not in expected format
            return "Unknown";
        }
    }


    public static @NotNull String getMajorMinecraftVersion() {
        // Get the Bukkit version string (format: "1.x.x-Rx")
        final String version = Bukkit.getBukkitVersion();
        return formatVersion(version);
    }

    @Contract(pure = true)
    public static @NotNull String getJsonCropVersion(final @NotNull String majorMinecraftVersion) {
        switch (majorMinecraftVersion) {
            case "1.0":
            case "1.1":
            case "1.2":
            case "1.3":
            case "1.4":
            case "1.5":
            case "1.6":
            case "1.7":
            case "1.8":
                return "1.0";
            case "1.9":
            case "1.10":
            case "1.11":
            case "1.12":
            case "1.13":
                return "1.9";
            case "1.14":
            case "1.15":
            case "1.16":
            case "1.17":
            case "1.18":
            case "1.19":
                return "1.14";
            case "1.20":
            case "1.21":
                return "1.20";
            default:
                return "Unsupported";
        }
    }

    public static @NotNull String getJsonCropVersionFromMinecraftVersion() {
        final String majorMinecraftVersion = getMajorMinecraftVersion();

        return getJsonCropVersion(majorMinecraftVersion);
    }

    public static String getLangFilePath(final String locale) {
        return "languages" + File.separator + locale + File.separator + "messages.yml";
    }

    public static String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }

}
