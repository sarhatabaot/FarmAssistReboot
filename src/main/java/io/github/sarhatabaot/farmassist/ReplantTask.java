package io.github.sarhatabaot.farmassist;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.inventory.ItemStack;

import static io.github.sarhatabaot.farmassist.FarmAssist.debug;

public class ReplantTask implements Runnable {
    private Block block;
    private Material material;

    private Cocoa cocoa;

    public ReplantTask(Block block) {
        this.block = block;
        this.material = block.getType();

        if (block.getType() == Material.COCOA) {
            this.cocoa = (Cocoa) block.getBlockData().clone();
            this.cocoa.setAge(0);
        }
    }


    @Override
    public void run() {
        debug("Block: " + block.getType().name() + ",Material: " + material.name());
        replantByMaterial();
    }

    private void replantByMaterial() {
        switch (material) {
            case WHEAT:
                debug("Planting wheat...");
                replantWheat();
                break;
            case CARROTS:
                debug("Planting carrot...");
                replantCarrots();
                break;
            case POTATOES:
                debug("Planting potato...");
                replantPotatoes();
                break;
            case CACTUS:
                debug("Planting cactus...");
                replantCactus();
                break;
            case SUGAR_CANE:
                debug("Planting sugar cane...");
                replantSugarCane();
                break;
            case NETHER_WART:
                debug("Planting nether wart...");
                replantNetherWart();
                break;
            case COCOA:
                debug("Planting cocoa...");
                replantCocoa();
                break;
            case ATTACHED_PUMPKIN_STEM:
            case PUMPKIN_STEM:
                debug("Planting pumpkin...");
                replantPumpkin();
                break;
            case ATTACHED_MELON_STEM:
            case MELON_STEM:
                debug("Planting melon...");
                replantMelon();
                break;
            case BEETROOTS:
                debug("Planting beetroot...");
                replantBeetroots();
                break;
            case FARMLAND:
                debug("Planting wheat...");
                replantWheatFarmland();
                break;
            case SWEET_BERRY_BUSH:
                debug("Planting sweet berries...");
                replantSweetBerries();
                break;
            default:
                debug("Error while getting material." + material.name());
                break;

        }
    }
    private void replantSweetBerries(){
        if(isBottomBlock(Material.DIRT, Material.COARSE_DIRT, Material.GRASS_BLOCK)){
            this.block.setType(Material.SWEET_BERRY_BUSH);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SWEET_BERRIES));
        }
    }

    private void replantWheat() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.WHEAT);
            this.block.setBlockData(setCropAge());
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WHEAT));
        }
    }

    private void replantCarrots() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.CARROTS);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.CARROT));
        }
    }

    private void replantPotatoes() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.POTATOES);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.POTATO));
        }
    }

    private void replantCactus() {
        if (isBottomBlock(Material.SAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.CACTUS);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.CACTUS));
        }
    }

    private void replantSugarCane() {
        if ((isBottomBlock(Material.GRASS, Material.DIRT, Material.SAND))
                && this.block.getType() == Material.AIR) {
            this.block.setType(Material.SUGAR_CANE);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SUGAR_CANE));
        }
    }

    private void replantNetherWart() {
        if (isBottomBlock(Material.SOUL_SAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.NETHER_WART);
            this.block.setBlockData(setCropAge());
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHER_WART));
        }
    }

    private void replantCocoa() {
        if (this.block.getType() == Material.AIR) {
            if (this.block.getRelative(cocoa.getFacing()).getType() == Material.JUNGLE_LOG) {
                this.block.setType(Material.COCOA);
                this.block.setBlockData(cocoa);
            } else {
                this.block.getWorld().dropItemNaturally(this.block.getLocation(), new ItemStack(Material.COCOA_BEANS));
            }
        }
    }

    private void replantPumpkin() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.PUMPKIN_STEM);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.PUMPKIN_SEEDS));
        }
    }

    private void replantMelon() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.MELON_STEM);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.MELON_SEEDS));
        }
    }

    private void replantBeetroots() {
        if (isBottomBlock(Material.FARMLAND) && this.block.getType() == Material.AIR) {
            this.block.setType(Material.BEETROOTS);
        } else {
            this.block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BEETROOT_SEEDS));
        }
    }

    private void replantWheatFarmland() {
        if (this.block.getRelative(BlockFace.UP).getType() == Material.AIR)
            this.block.getRelative(BlockFace.UP).setType(Material.WHEAT);
    }

    private boolean isBottomBlock(Material material) {
        return this.block.getRelative(BlockFace.DOWN).getType() == material;
    }

    private boolean isBottomBlock(Material... materials){
        for(Material material:materials){
            if(this.block.getRelative(BlockFace.DOWN).getType() == material)
                return true;
        }
        return false;
    }

    private BlockData setCropAge() {
        Ageable age = (Ageable) this.block.getBlockData();
        age.setAge(0);
        return age;
    }
}
