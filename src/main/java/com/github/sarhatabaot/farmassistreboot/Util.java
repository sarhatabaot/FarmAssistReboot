package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.messages.Permissions;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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
     * Will return -1 if no slot is found.
     *
     * @param playerInventory The player inventory
     * @param material Material to check
     * @return Return the slot matching the material. Will return -1 if no slot is found. Check for nbt/renamed.
     */
    public static int inventoryContainsSeeds(@NotNull PlayerInventory playerInventory, @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        final Material seedMaterial = crop.getSeed().parseMaterial();
        if (seedMaterial == null) {
            debug("There was a problem parsing the crop or the seed material for: %s", material.name());
            return -1;
        }

        Map<Integer, ? extends ItemStack> itemsSlotsMap = playerInventory.all(seedMaterial);
        if (itemsSlotsMap.isEmpty())
            return -1;
        List<Map.Entry<Integer, ? extends ItemStack>> list = itemsSlotsMap.entrySet().stream()
                .filter(p -> {
                    if (FarmAssistConfig.IGNORE_RENAMED) {
                        ItemStack itemStack = p.getValue();
                        return itemStack.getItemMeta() != null && !itemStack.getItemMeta().hasDisplayName();
                    }
                    return true;
                })
                .filter(p -> {
                    if (FarmAssistConfig.IGNORE_NBT) {
                        return NBT.get(p.getValue(), ReadableItemNBT::hasNBTData);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (list.isEmpty())
            return -1;

        return list.get(0).getKey();
    }

    public static boolean isWorldDisabled(@NotNull World world) {
        debug(Debug.Worlds.CONFIG_PER_WORLD, FarmAssistConfig.ENABLED_PER_WORLD);
        if (!FarmAssistConfig.ENABLED_PER_WORLD)
            return false;

        debug(Debug.Worlds.IS_WORLD_ENABLED, world.getName(), FarmAssistConfig.ENABLED_WORLDS.contains(world));
        return !FarmAssistConfig.ENABLED_WORLDS.contains(world);
    }

    public static void replant(@NotNull Player player, Block block, @NotNull Material material) {
        final Crop crop = Crop.valueOf(material.name());
        final Material seedMaterial = crop.getSeed().parseMaterial();
        if (seedMaterial == null) {
            debug("There was a problem parsing the crop or the seed material for: %s", material.name());
            return;
        }

        int spot = player.getInventory().first(seedMaterial);
        replant(player, block, spot);
    }

    public static void replant(@NotNull Player player, Block block, int spot) {
        debug("Spot:" + spot);
        debug("CONFIG:no-seeds: %b, PERMISSION:farmassist.no_seeds: %b", FarmAssistConfig.NO_SEEDS, player.hasPermission(Permissions.NO_SEEDS));
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
        sender.sendMessage(color(plugin.getLanguageManager().getActiveLanguage().getPrefix() + message)); //Add this to lang manager
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
        return FarmAssistConfig.NO_SEEDS || player.hasPermission(Permissions.NO_SEEDS);
    }

    public static boolean checkSeedsOrNoSeedsInInventory(final Player player, final Material material) {
        return !checkNoSeeds(player) || Util.inventoryContainsSeeds(player.getInventory(), material) == -1;
    }
}
