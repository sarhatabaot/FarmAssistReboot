package com.github.sarhatabaot.farmassistreboot;

import dev.watchwolf.entities.Position;
import dev.watchwolf.entities.items.Item;
import dev.watchwolf.entities.items.ItemType;
import dev.watchwolf.tester.AbstractTest;
import dev.watchwolf.tester.ExtendedClientPetition;
import dev.watchwolf.tester.TesterConnector;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(FarmAssistWatchWolfTest.class)
public class FarmAssistWatchWolfTest extends AbstractTest {
    @Override
    public String getConfigFile() {
        return "src/testWatchWolf/resources/watchwolf.yaml";
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void pluginLoadsAndInfoCommandResponds(TesterConnector connector) throws IOException {
        String output = connector.server.runCommand("farmassist info");
        assertTrue(output.contains("FarmAssistReboot version:"), () -> "Unexpected command output: " + output);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void globalToggleTurnsOffAndBackOn(TesterConnector connector) throws IOException {
        String disabled = connector.server.runCommand("farmassist global");
        assertTrue(disabled.contains("functions turned off globally!"),
                () -> "Unexpected command output: " + disabled);

        String enabled = connector.server.runCommand("farmassist global");
        assertTrue(enabled.contains("functions are globally back on!"),
                () -> "Unexpected command output: " + enabled);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void commandAliasesRespond(TesterConnector connector) throws IOException {
        String baseAlias = connector.server.runCommand("fa info");
        assertTrue(baseAlias.contains("FarmAssistReboot version:"),
                () -> "Unexpected command output: " + baseAlias);

        String subcommandAlias = connector.server.runCommand("farmassist about");
        assertTrue(subcommandAlias.contains("FarmAssistReboot version:"),
                () -> "Unexpected command output: " + subcommandAlias);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void unsupportedLanguageIsRejected(TesterConnector connector) throws IOException {
        String output = connector.server.runCommand("farmassist lang zz_invalid");
        assertTrue(output.contains("Unsupported locale: zz_invalid"),
                () -> "Unexpected command output: " + output);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void breakingWheatReplantsAndConsumesOneSeed(TesterConnector connector) throws Exception {
        assertAgeableCropReplants(connector, "wheat", "farmland", 7, ItemType.WHEAT_SEEDS);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void breakingCarrotsReplantsAndConsumesOneCarrot(TesterConnector connector) throws Exception {
        assertAgeableCropReplants(connector, "carrots", "farmland", 7, ItemType.CARROT);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void breakingPotatoesReplantsAndConsumesOnePotato(TesterConnector connector) throws Exception {
        assertAgeableCropReplants(connector, "potatoes", "farmland", 7, ItemType.POTATO);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void breakingBeetrootsReplantsAndConsumesOneSeed(TesterConnector connector) throws Exception {
        assertAgeableCropReplants(connector, "beetroots", "farmland", 3, ItemType.BEETROOT_SEEDS);
    }

    @ParameterizedTest
    @ArgumentsSource(FarmAssistWatchWolfTest.class)
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    void breakingNetherWartReplantsAndConsumesOneWart(TesterConnector connector) throws Exception {
        assertAgeableCropReplants(connector, "nether_wart", "soul_sand", 3, ItemType.NETHER_WART);
    }

    private static void assertAgeableCropReplants(TesterConnector connector, String cropType,
                                                   String supportType, int matureAge, ItemType seed) throws Exception {
        String playerName = connector.getClients()[0];
        ExtendedClientPetition client = connector.getClientPetition(playerName);
        Position crop = client.getPosition().getBlockPosition().add(2, 0, 0);
        Position farmland = crop.add(0, -1, 0);

        connector.server.opPlayer(playerName);
        connector.server.runCommand("gamemode survival " + playerName);
        connector.server.runCommand("gamerule doTileDrops false");
        connector.server.runCommand("gamerule randomTickSpeed 0");
        try {
            connector.server.runCommand("clear " + playerName);
            setBlock(connector, farmland, supportType);
            setBlock(connector, crop, cropType + "[age=" + matureAge + "]");
            connector.server.giveItem(playerName, new Item(seed));

            String matureCheck = checkBlock(connector, crop, cropType + "[age=" + matureAge + "]");
            assertTrue(matureCheck.contains("Test passed"),
                    () -> "Expected mature " + cropType + " before breaking; command output: " + matureCheck);
            assertEquals(1, seedCount(connector, playerName, seed));

            client.breakBlock(crop);

            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
            while (!isBlock(connector, crop, cropType + "[age=0]") && System.nanoTime() < deadline) {
                Thread.sleep(100);
            }

            String replantedCheck = checkBlock(connector, crop, cropType + "[age=0]");
            assertTrue(replantedCheck.contains("Test passed"),
                    () -> "Expected newly replanted " + cropType + "; command output: " + replantedCheck);
            assertEquals(0, seedCount(connector, playerName, seed),
                    "Replanting " + cropType + " should consume one seed");
        } finally {
            connector.server.runCommand("gamerule doTileDrops true");
            connector.server.runCommand("gamerule randomTickSpeed 3");
        }
    }

    private static void setBlock(TesterConnector connector, Position position, String block) throws IOException {
        connector.server.runCommand("setblock " + position.getBlockX() + " " + position.getBlockY() + " "
                + position.getBlockZ() + " minecraft:" + block);
    }

    private static boolean isBlock(TesterConnector connector, Position position, String block) throws IOException {
        return checkBlock(connector, position, block).contains("Test passed");
    }

    private static String checkBlock(TesterConnector connector, Position position, String block) throws IOException {
        return connector.server.runCommand("execute if block " + position.getBlockX() + " "
                + position.getBlockY() + " " + position.getBlockZ() + " minecraft:" + block);
    }

    private static int seedCount(TesterConnector connector, String playerName, ItemType seed) throws IOException {
        int count = 0;
        for (Item item : connector.server.getInventory(playerName).getItems()) {
            if (item != null && item.getType() == seed) {
                count += item.getAmount();
            }
        }
        return count;
    }
}
