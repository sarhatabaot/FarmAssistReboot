package com.github.sarhatabaot.farmassistreboot.tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleUpdateCheckerTaskTest {

    @Test
    @DisplayName("1.4.9 vs 1.5.0 -> update (the original 150 vs 1490 bug)")
    void needsUpdate_minorVersionBump() {
        assertTrue(SimpleUpdateCheckerTask.needsUpdate("1.4.9", "1.5.0"));
    }

    @Test
    @DisplayName("1.5.0 vs 1.4.9 -> no update (current is newer on minor)")
    void needsUpdate_currentNewerOnMinor() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("1.5.0", "1.4.9"));
    }

    @Test
    @DisplayName("1.4.9.0 vs 1.4.9 -> no update (missing segment treated as 0)")
    void needsUpdate_trailingZeroSegment() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("1.4.9.0", "1.4.9"));
    }

    @Test
    @DisplayName("1.4.9 vs 1.4.9.0 -> no update (missing segment treated as 0, reversed)")
    void needsUpdate_trailingZeroSegmentReversed() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("1.4.9", "1.4.9.0"));
    }

    @Test
    @DisplayName("1.4.9 vs 1.4.10 -> update (9 < 10, not '9' < '1')")
    void needsUpdate_doubleDigitSegment() {
        assertTrue(SimpleUpdateCheckerTask.needsUpdate("1.4.9", "1.4.10"));
    }

    @Test
    @DisplayName("1.4.10 vs 1.4.9 -> no update (10 > 9)")
    void needsUpdate_doubleDigitSegmentCurrentNewer() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("1.4.10", "1.4.9"));
    }

    @Test
    @DisplayName("1.5.0 vs 1.5.0 -> no update (identical)")
    void needsUpdate_identical() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("1.5.0", "1.5.0"));
    }

    @Test
    @DisplayName("2.0.0 vs 1.99.99 -> no update (current is on a higher major) ")
    void needsUpdate_majorBeatsMinorAndPatch() {
        assertFalse(SimpleUpdateCheckerTask.needsUpdate("2.0.0", "1.99.99"));
    }

    @Test
    @DisplayName("1.99.99 vs 2.0.0 -> update (major bump wins over minor/patch)")
    void needsUpdate_majorBeatsMinorAndPatchCurrentNewer() {
        assertTrue(SimpleUpdateCheckerTask.needsUpdate("1.99.99", "2.0.0"));
    }

    @Test
    @DisplayName("1.4 vs 1.4.1 -> update (missing patch segment on current treated as 0)")
    void needsUpdate_missingPatchSegment() {
        assertTrue(SimpleUpdateCheckerTask.needsUpdate("1.4", "1.4.1"));
    }
}
