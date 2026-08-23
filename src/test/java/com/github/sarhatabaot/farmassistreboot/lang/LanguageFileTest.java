package com.github.sarhatabaot.farmassistreboot.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link LanguageFile}.
 * <p>
 * These tests cover the C2 fix: when a translation key is missing from the
 * active language file, the corresponding getter must return a bundled
 * default (never {@code null}).
 * <p>
 * The tests bypass the real {@link com.github.sarhatabaot.farmassistreboot.FarmAssistReboot}
 * constructor by going through the static helper
 * {@link LanguageFile#applyValuesTo(LanguageFile, FileConfiguration)} with
 * an in-memory {@link org.bukkit.configuration.file.YamlConfiguration}.
 */
class LanguageFileTest {

    /**
     * The {@link LanguageFile} under test. Constructed via the
     * package-private no-arg constructor so we can drive
     * {@link LanguageFile#applyValuesTo(LanguageFile, FileConfiguration)}
     * without touching the file system or instantiating a real
     * {@link com.github.sarhatabaot.farmassistreboot.FarmAssistReboot}.
     */
    private LanguageFile file;

    @BeforeEach
    void setUp() {
        // Use the package-private no-arg constructor so we can drive
        // applyValuesTo without touching the file system or instantiating
        // a real FarmAssistReboot.
        file = new LanguageFile();
    }

    /**
     * Load an in-memory YamlConfiguration from a YAML string. We use
     * {@link org.bukkit.configuration.file.YamlConfiguration#loadConfiguration(java.io.Reader)}
     * because Bukkit does not expose a {@code fromString} factory directly.
     */
    private static FileConfiguration configFromString(final String yaml) {
        return org.bukkit.configuration.file.YamlConfiguration
                .loadConfiguration(new StringReader(yaml));
    }

    // ---------------------------------------------------------------------
    // Missing-key behaviour (the C2 fix)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("When the config is empty, every getter returns the bundled default")
    void emptyConfig_yieldsDefaults() {
        FileConfiguration empty = configFromString("");

        LanguageFile.applyValuesTo(file, empty);

        LanguageFile.Defaults d = new LanguageFile.Defaults();
        assertEquals(d.togglePlayerOn, file.getTogglePlayerOn());
        assertEquals(d.togglePlayerOff, file.getTogglePlayerOff());
        assertEquals(d.toggleGlobalOn, file.getToggleGlobalOn());
        assertEquals(d.toggleGlobalOff, file.getToggleGlobalOff());
        assertEquals(d.infoVersion, file.getInfoVersion());
        assertEquals(d.infoMaintainers, file.getInfoMaintainers());
        assertEquals(d.updateLatestVersion, file.getUpdateLatestVersion());
        assertEquals(d.updateNew, file.getUpdateNew());
        assertEquals(d.updateGetNew, file.getUpdateGetNew());
        assertEquals(d.updateNewVersionFail, file.getUpdateNewVersionFail());
        assertEquals(d.langUnsupported, file.getLangUnsupported());
        assertEquals(d.langChanged, file.getLangChanged());
        assertEquals(d.prefix, file.getPrefix());
    }

    @Test
    @DisplayName("When a single key is missing, only that getter returns the default")
    void partiallyMissingKey_fallsBackToDefault() {
        FileConfiguration partial = configFromString(
                "toggle:\n" +
                "  player-on: \"&b[custom on]&r\"\n" +
                "  player-off: \"&b[custom off]&r\"\n"
        );

        LanguageFile.applyValuesTo(file, partial);

        // Present keys win
        assertEquals("&b[custom on]&r", file.getTogglePlayerOn());
        assertEquals("&b[custom off]&r", file.getTogglePlayerOff());

        // Missing keys fall back to defaults (NOT null)
        assertEquals(new LanguageFile.Defaults().toggleGlobalOn, file.getToggleGlobalOn());
        assertEquals(new LanguageFile.Defaults().prefix, file.getPrefix());
    }

    @Test
    @DisplayName("Regression: getString used to return null for missing keys, NPE in String.format")
    void missingKey_doesNotReturnNull() {
        FileConfiguration partial = configFromString("toggle:\n  player-on: \"&aok\"\n");

        LanguageFile.applyValuesTo(file, partial);

        // The key that triggered the original bug was the prefix (the only key
        // that already had a default before the fix). For every other key, the
        // old code returned null. Assert none of them are null now.
        assertNotNull(file.getTogglePlayerOn());
        assertNotNull(file.getTogglePlayerOff());
        assertNotNull(file.getToggleGlobalOn());
        assertNotNull(file.getToggleGlobalOff());
        assertNotNull(file.getInfoVersion());
        assertNotNull(file.getInfoMaintainers());
        assertNotNull(file.getUpdateLatestVersion());
        assertNotNull(file.getUpdateNew());
        assertNotNull(file.getUpdateGetNew());
        assertNotNull(file.getUpdateNewVersionFail());
        assertNotNull(file.getLangUnsupported());
        assertNotNull(file.getLangChanged());
        assertNotNull(file.getPrefix());
    }

    @Test
    @DisplayName("String.format works on the loaded values without throwing")
    void loadedValues_areUsableWithStringFormat() {
        FileConfiguration empty = configFromString("");

        LanguageFile.applyValuesTo(file, empty);

        // Before the fix, these calls would throw IllegalFormatException
        // because the message string was null.
        String version = String.format(file.getInfoVersion(), "FarmAssistReboot", "1.4.9");
        assertEquals("FarmAssistReboot version: 1.4.9", version);

        String unsupported = String.format(file.getLangUnsupported(), "klingon");
        assertEquals("Unsupported locale: klingon", unsupported);

        String changed = String.format(file.getLangChanged(), "en", "fr");
        assertEquals("Changed locale from en to fr.", changed);
    }

    // ---------------------------------------------------------------------
    // Full happy path: every key present, all values match
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("When all keys are present, all values come from the file")
    void fullConfig_allValuesFromFile() {
        String yaml =
                "toggle:\n" +
                "  player-on: \"tpo\"\n" +
                "  player-off: \"tpf\"\n" +
                "  global-on: \"tgo\"\n" +
                "  global-off: \"tgf\"\n" +
                "info:\n" +
                "  version: \"iv\"\n" +
                "  maintainers: \"im\"\n" +
                "update:\n" +
                "  running-latest-version: \"ulv\"\n" +
                "  new-update: \"unu\"\n" +
                "  get-new-update: \"ugn\"\n" +
                "  new-version-fail: \"uvf\"\n" +
                "lang:\n" +
                "  unsupported: \"lu\"\n" +
                "  changed: \"lc\"\n" +
                "prefix: \"px\"\n";
        FileConfiguration config = configFromString(yaml);

        LanguageFile.applyValuesTo(file, config);

        assertEquals("tpo", file.getTogglePlayerOn());
        assertEquals("tpf", file.getTogglePlayerOff());
        assertEquals("tgo", file.getToggleGlobalOn());
        assertEquals("tgf", file.getToggleGlobalOff());
        assertEquals("iv", file.getInfoVersion());
        assertEquals("im", file.getInfoMaintainers());
        assertEquals("ulv", file.getUpdateLatestVersion());
        assertEquals("unu", file.getUpdateNew());
        assertEquals("ugn", file.getUpdateGetNew());
        assertEquals("uvf", file.getUpdateNewVersionFail());
        assertEquals("lu", file.getLangUnsupported());
        assertEquals("lc", file.getLangChanged());
        assertEquals("px", file.getPrefix());
    }

    // ---------------------------------------------------------------------
    // Defensive: null inputs
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("applyValuesTo(null, ...) is a no-op (does not throw)")
    void applyValuesTo_nullTarget_isNoOp() {
        FileConfiguration config = configFromString("toggle:\n  player-on: \"x\"\n");

        // Should not throw.
        LanguageFile.applyValuesTo(null, config);
    }

    @Test
    @DisplayName("applyValuesTo(file, null) is a no-op (does not throw, fields untouched)")
    void applyValuesTo_nullConfig_isNoOp() {
        // Before the call, fields are at their JVM-default (null).
        assertNull(file.getTogglePlayerOn());

        // Should not throw.
        LanguageFile.applyValuesTo(file, null);

        // After the call, fields are still untouched.
        assertNull(file.getTogglePlayerOn());
    }
}
