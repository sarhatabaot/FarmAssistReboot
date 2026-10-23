package com.github.sarhatabaot.farmassistreboot;

import dev.watchwolf.tester.AbstractTest;
import dev.watchwolf.tester.TesterConnector;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
}
