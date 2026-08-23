package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.lang.LanguageFile;
import com.github.sarhatabaot.farmassistreboot.lang.LanguageManager;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link JoinListener}.
 * <p>
 * Locks down the C3 fix: the "new update" branch is now driven by
 * {@code needsUpdate()} directly, so a player is never told there's a new
 * update when the plugin is actually up-to-date.
 * <p>
 * The full {@link Util#sendMessage(CommandSender, String)} path is
 * exercised: this test relies on {@code mockito-inline} to stub the
 * static {@link Bukkit#getPluginManager()} call so the {@code Util} utility
 * can resolve its dependencies without a live server.
 */
class JoinListenerTest {

    private FarmAssistReboot plugin;
    private FarmAssistConfig config;
    private LanguageManager languageManager;
    private LanguageFile languageFile;
    private PluginManager pluginManager;
    private Player player;
    private PlayerJoinEvent event;
    private JoinListener listener;

    @BeforeEach
    void setUp() {
        plugin = mock(FarmAssistReboot.class);
        config = mock(FarmAssistConfig.class);
        languageManager = mock(LanguageManager.class);
        languageFile = new LanguageFile(); // no-arg ctor
        // Populate the language fields from defaults (matches C2's fix behaviour).
        // We use the static helper exposed by LanguageFile#applyValuesTo.
        org.bukkit.configuration.file.FileConfiguration empty =
                org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(
                        new java.io.StringReader(""));
        LanguageFile.applyValuesTo(languageFile, empty);
        pluginManager = mock(PluginManager.class);
        player = mock(Player.class);
        event = mock(PlayerJoinEvent.class);

        when(plugin.getAssistConfig()).thenReturn(config);
        when(plugin.getLanguageManager()).thenReturn(languageManager);
        when(languageManager.getActiveLanguage()).thenReturn(languageFile);
        when(event.getPlayer()).thenReturn(player);

        // Util.sendMessage ultimately calls Bukkit.getPluginManager() to check
        // for PlaceholderAPI. Mock the static via mockito-inline so the call
        // returns a PluginManager that has no plugins (PAPI absent).
        bukkitMock = mockStatic(Bukkit.class);
        bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);
        when(pluginManager.getPlugin("PlaceholderAPI")).thenReturn(null);

        Util.init(plugin);
        listener = new JoinListener(plugin);
    }

    /** Held across the test so we can close the static mock in {@link #tearDown()}. */
    private org.mockito.MockedStatic<Bukkit> bukkitMock;

    @AfterEach
    void tearDown() {
        if (bukkitMock != null) {
            bukkitMock.close();
        }
        // Reset Util's static state so subsequent tests don't see a stale plugin.
        Util.reset();
    }

    // ---------------------------------------------------------------------
    // Permission gate (independent of the update logic)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Player without farmassist.notify.update never receives a message")
    void noPermission_noMessage() {
        when(player.hasPermission(Permissions.UPDATE_NOTIFY)).thenReturn(false);

        listener.onPlayerJoin(event);

        verify(player, never()).sendMessage(anyString());
    }

    // ---------------------------------------------------------------------
    // Needs-update branch
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("When needsUpdate=true, sends 'new update' + 'get at' messages")
    void needsUpdate_sendsNewUpdateMessages() {
        when(player.hasPermission(Permissions.UPDATE_NOTIFY)).thenReturn(true);
        when(plugin.needsUpdate()).thenReturn(true);
        when(plugin.getNewVersion()).thenReturn("1.5.0");
        when(plugin.getDescription()).thenReturn(
                new org.bukkit.plugin.PluginDescriptionFile(
                        "FarmAssistReboot", "1.4.9.0", "com.github.sarhatabaot.farmassistreboot.FarmAssistReboot"));
        when(config.disableLatestVersion()).thenReturn(true);

        listener.onPlayerJoin(event);

        // Two messages: new update + get at
        verify(player, times(2)).sendMessage(anyString());
    }

    // ---------------------------------------------------------------------
    // Latest-version branch
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("When no update AND disableLatestVersion=false, sends 'running latest'")
    void noUpdate_latestMessageEnabled_sendsRunningLatest() {
        when(player.hasPermission(Permissions.UPDATE_NOTIFY)).thenReturn(true);
        when(plugin.needsUpdate()).thenReturn(false);
        when(config.disableLatestVersion()).thenReturn(false);
        when(plugin.getDescription()).thenReturn(
                new org.bukkit.plugin.PluginDescriptionFile(
                        "FarmAssistReboot", "1.4.9.0", "com.github.sarhatabaot.farmassistreboot.FarmAssistReboot"));

        listener.onPlayerJoin(event);

        verify(player, times(1)).sendMessage(anyString());
    }

    @Test
    @DisplayName("Regression: when no update AND disableLatestVersion=true, no 'new update' message is sent")
    void noUpdate_latestMessageDisabled_sendsNoMessage() {
        // C3 regression: before the fix, the listener would fall through to
        // the 'new update' branch whenever disableLatestVersion was true,
        // telling the player there's a new update when there isn't one.
        when(player.hasPermission(Permissions.UPDATE_NOTIFY)).thenReturn(true);
        when(plugin.needsUpdate()).thenReturn(false);
        when(config.disableLatestVersion()).thenReturn(true);

        listener.onPlayerJoin(event);

        verify(player, never()).sendMessage(anyString());
    }
}
