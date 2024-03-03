package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrencyUtilsTest {

    @Test
    void waitForCondition() {
        assertFalse(ConcurrencyUtils.waitForCondition(new AtomicBoolean(false), false, Duration.ofSeconds(1)));
        assertTrue(ConcurrencyUtils.waitForCondition(new AtomicBoolean(true), false, Duration.ofSeconds(1)));

        assertTrue(ConcurrencyUtils.waitForCondition(new AtomicBoolean(false), true, Duration.ofSeconds(1)));
        assertFalse(ConcurrencyUtils.waitForCondition(new AtomicBoolean(true), true, Duration.ofSeconds(1)));
    }
}