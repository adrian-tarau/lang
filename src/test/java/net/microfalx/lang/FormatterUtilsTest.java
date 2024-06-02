package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FormatterUtilsTest {

    @Test
    void formatDate() {
        assertEquals("07/04/2020", FormatterUtils.formatDate(LocalDate.of(2020, 7,
                4)));
    }

    @Test
    void formatDateWithTimeZone() {
        assertEquals("07/04/2020", FormatterUtils.formatDate(LocalDate.of(2020, 7,
                4), ZoneId.systemDefault()));
        assertEquals("07/04/2020", FormatterUtils.formatDate(LocalDateTime.of(2020, 7,
                4, 4, 20), ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
    }

    @Test
    void formatTime() {
        assertEquals("17:18:05", FormatterUtils.formatTime(LocalDateTime.of(2020, 4, 7,
                17, 18, 5)));
    }

    @Test
    void formatTimeWithTimeZone() {
        assertEquals("17:18:05", FormatterUtils.formatTime(LocalDateTime.of(2020, 4, 7,
                17, 18, 5), ZoneId.systemDefault()));
        assertEquals("00:18:05", FormatterUtils.formatTime(LocalDateTime.of(2020, 4, 7,
                17, 18, 5), ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
    }

    @Test
    void formatDateTime() {
        assertEquals("04/07/2020 16:01:45", FormatterUtils.formatDateTime(LocalDateTime.of(2020, 4,
                7, 16, 1, 45)));
    }

    @Test
    void formatDateTimeWithTimeZone() {
        assertEquals("04/07/2020 16:01:45", FormatterUtils.formatDateTime(LocalDateTime.of(2020, 4,
                7, 16, 1, 45), ZoneId.systemDefault()));
        assertEquals("04/07/2020 23:01:45", FormatterUtils.formatDateTime(LocalDateTime.of(2020, 4,
                7, 16, 1, 45), ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
    }

    @Test
    void formatDateTimeUTC() {
        assertEquals("04/07/2020 20:01:45", FormatterUtils.formatDateTimeUTC(LocalDateTime.of(2020,
                4, 7, 16, 1, 45)));
    }

    @Test
    void formatDateTimeWithMillis() {
        assertEquals("04/07/2020 16:01:45 000", FormatterUtils.formatDateTimeWithMillis(LocalDateTime.of(2020,
                4, 7, 16, 1, 45)));
    }

    @Test
    void formatTemporal() {
        assertEquals("01/01/1970 12:00:00", FormatterUtils.formatTemporal(ZonedDateTime.of(LocalDate.EPOCH, LocalTime.NOON,
                ZoneId.systemDefault())));
    }

    @Test
    void formatTemporalWithTimeZone() {
        assertEquals("01/01/1970", FormatterUtils.formatTemporal(LocalDate.EPOCH,
                ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
        assertEquals("04/07/2020 23:01:45", FormatterUtils.formatTemporal(LocalDateTime.of(2020,
                4, 7,
                16, 1, 45), ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
        assertEquals("N/A", FormatterUtils.formatTemporal(null,
                ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
    }

    @Test
    void formatDateTimeWithDateTimeFormatter() {
        assertEquals("2020-07-04T07:00:00+03:00[Asia/Aden]", FormatterUtils.formatDateTime(DateTimeFormatter.
                ISO_DATE_TIME, LocalDate.of(2020,
                7, 4), ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
        assertEquals("N/A", FormatterUtils.formatDateTime(
                DateTimeFormatter.ISO_DATE_TIME,
                null, ZoneId.of(ZoneId.getAvailableZoneIds().iterator().next())));
        assertEquals("2020-07-04T00:00:00-04:00[America/New_York]", FormatterUtils.formatDateTime(DateTimeFormatter.
                ISO_DATE_TIME, LocalDate.of(2020,
                7, 4),null));
    }

    @Test
    void formatThroughput(){
        assertEquals("1,234.0r/s", FormatterUtils.formatThroughput(1234, null));
        assertEquals("234,567.0t/s", FormatterUtils.formatThroughput(234567, "t"));
        assertEquals("34MB/s", FormatterUtils.formatThroughput(34443435, "b"));
    }

    @Test
    void formatThroughputWithDuration() {
        assertEquals("-", FormatterUtils.formatThroughput(null, null, null));
        assertEquals("-", FormatterUtils.formatThroughput(0, null, null));
        assertEquals("-", FormatterUtils.formatThroughput(null, Duration.ZERO, null));
        assertEquals("1,000.0r/s", FormatterUtils.formatThroughput(10, Duration.ofMillis(10), null));
        assertEquals("8,333.3t/s", FormatterUtils.formatThroughput(25, Duration.ofMillis(3), "t"));
        assertEquals("969B/s", FormatterUtils.formatThroughput(123, Duration.ofMillis(127), "b"));
        assertEquals("185KB/s", FormatterUtils.formatThroughput(23454, Duration.ofMillis(127), "b"));
        assertEquals("18MB/s", FormatterUtils.formatThroughput(2345434, Duration.ofMillis(133), "b"));
    }

    @Test
    void formatThroughputWithDurationAndDefaultValue() {
        assertNull(FormatterUtils.formatThroughput(null, null, null, null));
        assertNull(FormatterUtils.formatThroughput(0, null, "", null));
        assertEquals("2.3KB/s",FormatterUtils.formatThroughput(7,Duration.ofSeconds(3),"KB",
                "5"));
        assertEquals("7.0KB/s",FormatterUtils.formatThroughput(7,null,"KB",
                "5"));

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
    void formatDurationWithDefaultValue() {
        assertEquals("default value",FormatterUtils.formatDuration(null,"default value"));
        assertEquals("default value",FormatterUtils.formatDuration("","default value"));
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
    void formatNumberWithDecimals() {
        assertEquals("0", FormatterUtils.formatNumber(0,5));
        assertEquals("1", FormatterUtils.formatNumber(1.0,0));
        assertEquals("1,000,000.00b", FormatterUtils.formatNumber(1_000_000_000_000_000.5678945140,2));
        assertEquals("N/A", FormatterUtils.formatNumber("",2));
    }

    @Test
    void formatNumberWithDecimalsAndSuffix() {
        assertEquals("1.68m",FormatterUtils.formatNumber(1.678,2,"m"));
        assertEquals("10,000,000t",FormatterUtils.formatNumber(10000000,2,"t"));
    }

}