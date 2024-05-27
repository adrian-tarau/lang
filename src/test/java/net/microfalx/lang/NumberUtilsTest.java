package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
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
        assertNull(NumberUtils.toNumber(null, null));
        assertEquals(1, NumberUtils.toNumber(null, 1));
        assertEquals(1, NumberUtils.toNumber(1, 2));
        assertEquals(1.5d, NumberUtils.toNumber(1.5, 1));
        assertEquals(1.5d, NumberUtils.toNumber("1.5", 1));
        assertEquals(100,NumberUtils.toNumber("number",100));
    }

    @Test
    void throughputWithTemporal(){
        assertEquals(Double.POSITIVE_INFINITY,NumberUtils.throughput(5000, LocalTime.MIDNIGHT));
    }

    @Test
    void throughputWithTemporalAndDefaultValue(){
        assertEquals(10d, NumberUtils.throughput(null, (Temporal) null, 10d));
    }

    @Test
    void throughputWithDuration(){
        assertEquals(6.365740409819409E-5, NumberUtils.throughput(11,Duration.ofDays(2L)));
    }

    @Test
    void throughputWithDurationAndDefaultValue() {
        assertNull(NumberUtils.throughput(null, (Duration) null, null));
        assertEquals(10d, NumberUtils.throughput(null, Duration.ofMillis(100), 10d));
        assertEquals(10d, NumberUtils.throughput(100, (Duration) null, 10d));
        assertEquals(813.008, NumberUtils.throughput(100, Duration.ofMillis(123), 10d), 0.001d);
    }

    @Test
    void isBetweenInclusive(){
        assertTrue(NumberUtils.isBetweenInclusive(5,5,10));
        assertTrue(NumberUtils.isBetweenInclusive(10,5,10));
        assertFalse(NumberUtils.isBetweenInclusive(4,5,10));
        assertFalse(NumberUtils.isBetweenInclusive(11,5,10));
    }

    @Test
    void isBetweenExclusive(){
        assertTrue(NumberUtils.isBetweenExclusive(5,5,10));
        assertFalse(NumberUtils.isBetweenExclusive(10,5,10));
        assertFalse(NumberUtils.isBetweenExclusive(4,5,10));
        assertFalse(NumberUtils.isBetweenExclusive(11,5,10));
    }

}