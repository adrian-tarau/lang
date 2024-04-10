package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterUtilsTest {

    @Test
    void formatNumber() {
        assertEquals("0", FormatterUtils.formatNumber(0));
        assertEquals("10", FormatterUtils.formatNumber(10));
        assertEquals("1,234", FormatterUtils.formatNumber(1234));
        assertEquals("12.3k", FormatterUtils.formatNumber(12345));
        assertEquals("123.5k", FormatterUtils.formatNumber(123456));
        assertEquals("1,234.6k", FormatterUtils.formatNumber(1234567));
        assertEquals("12.3m", FormatterUtils.formatNumber(12345678));
        assertEquals("123.5m", FormatterUtils.formatNumber(123456789));
        assertEquals("1,234.6m", FormatterUtils.formatNumber(1234567890));
        assertEquals("12.3b", FormatterUtils.formatNumber(12345678900L));
        assertEquals("123.5b", FormatterUtils.formatNumber(123456789000L));
    }

    @Test
    void formatDuration() {
        assertEquals("N/A", FormatterUtils.formatDuration(-1));
        assertEquals("N/A", FormatterUtils.formatDuration(Duration.ofMillis(-1)));

        assertEquals("0ms", FormatterUtils.formatDuration(0));
        assertEquals("0ms", FormatterUtils.formatDuration(Duration.ZERO));

        assertEquals("2μs", FormatterUtils.formatDuration(0.002f));
        assertEquals("2μs", FormatterUtils.formatDuration(Duration.ofNanos(2000)));

        assertEquals("200ns", FormatterUtils.formatDuration(0.0002f));
        assertEquals("200ns", FormatterUtils.formatDuration(Duration.ofNanos(200)));

        assertEquals("5ms", FormatterUtils.formatDuration(5));
        assertEquals("5ms", FormatterUtils.formatDuration(Duration.ofMillis(5)));

        assertEquals("530ms", FormatterUtils.formatDuration(530));
        assertEquals("250ms", FormatterUtils.formatDuration(Duration.ofMillis(250)));

        assertEquals("7s 345ms", FormatterUtils.formatDuration(7345));
        assertEquals("9s 345ms", FormatterUtils.formatDuration(Duration.ofMillis(9345)));

        assertEquals("1m 13s", FormatterUtils.formatDuration(73450));
        assertEquals("45s", FormatterUtils.formatDuration(Duration.ofSeconds(45)));

        assertEquals("12m 14s", FormatterUtils.formatDuration(734500));
        assertEquals("7m 30s", FormatterUtils.formatDuration(Duration.ofSeconds(450)));
    }

    @Test
    void formatBytes() {
        assertEquals("0B", FormatterUtils.formatBytes(0));
        assertEquals("10B", FormatterUtils.formatBytes(10));
        assertEquals("1,234B", FormatterUtils.formatBytes(1234));
        assertEquals("12KB", FormatterUtils.formatBytes(12345));
        assertEquals("123KB", FormatterUtils.formatBytes(123456));
        assertEquals("1,235KB", FormatterUtils.formatBytes(1234567));
        assertEquals("12MB", FormatterUtils.formatBytes(12345678));
        assertEquals("123MB", FormatterUtils.formatBytes(123456789));
        assertEquals("1,235MB", FormatterUtils.formatBytes(1234567890));
        assertEquals("12GB", FormatterUtils.formatBytes(12345678900L));
        assertEquals("123GB", FormatterUtils.formatBytes(123456789000L));
    }

    @Test
    void formatThroughput() {
        assertEquals("-", FormatterUtils.formatThroughput(null, null, null));
        assertEquals("-", FormatterUtils.formatThroughput(0, null, null));
        assertEquals("-", FormatterUtils.formatThroughput(null, Duration.ZERO, null));
        assertEquals("1,000.0r/s", FormatterUtils.formatThroughput(10, Duration.ofMillis(10), null));
        assertEquals("8,333.3t/s", FormatterUtils.formatThroughput(25, Duration.ofMillis(3), "t"));
        assertEquals("969B/s", FormatterUtils.formatThroughput(123, Duration.ofMillis(127), "b"));
        assertEquals("185KB/s", FormatterUtils.formatThroughput(23454, Duration.ofMillis(127), "b"));
        assertEquals("18MB/s", FormatterUtils.formatThroughput(2345434, Duration.ofMillis(133), "b"));

        assertEquals("1,234.0r/s", FormatterUtils.formatThroughput(1234, null));
        assertEquals("234,567.0t/s", FormatterUtils.formatThroughput(234567, "t"));
        assertEquals("34MB/s", FormatterUtils.formatThroughput(34443435, "b"));
    }

}