package io.github.sarhatabaot;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sarhatabaot
 */

public class Config {
    private static boolean usePermissions = true;
    private static boolean wheat = true;
    private static boolean wheatRipe = false;
    private static boolean wheatOnTill = true;
    private static boolean sugarCane = true;
    private static boolean netherWart = true;
    private static boolean netherWartRipe = false;
    private static boolean cocoaBeans = true;
    private static boolean cocoaRipe = false;
    private static boolean perWorld = false;
    private static List<String> worlds = new ArrayList<>();
    private static boolean carrots = true;
    private static boolean carrotsRipe = false;
    private static boolean potatoes = true;
    private static boolean potatoesRipe = false;
    private static boolean pumpkin = true;
    private static boolean pumpkinRipe = false;
    private static boolean melon = true;
    private static boolean melonRipe = false;
    private static boolean beetroot = true;
    private static boolean beetrootRipe = false;

    private static boolean checkForUpdates = true;
    private static boolean debug = false;

    public Config() {
    }

    public static void initConfig(File dataFolder) throws IOException {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File configFile = new File(dataFolder, "config.yml");
        if(!configFile.exists()){
            configFile.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(configFile));
            out.write("# FarmAssist Configuration File");
            out.newLine();
            writeUsePermissions(out);
            writeCrops(out);
            writeWorld(out);
            writeCheckForUpdates(out);
            writeDebug(out);
            out.close();
        }
    }
    private static void loadConfig(File configFile) throws IOException{
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        BufferedWriter out = new BufferedWriter(new FileWriter(configFile, true));
        if(config.contains("Wheat.Enabled")){
            Config.setWheat(config.getBoolean("Wheat.Enabled"));
        }
        if(config.contains("Wheat.Only Replant When Fully Grown")){
            Config.setWheatRipe(config.getBoolean("Wheat.Only Replant When Fully Grown"));
        }
        if(config.contains("Wheat.Plant on till")){
            Config.setWheatOnTill(config.getBoolean("Wheat.Plant on till"));
        }
        if(config.contains("Wheat.Enabled")){
            Config.setWheat(config.getBoolean("Wheat.Enabled"));
        }
        if(config.contains("Reeds.Enabled")){
            Config.setSugarCane(config.getBoolean("Reeds.Enabled"));
        }
        if(config.contains("Netherwart.Enabled")){
            Config.setNetherWart(config.getBoolean("Netherwart.Enabled"));
        }
        if(config.contains("Netherwart.Only Replant When Fully Grown")){
            Config.setNetherWartRipe(config.getBoolean("Netherwart.Only Replant When Fully Grown"));
        }
        if(config.contains("Cocoa Beans.Enabled")){
            Config.setCocoaBeans(config.getBoolean("Cocoa Beans.Enabled"));
        }
        if(config.contains("Cocoa Beans.Only Replant When Fully Grown")){
            Config.setCocoaRipe(config.getBoolean("Cocoa Beans.Only Replant When Fully Grown"));
        }
        if(config.contains("Carrots.Enabled")){
            Config.setCarrots(config.getBoolean("Carrots.Enabled"));
        }
        if(config.contains("Carrots.Only Replant When Fully Grown")){
            Config.setCarrotsRipe(config.getBoolean("Carrots.Only Replant When Fully Grown"));
        }
        if(config.contains("Potatoes.Enabled")){
            Config.setPotatoes(config.getBoolean("Potatoes.Enabled"));
        }
        if(config.contains("Potatoes.Only Replant When Fully Grown")){
            Config.setPotatoesRipe(config.getBoolean("Potatoes.Only Replant When Fully Grown"));
        }
        if(config.contains("Pumpkin.Enabled")){
            Config.setPumpkin(config.getBoolean("Pumpkin.Enabled"));
        }
        if(config.contains("Pumpkin.Only Replant When Fully Grown")){
            Config.setPumpkinRipe(config.getBoolean("Pumpkin.Only Replant When Fully Grown"));
        }
        if(config.contains("Melon.Enabled")){
            Config.setMelon(config.getBoolean("Melon.Enabled"));
        }
        if(config.contains("Melon.Only Replant When Fully Grown")){
            Config.setMelonRipe(config.getBoolean("Melon.Only Replant When Fully Grown"));
        }
        if(config.contains("Beetroot.Enabled")){
            Config.setBeetroot(config.getBoolean("Beetroot.Enabled"));
        }
        if(config.contains("Beetroot.Only Replant When Fully Grown")){
            Config.setBeetrootRipe(config.getBoolean("Beetroot.Only Replant When Fully Grown"));
        }
        if(config.contains("World.Enable Per World")){
            Config.setPerWorld(config.getBoolean("World.Enable Per World"));
        }
        /* Figure it out
        if(config.contains("Worlds.Enabled Worlds")){
            Config.setWorlds(config.getList("Worlds.Enabled Worlds"));
        }*/
        if(config.contains("Main.Use Permission")){

        } else {
            writeUsePermissions(out);
        }
        if(config.contains("Check for updates")){

        } else {
            writeCheckForUpdates(out);
        }
        if (config.contains("debug")){

        } else {
            writeDebug(out);
        }
        out.close();
    }
    private static void writeCrops(BufferedWriter out) throws IOException {
        out.write("Wheat.Enabled: true");
        out.newLine();
        out.write("Wheat.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Wheat.Plant on till: true");
        out.newLine();
        out.write("Reeds.Enabled: true");
        out.newLine();
        out.write("Netherwart.Enabled: true");
        out.newLine();
        out.write("Netherwart.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Cocoa Beans.Enabled: true");
        out.newLine();
        out.write("Cocoa Beans.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Carrots.Enabled: true");
        out.newLine();
        out.write("Carrots.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Potatoes.Enabled: true");
        out.newLine();
        out.write("Potatoes.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Pumpkin.Enabled: true");
        out.newLine();
        out.write("Pumpkin.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Melon.Enabled: true");
        out.newLine();
        out.write("Melon.Only Replant When Fully Grown: false");
        out.newLine();
        out.write("Beetroot.Enabled: true");
        out.newLine();
        out.write("Beetroot.Only Replant When Fully Grown");
        out.newLine();
    }
    private static void writeWorld(BufferedWriter out) throws IOException{
        out.write("# Enable per world");
        out.newLine();
        out.write("World.Enable Per World: true");
        out.newLine();
        out.write("# Enabled Worlds:");
        out.newLine();
        out.write("#    - ExampleWorld");
        out.newLine();
        out.write("Worlds.Enabled Worlds: ");
        out.newLine();
    }
    private static void writeUsePermissions(BufferedWriter out) throws IOException {
        out.write("# Use permissions or just config.yml");
        out.newLine();
        out.write("Main.Use Permissions: true");
        out.newLine();
    }

    private static void writeCheckForUpdates(BufferedWriter out) throws IOException {
        out.write("# Check for updates");
        out.newLine();
        out.write("Check for updates: true");
        out.newLine();
    }

    private static void writeDebug(BufferedWriter out) throws IOException{
        out.write("# For debugging only");
        out.newLine();
        out.write("debug: false");
        out.newLine();
    }

    public static boolean isBeetroot() {
        return beetroot;
    }

    public static void setBeetroot(boolean beetroot) {
        Config.beetroot = beetroot;
    }

    public static boolean isBeetrootRipe() {
        return beetrootRipe;
    }

    public static void setBeetrootRipe(boolean beetrootRipe) {
        Config.beetrootRipe = beetrootRipe;
    }

    public static boolean isUsePermissions() {
        return usePermissions;
    }

    public static void setUsePermissions(boolean usePermissions) {
        Config.usePermissions = usePermissions;
    }

    public static boolean isWheat() {
        return wheat;
    }

    public static void setWheat(boolean wheat) {
        Config.wheat = wheat;
    }

    public static boolean isWheatRipe() {
        return wheatRipe;
    }

    public static void setWheatRipe(boolean wheatRipe) {
        Config.wheatRipe = wheatRipe;
    }

    public static boolean isWheatOnTill() {
        return wheatOnTill;
    }

    public static void setWheatOnTill(boolean wheatOnTill) {
        Config.wheatOnTill = wheatOnTill;
    }

    public static boolean isSugarCane() {
        return sugarCane;
    }

    public static void setSugarCane(boolean sugarCane) {
        Config.sugarCane = sugarCane;
    }

    public static boolean isNetherWart() {
        return netherWart;
    }

    public static void setNetherWart(boolean netherWart) {
        Config.netherWart = netherWart;
    }

    public static boolean isNetherWartRipe() {
        return netherWartRipe;
    }

    public static void setNetherWartRipe(boolean netherWartRipe) {
        Config.netherWartRipe = netherWartRipe;
    }

    public static boolean isCocoaBeans() {
        return cocoaBeans;
    }

    public static void setCocoaBeans(boolean cocoaBeans) {
        Config.cocoaBeans = cocoaBeans;
    }

    public static boolean isCocoaRipe() {
        return cocoaRipe;
    }

    public static void setCocoaRipe(boolean cocoaRipe) {
        Config.cocoaRipe = cocoaRipe;
    }

    public static boolean isPerWorld() {
        return perWorld;
    }

    public static void setPerWorld(boolean perWorld) {
        Config.perWorld = perWorld;
    }

    public static List<String> getWorlds() {
        return worlds;
    }

    public static void setWorlds(List<String> worlds) {
        Config.worlds = worlds;
    }

    public static boolean isCarrots() {
        return carrots;
    }

    public static void setCarrots(boolean carrots) {
        Config.carrots = carrots;
    }

    public static boolean isCarrotsRipe() {
        return carrotsRipe;
    }

    public static void setCarrotsRipe(boolean carrotsRipe) {
        Config.carrotsRipe = carrotsRipe;
    }

    public static boolean isPotatoes() {
        return potatoes;
    }

    public static void setPotatoes(boolean potatoes) {
        Config.potatoes = potatoes;
    }

    public static boolean isPotatoesRipe() {
        return potatoesRipe;
    }

    public static void setPotatoesRipe(boolean potatoesRipe) {
        Config.potatoesRipe = potatoesRipe;
    }

    public static boolean isPumpkin() {
        return pumpkin;
    }

    public static void setPumpkin(boolean pumpkin) {
        Config.pumpkin = pumpkin;
    }

    public static boolean isPumpkinRipe() {
        return pumpkinRipe;
    }

    public static void setPumpkinRipe(boolean pumpkinRipe) {
        Config.pumpkinRipe = pumpkinRipe;
    }

    public static boolean isMelon() {
        return melon;
    }

    public static void setMelon(boolean melon) {
        Config.melon = melon;
    }

    public static boolean isMelonRipe() {
        return melonRipe;
    }

    public static void setMelonRipe(boolean melonRipe) {
        Config.melonRipe = melonRipe;
    }

    public static boolean isCheckForUpdates() {
        return checkForUpdates;
    }

    public static void setCheckForUpdates(boolean checkForUpdates) {
        Config.checkForUpdates = checkForUpdates;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Config.debug = debug;
    }
}
