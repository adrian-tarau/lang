package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilsTest {

    @Test
    void isInteger() {
        assertTrue(NumberUtils.isInteger(0));
        assertTrue(NumberUtils.isInteger(1));
        assertTrue(NumberUtils.isInteger(100));
        assertFalse(NumberUtils.isInteger(0.1));
        assertFalse(NumberUtils.isInteger(0.00001));
    }

    @Test
    void toNumber() {
        assertEquals(null, NumberUtils.toNumber(null, null));
        assertEquals(1, NumberUtils.toNumber(null, 1));
        assertEquals(1, NumberUtils.toNumber(1, 2));
        assertEquals(1.5d, NumberUtils.toNumber(1.5, 1));
        assertEquals(1.5d, NumberUtils.toNumber("1.5", 1));
    }

    @Test
    void throughput() {
        assertEquals(null, NumberUtils.throughput(null, (Duration) null, null));
        assertEquals(10d, NumberUtils.throughput(null, (Temporal) null, 10d));
        assertEquals(10d, NumberUtils.throughput(null, Duration.ofMillis(100), 10d));
        assertEquals(10d, NumberUtils.throughput(100, (Duration) null, 10d));
        assertEquals(813.008, NumberUtils.throughput(100, Duration.ofMillis(123), 10d), 0.001d);
    }
}