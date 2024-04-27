package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    private final LocalTime TIME = LocalTime.of(17, 12, 30);
    private final OffsetTime OFFSET_TIME = LocalTime.of(17, 12, 30).atOffset(ZoneOffset.ofHours(-2));
    private final LocalDate DATE = LocalDate.of(2023, 9, 27);
    private final Instant INSTANT = Instant.ofEpochMilli(1695849150000L);
    private final ZonedDateTime ZONED_DATETIME = ZonedDateTime.of(DATE, TIME, ZoneId.systemDefault());

    @Test
    void convertMillis() {
        long timestamp = TimeUtils.toMillis(ZONED_DATETIME);
        assertEquals(INSTANT.toEpochMilli(), timestamp);
        assertEquals(ZONED_DATETIME, TimeUtils.fromMillis(timestamp));
    }

    @Test
    void formatTemporal() {
        assertEquals("2023-09-27T17:12:30-04:00", TimeUtils.format(ZONED_DATETIME));
        assertEquals("2023-09-27T21:12:30Z", TimeUtils.format(INSTANT));
        assertEquals("2023-09-27", TimeUtils.format(DATE));
        assertEquals("17:12:30Z", TimeUtils.format(TIME));
        assertEquals("17:12:30-02:00", TimeUtils.format(OFFSET_TIME));
    }

    @Test
    void seemsDateTime() {
        assertFalse(TimeUtils.seemsDateTime(null));
        assertFalse(TimeUtils.seemsDateTime(""));
        assertFalse(TimeUtils.seemsDateTime("a"));
        assertFalse(TimeUtils.seemsDateTime("20"));
        assertFalse(TimeUtils.seemsDateTime("2023"));
        assertFalse(TimeUtils.seemsDateTime("20231207"));
        assertFalse(TimeUtils.seemsDateTime("2024/03/10"));
        assertFalse(TimeUtils.seemsDateTime("2024-03-10"));
        assertFalse(TimeUtils.seemsDateTime("03/12/2024"));
        assertFalse(TimeUtils.seemsDateTime("12.03.2024"));
        assertFalse(TimeUtils.seemsDateTime("03/12/2024 123"));
        assertFalse(TimeUtils.seemsDateTime("12.03.2024T12:23"));

        assertTrue(TimeUtils.seemsDateTime("2024/03/10 12:20:45"));
        assertTrue(TimeUtils.seemsDateTime("2024/03/10 12:20:45Z"));
        assertTrue(TimeUtils.seemsDateTime("2024-03-10T12:20:45"));
        assertTrue(TimeUtils.seemsDateTime("2024-03-10T12:20:45Z"));
        assertTrue(TimeUtils.seemsDateTime("2024-03-10T12:20:45+02:00"));
        assertTrue(TimeUtils.seemsDateTime("03/12/2024 12:20:45"));
        assertTrue(TimeUtils.seemsDateTime("03/12/2024 12:20:45Z"));
        assertTrue(TimeUtils.seemsDateTime("03/12/2024 12:20:45-02:00"));
        assertTrue(TimeUtils.seemsDateTime("12.03.2024 12:20:45"));
        assertTrue(TimeUtils.seemsDateTime("12.03.2024 12:20:45Z"));
        assertTrue(TimeUtils.seemsDateTime("12.03.2024 12:20:45-02:00"));
    }

    @Test
    public void isoWithValidDateTime() {
        assertFalse(TimeUtils.isDateTime("2023-12-07T12:30:00.123+0100"));
        assertFalse(TimeUtils.isDateTime("2023-12-07T12:30:00.123-0100"));
        assertFalse(TimeUtils.isDateTime("2023-12-07T12:30:00+0100"));
        assertFalse(TimeUtils.isDateTime("2023-12-07T12:30:00-0100"));

        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00Z"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123Z"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123+01:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123-01:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123+01:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00.123-01:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00+01:00"));
        assertTrue(TimeUtils.isDateTime("2023-12-07T12:30:00-01:00"));
    }

    @Test
    void seemsDate() {
        assertFalse(TimeUtils.seemsDate(null));
        assertFalse(TimeUtils.seemsDate(""));
        assertFalse(TimeUtils.seemsDate("a"));
        assertFalse(TimeUtils.seemsDate("12"));
        assertFalse(TimeUtils.seemsDate("2024"));
        assertFalse(TimeUtils.seemsDate("202403"));
        assertFalse(TimeUtils.seemsDate("20240310"));
        assertFalse(TimeUtils.seemsDate("2024-0310"));
        assertFalse(TimeUtils.seemsDate("2024\\03\\10"));

        assertTrue(TimeUtils.seemsDate("2024/03/10"));
        assertTrue(TimeUtils.seemsDate("2024-03-10"));
        assertTrue(TimeUtils.seemsDate("03/12/2024"));
        assertTrue(TimeUtils.seemsDate("12.03.2024"));
    }

    @Test
    void seemsTime() {
        assertFalse(TimeUtils.seemsTime(null));
        assertFalse(TimeUtils.seemsTime(""));
        assertFalse(TimeUtils.seemsTime("a"));
        assertFalse(TimeUtils.seemsTime("12"));
        assertFalse(TimeUtils.seemsTime("1234"));
        assertFalse(TimeUtils.seemsTime("12341234"));
        assertFalse(TimeUtils.seemsTime("12:20"));

        assertTrue(TimeUtils.seemsTime("12:20:45"));
        assertTrue(TimeUtils.seemsTime("12:20:45Z"));
        assertTrue(TimeUtils.seemsTime("12:20:45+02:00"));
        assertTrue(TimeUtils.seemsTime("12:20:45-02:00"));
    }
}