package com.github.sarhatabaot.farmassistreboot;

import com.cryptomorin.xseries.XMaterial;
import com.github.sarhatabaot.farmassistreboot.crop.Crop;
import com.github.sarhatabaot.farmassistreboot.utils.Util;
import org.bukkit.Material;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {


    @ParameterizedTest
    @CsvSource({
            // Early Minecraft versions that should map to "1.0"
            "1.0, 1.0",
            "1.1, 1.0",
            "1.2, 1.0",
            "1.3, 1.0",
            "1.4, 1.0",
            "1.5, 1.0",
            "1.6, 1.0",
            "1.7, 1.0",
            "1.8, 1.0",

            // Mid-range Minecraft versions that should map to "1.9"
            "1.9, 1.9",
            "1.10, 1.9",
            "1.11, 1.9",
            "1.12, 1.9",
            "1.13, 1.9",

            // Late-range Minecraft version that should map to "1.14"
            "1.14, 1.14",
            "1.15, 1.14",
            "1.16, 1.14",
            "1.17, 1.14",
            "1.18, 1.14",
            "1.19, 1.14",

            // Latest Minecraft versions that should map to "1.20"
            "1.20, 1.20",
            "1.21, 1.20",

            // Unsupported version that should return "Unsupported"
            "2.0, Unsupported"
    })
    void testGetJsonCropVersion(String version, String expected) {
        assertEquals(expected, Util.getJsonCropVersion(version),
                "The version " + version + " should map to '" + expected + "'"
        );
    }


    @ParameterizedTest
    @CsvSource({
            "1.16.0, 1.16",           // Valid version string with multiple segments
            "2.3-RELEASE, 2.3",       // Version with a dash
            "16, Unknown",            // Invalid version format
            "'', Unknown",            // Empty version string
            "1.2.3.4, 1.2"            // Version string with multiple dots
    })
    void testFormatVersion(String version, String expected) {
        // Test case for various input versions
        String actual = Util.formatVersion(version);
        assertEquals(expected, actual, "The version " + version + " should be formatted as '" + expected + "'");
    }

    @ParameterizedTest
    @CsvSource({
            "en, languages\\en\\messages.yml",
            "fr, languages\\fr\\messages.yml",
    })
    void testLanguagePath(String locale, String expected) {
        String actual = Util.getLangFilePath(locale);
        assertEquals(expected, actual, "The language path for locale "+ locale + "should be '" + expected + "'");
    }



    // Parameterized test for getCropPermission
    @ParameterizedTest
    @CsvSource({
      "wheat, farmassist.crops.wheat",
      "carrot, farmassist.crops.carrot",
      "potato, farmassist.crops.potato"
    })
    void testGetCropPermission(String crop, String expectedPermission) {
        String actualPermission = Util.getCropPermission(crop);
        assertEquals(expectedPermission, actualPermission);
    }


}

