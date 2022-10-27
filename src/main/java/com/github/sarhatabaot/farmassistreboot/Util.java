package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
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
import org.jetbrains.annotations.NotNull;


public class Util {
    private static FarmAssistReboot plugin;

    private Util() {
        throw new UnsupportedOperationException();
    }

    public static void init(final FarmAssistReboot plugin) {
        Util.plugin = plugin;
    }

    public static boolean inventoryContainsSeeds(@NotNull PlayerInventory playerInventory, @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());

        boolean containsSeed = playerInventory.contains(crop.getSeed());
        if(FarmAssistConfig.IGNORE_RENAMED && containsSeed) {
            int spot = playerInventory.first(crop.getSeed());
            final ItemStack itemStack = playerInventory.getItem(spot);
            if(itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().hasDisplayName())
                return false;
        }

        return containsSeed;
    }

    public static boolean isWorldEnabled(@NotNull World world) {
        debug(Debug.Worlds.CONFIG_PER_WORLD, FarmAssistConfig.ENABLED_PER_WORLD);
        if (!FarmAssistConfig.ENABLED_PER_WORLD)
            return true;

        debug(Debug.Worlds.IS_WORLD_ENABLED, world.getName(), FarmAssistConfig.ENABLED_WORLDS.contains(world));
        return FarmAssistConfig.ENABLED_WORLDS.contains(world);
    }

    public static void replant(@NotNull Player player, Block block, @NotNull Material material) {
        Crop crop = Crop.valueOf(material.name());
        int spot = player.getInventory().first(crop.getSeed());
        debug("Spot:" + spot);
        if (spot >= 0) {
            removeOrSubtractItem(player, spot);
            new ReplantTask(block, plugin).runTaskLater(plugin, 5L);
        }
    }

    public static void removeOrSubtractItem(@NotNull Player player, int spot) {
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

    public static void sendMessage(final @NotNull CommandSender sender,final String message) {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            sender.sendMessage(color(PlaceholderAPI.setPlaceholders(!(sender instanceof Player) ? null : (Player) sender, message)));
            return;
        }
        sender.sendMessage(color(message));
    }

    public static String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
