package net.microfalx.lang;

import java.time.*;
import java.time.temporal.Temporal;

public class TimeUtils {

    public static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    public static final ZoneOffset UTC_OFFSET = ZoneOffset.UTC;

    /**
     * Converts millis since epoch to a {@link ZonedDateTime}.
     *
     * @param value the time in millis
     * @return the date/time, null if the value represents a null
     */
    public static ZonedDateTime fromMillis(long value) {
        if (value <= 0) return null;
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault());
    }

    /**
     * Converts a {@link java.time.temporal.Temporal} to millis.
     *
     * @param temporal any of the Java Time API temporal objects
     * @return millis since epoch.
     */
    public static long toMillis(Temporal temporal) {
        if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).atZone(UTC_ZONE).toInstant().toEpochMilli();
        } else if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).atStartOfDay().atZone(UTC_ZONE).toInstant().toEpochMilli();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).withZoneSameInstant(UTC_ZONE).toInstant().toEpochMilli();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toInstant().toEpochMilli();
        } else {
            throw new IllegalArgumentException("Value not a temporal: " + temporal);
        }
    }
}
