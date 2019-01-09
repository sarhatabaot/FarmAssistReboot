package io.github.sarhatabaot.farminassistreboot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    }

    private HashMap<String, String> loadConfigurables(HashMap<String, String> items) {
        items.put("Main.Use Permissions", "true");
        items.put("Wheat.Enabled", "true");
        items.put("Wheat.Only Replant When Fully Grown", "false");
        items.put("Wheat.Plant on till", "true");
        items.put("Reeds.Enabled", "true");
        items.put("Netherwart.Enabled", "true");
        items.put("Netherwart.Only Replant When Fully Grown", "false");
        items.put("Cocoa Beans.Enabled", "true");
        items.put("Cocoa Beans.Only Replant When Fully Grown", "false");
        items.put("Worlds.Enable Per World", "false");
        items.put("Worlds.Enabled Worlds", "LIST");
        items.put("Carrots.Enabled", "true");
        items.put("Carrots.Only Replant When Fully Grown", "false");
        items.put("Potatoes.Enabled", "true");
        items.put("Potatoes.Only Replant When Fully Grown", "false");
        items.put("Pumpkin.Enabled", "true");
        items.put("Pumpkin.Only Replant When Fully Grown", "false");
        items.put("Melon.Enabled", "true");
        items.put("Melon.Only Replant When Fully Grown", "false");
        items.put("Check for updates","true");
        items.put("debug","false");
        return items;
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
