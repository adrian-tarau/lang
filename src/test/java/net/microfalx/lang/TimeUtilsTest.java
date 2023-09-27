package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilsTest {

    private LocalTime TIME = LocalTime.of(17, 12, 30);
    private LocalDate DATE = LocalDate.of(2023, 9, 27);
    private Instant INSTANT = Instant.ofEpochMilli(1695849150000L);
    private ZonedDateTime ZONED_DATETIME = ZonedDateTime.of(DATE, TIME, ZoneId.systemDefault());

    @Test
    void convertMillis() {
        long timestamp = TimeUtils.toMillis(ZONED_DATETIME);
        assertEquals(INSTANT.toEpochMilli(), timestamp);
        assertEquals(ZONED_DATETIME, TimeUtils.fromMillis(timestamp));
    }
}