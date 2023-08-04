package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterUtilsTest {

    @Test
    void formatNumber() {
        assertEquals("0",FormatterUtils.formatNumber(0));
        assertEquals("10",FormatterUtils.formatNumber(10));
        assertEquals("1,234",FormatterUtils.formatNumber(1234));
        assertEquals("12.3k",FormatterUtils.formatNumber(12345));
        assertEquals("123.5k",FormatterUtils.formatNumber(123456));
        assertEquals("1,234.6k",FormatterUtils.formatNumber(1234567));
        assertEquals("12.3m",FormatterUtils.formatNumber(12345678));
        assertEquals("123.5m",FormatterUtils.formatNumber(123456789));
        assertEquals("1,234.6m",FormatterUtils.formatNumber(1234567890));
        assertEquals("12.3b",FormatterUtils.formatNumber(12345678900L));
        assertEquals("123.5b",FormatterUtils.formatNumber(123456789000L));
    }

}