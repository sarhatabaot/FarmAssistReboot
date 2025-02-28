package com.github.sarhatabaot.farmassistreboot.lang;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
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
        switchLanguages(plugin.getAssistConfig().activeLanguage());
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
    public void saveLanguagesDirectories(final @NotNull JavaPlugin plugin) {
        for(final String locale: supportedLanguages) {
            saveFileFromJar(plugin,"languages" + File.separator + locale,"messages.yml");
        }
    }

    public static @NotNull List<String> getFolderNamesFromJar(@NotNull JavaPlugin plugin) {
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

    public static void saveFileFromJar(@NotNull JavaPlugin plugin, final String resourcePath, final String fileName) {
        final String path = resourcePath + File.separator + fileName;
        final File file = new File(plugin.getDataFolder(), path);

        if (!file.exists()) {
            plugin.saveResource(path, false);
        }
    }

    public boolean isSupported(final String locale) {
        return supportedLanguages.contains(locale);
    }

    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public LanguageFile getActiveLanguage() {
        return activeLanguage;
    }
}
