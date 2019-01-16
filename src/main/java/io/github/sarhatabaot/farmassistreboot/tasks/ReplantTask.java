package io.github.sarhatabaot.farmassistreboot.tasks;

import io.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.inventory.ItemStack;

public class ReplantTask implements Runnable {
    private FarmAssistReboot plugin;
    private Block block;
    private Material material;

    private Cocoa cocoa;

    public ReplantTask(FarmAssistReboot plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
        this.material = block.getType();

        //case cocoa
        if(block.getType() == Material.COCOA){
            this.cocoa = (Cocoa) block.getBlockData().clone();
            this.cocoa.setAge(0);
        }
    }


    @Override
    public void run() {
        FarmAssistReboot.debug("Run:"+block.getType().name()+"Material:"+material.name());
        switch (material){
            case WHEAT:{
                if (relativeBlockDown(Material.FARMLAND)
                        && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.WHEAT);
                    setCropState();
                } else{
                    this.block.getDrops();
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.WHEAT));
                }
                break;
            }
            case CARROTS:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.CARROTS);
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.CARROT));
                }
                break;
            }
            case POTATOES:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.POTATOES);
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.POTATO));
                }
                break;
            }
            case SUGAR_CANE:{
                if ((this.block.getRelative(BlockFace.DOWN).getType() == Material.GRASS
                        || this.block.getRelative(BlockFace.DOWN).getType() == Material.DIRT
                        || this.block.getRelative(BlockFace.DOWN).getType() == Material.SAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.SUGAR_CANE);
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.SUGAR_CANE));
                }
                break;
            }
            case NETHER_WART:{
                if (this.block.getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND
                        && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.NETHER_WART);
                    setCropState();
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.NETHER_WART));
                }
                break;
            }
            case COCOA:{
                if (this.block.getType() == Material.AIR) {
                    if(this.block.getRelative(cocoa.getFacing()).getType() == Material.JUNGLE_LOG){
                        this.block.setType(Material.COCOA);
                        this.block.setBlockData(cocoa);
                    }
                    else {
                        this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
                    }
                }
                break;
            }
            case PUMPKIN_STEM:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.PUMPKIN_STEM);
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.PUMPKIN_SEEDS));
                }
                break;
            }
            case MELON_STEM:{
                if (relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.MELON_STEM);
                } else{
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.MELON_SEEDS));
                }
                break;
            }
            case BEETROOTS: {
                if(relativeBlockDown(Material.FARMLAND) && this.block.getType() == Material.AIR) {
                    this.block.setType(Material.BEETROOTS);
                } else {
                    this.block.getWorld().dropItemNaturally(block.getLocation(),new ItemStack(Material.BEETROOT_SEEDS));
                }
                break;
            }
            case FARMLAND: {
                if(this.block.getRelative(BlockFace.UP).getType() == Material.AIR)
                    this.block.getRelative(BlockFace.UP).setType(Material.WHEAT);
                break;
            }
            default:{
                FarmAssistReboot.debug("Error while getting material."+material.name());
                break;
            }
        }
    }

    private boolean relativeBlockDown(Material material){
        return this.block.getRelative(BlockFace.DOWN).getType() == material;
    }

    private void setCropState(){
        Ageable age = (Ageable)this.block.getBlockData();
        age.setAge(0);
        this.block.setBlockData(age);
    }
}
