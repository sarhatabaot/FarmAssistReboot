package com.github.sarhatabaot.farmassistreboot.lang;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author sarhatabaot
 */
public class LanguageManager {
    private final FarmAssistReboot plugin;

    private final List<String> supportedLanguages;
    private LanguageFile activeLanguage;

    public LanguageManager(final FarmAssistReboot plugin) {
        this.plugin = plugin;
        this.supportedLanguages = loadSupportedLanguages();

        saveLanguagesDirectories(plugin);
        switchLanguages(FarmAssistConfig.ACTIVE_LANGUAGE);
    }

    private @NotNull List<String> loadSupportedLanguages() {
        return getFolderNamesFromJar(plugin);
    }

    public void switchLanguages(final String locale) {
        this.activeLanguage = new LanguageFile(plugin,locale,"messages.yml");
    }

    /**
     * Saves the languages from jar
     *
     * @param plugin plugin
     */
    public void saveLanguagesDirectories(final JavaPlugin plugin) {
        File languagesFolder = new File(plugin.getDataFolder(),"languages");
        for(final String locale: supportedLanguages) {
            saveFileFromJar(plugin,"languages" + File.separator + locale,"messages.yml", languagesFolder, false);
        }
    }

    public static List<String> getFolderNamesFromJar(JavaPlugin plugin) {
        List<String> fileNames = new ArrayList<>();
        CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            try (ZipInputStream zip = new ZipInputStream(jar.openStream())) {
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (!name.equals("languages/") && name.startsWith("languages/") && e.isDirectory()) {
                        String folder = name.split("languages/")[1].replace("/","");
                        fileNames.add(folder);
                    }
                }
            } catch (IOException e) {
                //
            }
        }
        return fileNames;
    }

    public static void saveFileFromJar(JavaPlugin plugin, final String resourcePath, final String fileName, final File folder, final boolean replace) {
        File file = new File(folder, fileName);

        if (!file.exists()) {
            plugin.saveResource(resourcePath + File.separator + fileName, replace);
        }
    }

    public LanguageFile getActiveLanguage() {
        return activeLanguage;
    }
}