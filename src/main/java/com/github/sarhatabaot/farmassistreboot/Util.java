package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Util {
    private static FarmAssistReboot plugin;

    private Util() {
        throw new UnsupportedOperationException();
    }

    public static void init(final FarmAssistReboot plugin) {
        Util.plugin = plugin;
    }

    /**
     * Reset the static plugin reference. Intended for unit tests that
     * initialise {@link Util#init(FarmAssistReboot)} in {@code @BeforeEach}
     * and need to clean up in {@code @AfterEach} so test order doesn't
     * leak state between cases.
     */
    public static void reset() {
        Util.plugin = null;
    }

    /**
     * Will return -1 if no slot is found.
     *
     * @param playerInventory The player inventory
     * @param material Material to check
     * @return Return the slot matching the material. Will return -1 if no slot is found. Check for nbt/renamed.
     */
    public static int inventoryContainsSeeds(@NotNull PlayerInventory playerInventory, @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        final Material seedMaterial = crop.getSeed().get();
        if (seedMaterial == null) {
            debug("There was a problem parsing the crop or the seed material for: %s", material.name());
            return -1;
        }

        Map<Integer, ? extends ItemStack> itemsSlotsMap = playerInventory.all(seedMaterial);
        if (itemsSlotsMap.isEmpty())
            return -1;
        List<Map.Entry<Integer, ? extends ItemStack>> list = itemsSlotsMap.entrySet().stream()
                .filter(p -> {
                    if (plugin.getAssistConfig().ignoreRenamed()) {
                        ItemStack itemStack = p.getValue();
                        return itemStack.getItemMeta() != null && !itemStack.getItemMeta().hasDisplayName();
                    }
                    return true;
                })
                .filter(p -> {
                    if (plugin.getAssistConfig().ignoreNbt()) {
                        return NBT.get(p.getValue(), ReadableItemNBT::hasNBTData);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (list.isEmpty())
            return -1;

        return list.get(0).getKey();
    }


    public static boolean isWorldDisabled(@NotNull String world) {
        debug(Debug.Worlds.CONFIG_PER_WORLD, plugin.getAssistConfig().enabledPerWorld());
        if (!plugin.getAssistConfig().enabledPerWorld())
            return false;

        final boolean isWorldEnabled = plugin.getAssistConfig().isWorldEnabled(world);
        debug(Debug.Worlds.IS_WORLD_ENABLED, world, isWorldEnabled);
        return !isWorldEnabled;
    }

    public static void replant(@NotNull Player player, Block block, @NotNull Material material) {
        int spot = inventoryContainsSeeds(player.getInventory(), material);
        replant(player, block, spot);
    }

    public static void replant(@NotNull Player player, Block block, int spot) {
        debug("Spot: %d", spot);
        debug("CONFIG:no-seeds: %b, PERMISSION:farmassist.no_seeds: %b", plugin.getAssistConfig().noSeeds(), player.hasPermission(Permissions.NO_SEEDS));
        if (spot >= 0 || Util.checkNoSeeds(player)) {
            removeOrSubtractItem(player, spot);
            plugin.getPaperLib().scheduling().regionSpecificScheduler(block.getLocation()).runDelayed(new ReplantTask(block,plugin), 5L);
        }
    }

    public static void removeOrSubtractItem(@NotNull Player player, int spot) {
        if(Util.checkNoSeeds(player)) {
            return;
        }

        ItemStack next = player.getInventory().getItem(spot);
        if (next != null && next.getAmount() > 1) {
            next.setAmount(next.getAmount() - 1);
            player.getInventory().setItem(spot, next);
        } else {
            player.getInventory().setItem(spot, new ItemStack(Material.AIR));
        }
    }

    private static void debug(final String message) {
        Util.plugin.debug(Util.class, message);
    }

    public static void debug(final String message, Object... args) {
        debug(String.format(message, args));
    }

    public static void sendMessage(final @NotNull CommandSender sender, final String message) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            sendPrefixedAndColoredMessage(sender, PlaceholderAPI.setPlaceholders(!(sender instanceof Player) ? null : (Player) sender, message));
            return;
        }

        sendPrefixedAndColoredMessage(sender, message);
    }

    public static void sendPrefixedAndColoredMessage(final @NotNull CommandSender sender, final String message) {
        com.github.sarhatabaot.farmassistreboot.lang.LanguageFile lang =
                plugin.getLanguageManager().getActiveLanguage();
        String prefix = (lang != null && lang.getPrefix() != null)
                ? lang.getPrefix()
                : "&7[&aFarmAssistReboot&7]&r ";
        sender.sendMessage(color(prefix + message));
    }

    @Contract("_ -> new")
    public static @NotNull String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Checks if the "no-seeds" config option is enabled
     * or if the player has the "no-seeds" permission.
     * @param player Player to check
     * @return true if the config option is enabled or if the player has the permission
     */
    public static boolean checkNoSeeds(final Player player) {
        return plugin.getAssistConfig().noSeeds() || player.hasPermission(Permissions.NO_SEEDS);
    }

    public static boolean checkNoDrops(final Player player) {
        return plugin.getAssistConfig().noDrops() || player.hasPermission(Permissions.NO_DROPS);
    }

    /**
     * L4: renamed from {@code checkSeedsOrNoSeedsInInventory} — the old
     * name was ambiguous (it could be read as "are the seeds missing? OR
     * are we in no-seeds mode?"). The new name states the actual return
     * contract: returns {@code true} iff the player must have seeds and
     * doesn't.
     *
     * @return {@code true} if the player is required to have seeds and
     *         their inventory does not contain any
     */
    public static boolean isMissingRequiredSeeds(final Player player, final Material material) {
        return isMissingRequiredSeeds(player, Util.inventoryContainsSeeds(player.getInventory(), material));
    }

    public static boolean isMissingRequiredSeeds(final Player player, int slot) {
        return !checkNoSeeds(player) && slot == -1;
    }
}
