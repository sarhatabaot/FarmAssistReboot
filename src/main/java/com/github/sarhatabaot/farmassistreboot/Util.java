package com.github.sarhatabaot.farmassistreboot;

import com.github.sarhatabaot.farmassistreboot.config.FarmAssistConfig;
import com.github.sarhatabaot.farmassistreboot.messages.Debug;
import com.github.sarhatabaot.farmassistreboot.tasks.ReplantTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class Util {
    private static FarmAssistReboot plugin;

    private Util() {
        throw new UnsupportedOperationException();
    }

    public static void init(final FarmAssistReboot plugin){
        Util.plugin= plugin;
    }

    public static boolean inventoryContainsSeeds(@NotNull PlayerInventory playerInventory, @NotNull Material material){
        Crop crop = Crop.valueOf(material.name());
        return playerInventory.contains(crop.getSeed());
    }

    public static boolean isWorldEnabled(@NotNull World world) {
        debug(Debug.Worlds.CONFIG_PER_WORLD,FarmAssistConfig.ENABLED_PER_WORLD);
        if(!FarmAssistConfig.ENABLED_PER_WORLD)
            return true;

        debug(Debug.Worlds.IS_WORLD_ENABLED,world.getName(),FarmAssistConfig.ENABLED_WORLDS.contains(world));
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

    private static void debug(final String message,Object... args) {
        debug(String.format(message, args));
    }

}
