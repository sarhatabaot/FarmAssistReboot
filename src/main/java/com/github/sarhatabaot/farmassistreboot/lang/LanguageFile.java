package com.github.sarhatabaot.farmassistreboot.lang;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author sarhatabaot
 */
public class LanguageFile extends ConfigFile<FarmAssistReboot> {
    private final String locale;
    private String togglePlayerOn;
    private String togglePlayerOff;

    private String toggleGlobalOn;
    private String toggleGlobalOff;

    private String infoVersion;
    private String infoMaintainers;

    private String updateLatestVersion;
    private String updateNew;
    private String updateGetNew;
    private String updateNewVersionFail;

    private String langUnsupported;
    private String langChanged;

    private String prefix;


    /**
     * No-arg constructor for unit tests only. Allows the test suite to
     * instantiate a {@link LanguageFile} without supplying a real
     * {@link FarmAssistReboot} and data folder. The instance produced by
     * this constructor is NOT usable for normal plugin operation — its
     * {@code config} field is {@code null} until {@link #initValues()} is
     * called via {@link #applyValuesTo(LanguageFile, FileConfiguration)}.
     * <p>
     * Package-private to keep it out of the public API.
     */
    public LanguageFile() {
        super();
        this.locale = "test";
    }

    public LanguageFile(final FarmAssistReboot plugin, final String locale, final String fileName) {
        super(plugin, "languages" + File.separator + locale + File.separator, fileName, "languages" + File.separator + locale);
        this.locale = locale;

        saveDefaultConfig();
    }

    /**
     * Populate the cached message strings from the active configuration.
     * <p>
     * Every lookup passes a default value so a missing translation key never
     * surfaces as {@code null} to callers (which would later NPE inside
     * {@link String#format(String, Object...)} or
     * {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)}).
     * <p>
     * Package-private for unit testing.
     */
    void initValues() {
        applyValuesTo(this, getConfig());
    }

    /**
     * Static, dependency-injected variant of {@link #initValues()}. Lets unit
     * tests populate the message cache from an in-memory
     * {@link org.bukkit.configuration.file.FileConfiguration} without needing
     * a real plugin or temp data folder.
     * <p>
     * Package-private for unit testing.
     *
     * @param target the {@link LanguageFile} whose fields to populate
     * @param config the configuration to read keys from (may be {@code null},
     *               in which case the method is a no-op)
     */
    public static void applyValuesTo(final LanguageFile target,
                                     final org.bukkit.configuration.file.FileConfiguration config) {
        if (target == null || config == null) {
            return;
        }
        target.togglePlayerOn       = config.getString("toggle.player-on",       DEFAULTS.togglePlayerOn);
        target.togglePlayerOff      = config.getString("toggle.player-off",      DEFAULTS.togglePlayerOff);
        target.toggleGlobalOn       = config.getString("toggle.global-on",       DEFAULTS.toggleGlobalOn);
        target.toggleGlobalOff      = config.getString("toggle.global-off",      DEFAULTS.toggleGlobalOff);
        target.infoVersion          = config.getString("info.version",           DEFAULTS.infoVersion);
        target.infoMaintainers      = config.getString("info.maintainers",       DEFAULTS.infoMaintainers);
        target.updateLatestVersion  = config.getString("update.running-latest-version", DEFAULTS.updateLatestVersion);
        target.updateNew            = config.getString("update.new-update",      DEFAULTS.updateNew);
        target.updateGetNew         = config.getString("update.get-new-update",  DEFAULTS.updateGetNew);
        target.updateNewVersionFail = config.getString("update.new-version-fail", DEFAULTS.updateNewVersionFail);
        target.langUnsupported      = config.getString("lang.unsupported",       DEFAULTS.langUnsupported);
        target.langChanged          = config.getString("lang.changed",           DEFAULTS.langChanged);
        target.prefix               = config.getString("prefix",                 DEFAULTS.prefix);
    }

    /**
     * Default values for every message key, in one place. Centralising the
     * defaults here keeps {@link #initValues()} simple and gives unit tests a
     * single point of reference for the bundled fall-back strings.
     */
    static final class Defaults {
        final String togglePlayerOn       = "&aFarmAssistReboot functions are now on for you!";
        final String togglePlayerOff      = "&aFarmAssistReboot functions turned off for you!";
        final String toggleGlobalOn       = "&aFarmAssistReboot functions are globally back on!";
        final String toggleGlobalOff      = "&aFarmAssistReboot functions turned off globally!";
        final String infoVersion          = "%s version: %s";
        final String infoMaintainers      = "Maintainers: %s";
        final String updateLatestVersion  = "You are running the latest version: %s";
        final String updateNew            = "New update: %s Current version: %s";
        final String updateGetNew         = "Get at %s";
        final String updateNewVersionFail = "Could not get new version.";
        final String langUnsupported      = "Unsupported locale: %s";
        final String langChanged          = "Changed locale from %s to %s.";
        final String prefix               = "&7[&aFarmAssistReboot&7]&r ";
    }

    /** Shared {@link Defaults} instance used by {@link #initValues()}. */
    private static final Defaults DEFAULTS = new Defaults();

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        initValues();
    }

    public String getLocale() {
        return locale;
    }

    public String getTogglePlayerOn() {
        return togglePlayerOn;
    }

    public String getTogglePlayerOff() {
        return togglePlayerOff;
    }

    public String getToggleGlobalOn() {
        return toggleGlobalOn;
    }

    public String getToggleGlobalOff() {
        return toggleGlobalOff;
    }

    public String getInfoVersion() {
        return infoVersion;
    }

    public String getInfoMaintainers() {
        return infoMaintainers;
    }

    public String getUpdateLatestVersion() {
        return updateLatestVersion;
    }

    public String getUpdateNew() {
        return updateNew;
    }

    public String getUpdateGetNew() {
        return updateGetNew;
    }

    public String getUpdateNewVersionFail() {
        return updateNewVersionFail;
    }

    public String getLangUnsupported() {
        return langUnsupported;
    }

    public String getLangChanged() {
        return langChanged;
    }

    public String getPrefix() {
        return prefix;
    }
}
