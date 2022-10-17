package com.github.sarhatabaot.farmassistreboot.lang;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.ConfigFile;

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


    public LanguageFile(final FarmAssistReboot plugin, final String locale, final String fileName) {
        super(plugin, "languages" + File.separator + locale + File.separator, fileName, "languages" + File.separator + locale);
        this.locale = locale;

        saveDefaultConfig();
    }

    private void initValues() {
        this.togglePlayerOn = getConfig().getString("toggle.player.on");
        this.togglePlayerOff = getConfig().getString("toggle.player.off");
        this.toggleGlobalOn = getConfig().getString("toggle.global.on");
        this.toggleGlobalOff = getConfig().getString("toggle.global.off");

        this.infoMaintainers = getConfig().getString("info.maintainers");
        this.infoVersion = getConfig().getString("info.version");

        this.updateLatestVersion = getConfig().getString("update.running-latest-version");
        this.updateNew = getConfig().getString("update.new-update");
        this.updateGetNew = getConfig().getString("update.get-new-update");
        this.updateNewVersionFail = getConfig().getString("update.new-version-fail");

        this.langUnsupported = getConfig().getString("lang.unsupported");
        this.langChanged = getConfig().getString("lang.changed");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        initValues();
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

    public String getLocale() {
        return locale;
    }

    public String getLangUnsupported() {
        return langUnsupported;
    }

    public String getLangChanged() {
        return langChanged;
    }
}
