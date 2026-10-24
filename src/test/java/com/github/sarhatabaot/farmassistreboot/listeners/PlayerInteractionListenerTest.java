package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import com.github.sarhatabaot.farmassistreboot.Util;
import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PlayerInteractionListener}.
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

    @Test
    @DisplayName("Auto-till schedules wheat in the air above farmland")
    void onPlayerInteract_schedulesWheatAboveFarmland() {
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);
        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack hoe = mock(ItemStack.class);
        Block dirt = mock(Block.class);
        Block above = mock(Block.class);
        World world = mock(World.class);

        when(plugin.isGlobalEnabled()).thenReturn(true);
        when(plugin.getDisabledPlayerList()).thenReturn(Collections.emptySet());
        when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(event.getPlayer()).thenReturn(player);
        when(event.hasBlock()).thenReturn(true);
        when(event.getClickedBlock()).thenReturn(dirt);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(hoe);
        when(hoe.getType()).thenReturn(Material.IRON_HOE);
        when(dirt.getType()).thenReturn(Material.DIRT);
        when(dirt.getRelative(BlockFace.UP)).thenReturn(above);
        when(above.getType()).thenReturn(Material.AIR);
        when(player.getWorld()).thenReturn(world);
        when(world.getName()).thenReturn("world");
        when(config.getEnabled(Material.WHEAT)).thenReturn(true);
        when(config.plantWheatOnTill()).thenReturn(true);

        try (org.mockito.MockedStatic<Util> util = mockStatic(Util.class)) {
            util.when(() -> Util.isWorldDisabled("world")).thenReturn(false);
            util.when(() -> Util.isMissingRequiredSeeds(player, Material.WHEAT)).thenReturn(false);

            listener.onPlayerInteract(event);

            verify(dirt).setType(Material.FARMLAND);
            util.verify(() -> Util.replant(player, above, Material.WHEAT));
        }
    }
}

