package com.github.sarhatabaot.farmassistreboot.tasks;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReplantTaskTest {

    @Test
    void plantsWheatInAirAboveFarmland() {
        FarmAssistReboot plugin = mock(FarmAssistReboot.class);
        Block target = mock(Block.class);
        Block farmland = mock(Block.class);
        Ageable wheatData = mock(Ageable.class);

        when(target.getType()).thenReturn(Material.AIR);
        when(target.getRelative(BlockFace.DOWN)).thenReturn(farmland);
        when(farmland.getType()).thenReturn(Material.FARMLAND);
        when(target.getBlockData()).thenReturn(wheatData);

        new ReplantTask(target, plugin, Material.WHEAT).run();

        verify(target).setType(Material.WHEAT);
        verify(wheatData).setAge(0);
        verify(target).setBlockData(wheatData);
    }
}
