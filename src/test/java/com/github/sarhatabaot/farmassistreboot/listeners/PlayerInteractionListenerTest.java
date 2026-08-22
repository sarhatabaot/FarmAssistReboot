package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PlayerInteractionListener#doesNotHaveWheatAndTillPermissions(Player)}.
 * <p>
 * Locks down the fix for the original C1 precedence bug, where the missing
 * {@code farmassist.till} permission could block auto-till even when
 * {@code use-permissions} was {@code false}.
 */
class PlayerInteractionListenerTest {

    private FarmAssistReboot plugin;
    private FarmAssistConfig config;
    private Player player;
    private PlayerInteractionListener listener;

    @BeforeEach
    void setUp() {
        plugin = mock(FarmAssistReboot.class);
        config = mock(FarmAssistConfig.class);
        player = mock(Player.class);
        when(plugin.getAssistConfig()).thenReturn(config);
        listener = new PlayerInteractionListener(plugin);
    }

    /**
     * Exhaustive truth table for the (usePermissions, hasWheat, hasTill) cases.
     * The expected result is the contract that {@code doesNotHaveWheatAndTillPermissions}
     * MUST satisfy.
     * <p>
     * Columns:
     * <ol>
     *   <li>use-permissions config value</li>
     *   <li>player has {@code farmassist.wheat}</li>
     *   <li>player has {@code farmassist.till}</li>
     *   <li>expected return value (true = blocked)</li>
     * </ol>
     */
    @ParameterizedTest(name = "[usePerms={0}, wheat={1}, till={2}] -> blocked={3}")
    @CsvSource({
            // usePerms, hasWheat, hasTill, expected
            "false,     true,      true,    false",   // gating off -> never blocked
            "false,     true,      false,   false",   // gating off -> still allowed (C1 fix)
            "false,     false,     true,    false",   // gating off -> still allowed (C1 fix)
            "false,     false,     false,   false",   // gating off -> still allowed
            "true,      true,      true,    false",   // both perms -> allowed
            "true,      true,      false,   true",    // missing till -> blocked
            "true,      false,     true,    true",    // missing wheat -> blocked
            "true,      false,     false,   true"     // missing both -> blocked
    })
    @DisplayName("doesNotHaveWheatAndTillPermissions truth table")
    void doesNotHaveWheatAndTillPermissions_truthTable(
            boolean usePermissions,
            boolean hasWheat,
            boolean hasTill,
            boolean expected) {

        when(config.usePermissions()).thenReturn(usePermissions);
        when(player.hasPermission(Permissions.WHEAT)).thenReturn(hasWheat);
        when(player.hasPermission(Permissions.TILL)).thenReturn(hasTill);

        if (expected) {
            assertTrue(listener.doesNotHaveWheatAndTillPermissions(player),
                    "expected blocked=true for (usePerms=" + usePermissions
                            + ", wheat=" + hasWheat + ", till=" + hasTill + ")");
        } else {
            assertFalse(listener.doesNotHaveWheatAndTillPermissions(player),
                    "expected blocked=false for (usePerms=" + usePermissions
                            + ", wheat=" + hasWheat + ", till=" + hasTill + ")");
        }
    }

    @Test
    @DisplayName("Regression: when use-permissions=false, missing till perm must NOT block")
    void doesNotHaveWheatAndTillPermissions_usePermissionsFalse_neverBlocks() {
        when(config.usePermissions()).thenReturn(false);
        when(player.hasPermission(Permissions.WHEAT)).thenReturn(true);
        when(player.hasPermission(Permissions.TILL)).thenReturn(false);

        // Original bug returned `true` here because of `||` precedence
        assertFalse(listener.doesNotHaveWheatAndTillPermissions(player),
                "Auto-till must not be blocked when use-permissions is disabled, " +
                "even if the player lacks farmassist.till");
    }

    @Test
    @DisplayName("When use-permissions=true and player has both perms, allow")
    void doesNotHaveWheatAndTillPermissions_allPermissionsPresent_allow() {
        when(config.usePermissions()).thenReturn(true);
        when(player.hasPermission(Permissions.WHEAT)).thenReturn(true);
        when(player.hasPermission(Permissions.TILL)).thenReturn(true);

        assertFalse(listener.doesNotHaveWheatAndTillPermissions(player));
    }

    @Test
    @DisplayName("When use-permissions=true and player is missing till, block")
    void doesNotHaveWheatAndTillPermissions_missingTill_block() {
        when(config.usePermissions()).thenReturn(true);
        when(player.hasPermission(Permissions.WHEAT)).thenReturn(true);
        when(player.hasPermission(Permissions.TILL)).thenReturn(false);

        assertTrue(listener.doesNotHaveWheatAndTillPermissions(player));
    }

    @Test
    @DisplayName("When use-permissions=true and player is missing wheat, block")
    void doesNotHaveWheatAndTillPermissions_missingWheat_block() {
        when(config.usePermissions()).thenReturn(true);
        when(player.hasPermission(Permissions.WHEAT)).thenReturn(false);
        when(player.hasPermission(Permissions.TILL)).thenReturn(true);

        assertTrue(listener.doesNotHaveWheatAndTillPermissions(player));
    }
}

