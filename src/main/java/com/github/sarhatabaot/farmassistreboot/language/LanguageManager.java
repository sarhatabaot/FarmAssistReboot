package com.github.sarhatabaot.farmassistreboot.language;


import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.MainConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LanguageManager {
    private final FarmAssistReboot plugin;
    private final MainConfig config;
    private final Map<String, LanguageFile> languages;
    private final Map<UUID, String> playerLanguages; //todo, cache online players only. Offline players will get saved to a database file.

    public LanguageManager(FarmAssistReboot plugin, MainConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.languages = new HashMap<>();
        this.playerLanguages = new HashMap<>();

        loadSupportedLanguages();
    }

    public void reload() {
        this.languages.forEach((s, languageFile) -> {
            try {
                languageFile.reload();
            } catch (IOException e) {
                plugin.getLogger().info("There was a problem reloading the language file: " + s);
            }
        });
    }


    public boolean isNotSupported(final String language) {
        return !languages.containsKey(language);
    }

    public LanguageFile getLanguage(String language) {
        return languages.get(language);
    }

    public LanguageFile getActiveLanguage() {
        return getLanguage(config.getDefaultLocale());
    }

    public LanguageFile getPlayerLanguage(final @NotNull Player player) {
        final LanguageFile playerLanguage = getLanguage(playerLanguages.get(player.getUniqueId()));
        if (playerLanguage == null) {
            return getActiveLanguage();
        }
        return playerLanguage;
    }

    private void loadSupportedLanguages() {
        final List<String> locales = getFolderNamesFromJar(plugin);
        for (final String locale : locales) {
            try {
                languages.put(locale, new LanguageFile(plugin, locale));
            } catch (final IOException e) {
                plugin.getLogger().warning("Failed to load language: " + locale);
            }
        }
    }

    public static @NotNull List<String> getFolderNamesFromJar(@NotNull JavaPlugin plugin) {
        final String langFolder = "languages/";
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
                    if (!name.equals(langFolder) && name.startsWith(langFolder) && e.isDirectory()) {
                        String folder = name.split(langFolder)[1].replace("/","");
                        fileNames.add(folder);
                    }
                }
            } catch (IOException e) {
                //
            }
        }
        return fileNames;
    }

    public void setPlayerLanguage(final Player player, final String language) {
        this.playerLanguages.put(player.getUniqueId(), language);
    }

    public void save() {
        //todo
    }
}
