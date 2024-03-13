package com.github.sarhatabaot.farmassistreboot.lang;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.ConfigFile;
import lombok.Getter;

import java.io.File;

/**
 * @author sarhatabaot
 */
@Getter
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


    public LanguageFile(final FarmAssistReboot plugin, final String locale, final String fileName) {
        super(plugin, "languages" + File.separator + locale + File.separator, fileName, "languages" + File.separator + locale);
        this.locale = locale;

        saveDefaultConfig();
    }

    private void initValues() {
        this.togglePlayerOn = getConfig().getString("toggle.player-on");
        this.togglePlayerOff = getConfig().getString("toggle.player-off");
        this.toggleGlobalOn = getConfig().getString("toggle.global-on");
        this.toggleGlobalOff = getConfig().getString("toggle.global-off");

        this.infoMaintainers = getConfig().getString("info.maintainers");
        this.infoVersion = getConfig().getString("info.version");

        this.updateLatestVersion = getConfig().getString("update.running-latest-version");
        this.updateNew = getConfig().getString("update.new-update");
        this.updateGetNew = getConfig().getString("update.get-new-update");
        this.updateNewVersionFail = getConfig().getString("update.new-version-fail");

        this.langUnsupported = getConfig().getString("lang.unsupported");
        this.langChanged = getConfig().getString("lang.changed");

        this.prefix = getConfig().getString("prefix", "&7[&aFarmAssistReboot&7]&r ");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        initValues();
    }

}
