package io.github.sarhatabaot;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sarhatabaot
 */

public class ReplantTask extends BukkitRunnable {
    private FarmAssist plugin;
    private Block block;
    private Material material;

    public ReplantTask(FarmAssist plugin, Block block, Material type) {
        this.plugin = plugin;
        this.block = block;
        this.material = type;
    }

    public ReplantTask(FarmAssist plugin,Block block){
        this.plugin = plugin;
        this.block = block;
        this.material = null;
    }

    @Override
    public void run() {
        if(material == null){
            onTilt();
            return;
        }
        switch (material){
            case WHEAT_SEEDS:{
                if (relativeBlockDown(Material.FARMLAND)
                        && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.WHEAT);
                    setCropState();
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.WHEAT_SEEDS));
                }
            }
            case SUGAR_CANE:{
                if ((this.block.getRelative(BlockFace.DOWN).getType() == Material.GRASS
                        || this.block.getRelative(BlockFace.DOWN).getType() == Material.DIRT
                        || this.block.getRelative(BlockFace.DOWN).getType() == Material.SAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.SUGAR_CANE);
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.SUGAR_CANE));
                }
            }
            case NETHER_WART:{
                if (this.block.getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND
                        && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.NETHER_WART);
                    setCropState();
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.NETHER_WART));
                }
            }
            case COCOA_BEANS:{
                if (this.block.getType() == Material.AIR) {
                    //Material.JUNGLE_LOG
                    if (this.block.getRelative(BlockFace.NORTH).getType() == Material.JUNGLE_LOG) {
                        this.block.setType(Material.COCOA);
                        setCropState();
                        setCocoaDirection(BlockFace.NORTH);
                    } else if (this.block.getRelative(BlockFace.SOUTH).getType() == Material.JUNGLE_LOG) {
                        setCropState();
                        setCocoaDirection(BlockFace.SOUTH);
                    } else if (this.block.getRelative(BlockFace.EAST).getType() == Material.JUNGLE_LOG) {
                        setCropState();
                        setCocoaDirection(BlockFace.EAST);
                    } else if (this.block.getRelative(BlockFace.WEST).getType() == Material.JUNGLE_LOG) {
                        setCropState();
                        setCocoaDirection(BlockFace.WEST);
                    } else {
                        this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
                    }
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
                }
            }
            case CARROT:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.CARROTS);
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.CARROT));
                }
            }
            case POTATO:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.POTATOES);
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.POTATO));
                }
            }
            case PUMPKIN:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.PUMPKIN_STEM);
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.PUMPKIN_SEEDS));
                }
            }
            case MELON:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.MELON_STEM);
                } else {
                    this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.MELON_SEEDS));
                }
            }
            default:{
                plugin.logger.warning("Error while getting material.");
                break;
            }
        }
    }

    private void onTilt(){
        if (this.block.getRelative(BlockFace.UP).getType() == Material.AIR && this.block.getType() == Material.FARMLAND) {
            this.block.getRelative(BlockFace.UP).setType(Material.FARMLAND);
        } else {
            this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.WHEAT_SEEDS));
        }
    }

    private boolean relativeBlockDown(Material material){
        return this.block.getRelative(BlockFace.DOWN).getType() == material;
    }

    private void setCocoaDirection(BlockFace face){
        Cocoa cocoa = (Cocoa) this.block.getBlockData();
        cocoa.setFacing(face);
    }

    private void setCropState(){
        Ageable age = (Ageable)this.block.getBlockData();
        age.setAge(0);
        this.block.setBlockData(age);
    }
}
