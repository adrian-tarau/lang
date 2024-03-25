package net.microfalx.lang;

import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class TimeUtils {

    /**
     * How many microseconds are in a millisecond.
     */
    public static final long MICROSECONDS_IN_MILLISECONDS = 1000;

    /**
     * How many microseconds are in a millisecond.
     */
    public static final long NANOSECONDS_IN_MILLISECONDS = 1000 * MICROSECONDS_IN_MILLISECONDS;

    /**
     * How many milliseconds are in a second.
     */
    public static final long MILLISECONDS_IN_SECOND = 1000;

    /**
     * How many milliseconds are in a minute.
     */
    public static final long MILLISECONDS_IN_MINUTE = 60 * MILLISECONDS_IN_SECOND;

    /**
     * How many milliseconds are in an hour.
     */
    public static final long MILLISECONDS_IN_HOUR = 60 * MILLISECONDS_IN_MINUTE;

    /**
     * How many milliseconds are in an day.
     */
    public static final long MILLISECONDS_IN_DAY = 24 * MILLISECONDS_IN_HOUR;

    /**
     * A constant for 30 seconds.
     */
    public static final long THIRTY_SECONDS = 30 * MILLISECONDS_IN_SECOND;

    /**
     * A constant for 20 seconds.
     */
    public static final long TWENTY_SECONDS = 20 * MILLISECONDS_IN_SECOND;

    /**
     * A constant for 10 seconds.
     */
    public static final long TEN_SECONDS = 10 * MILLISECONDS_IN_SECOND;

    /**
     * A constant for one minute.
     */
    public static final long ONE_MINUTE = MILLISECONDS_IN_MINUTE;

    /**
     * A constant for 5 minutes.
     */
    public static final long FIVE_MINUTE = 5 * MILLISECONDS_IN_MINUTE;

    /**
     * A constant for 15 minutes.
     */
    public static final long FIFTEEN_MINUTE = 15 * MILLISECONDS_IN_MINUTE;

    /**
     * A constant for one hour.
     */
    public static final long ONE_HOUR = MILLISECONDS_IN_HOUR;

    /**
     * A constant for one day.
     */
    public static final long ONE_DAY = MILLISECONDS_IN_DAY;

    /**
     * A constant for one month.
     */
    public static final long ONE_MONTH = 30 * MILLISECONDS_IN_DAY;

    /**
     * A constant for one year.
     */
    public static final long ONE_YEAR = 365 * MILLISECONDS_IN_DAY;

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
     * Returns a local date/time from a temporal
     *
     * @param temporal the temporal
     * @return the local date/time, null if was null
     */
    public static LocalDateTime toLocalDateTime(Object temporal) {
        ZonedDateTime zonedDateTime = toZonedDateTime(temporal);
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }

    /**
     * Converts a temporal (old and new API) to a {@link ZonedDateTime} and changes the zone.
     *
     * @param temporal any of the Java Time API temporal objects
     * @return a zoned date time
     * @see ZonedDateTime#withZoneSameInstant(ZoneId)
     */
    public static ZonedDateTime toZonedDateTimeSameInstant(Object temporal, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = toZonedDateTime(temporal);
        return zonedDateTime != null ? zonedDateTime.withZoneSameInstant(zoneId) : null;
    }

    /**
     * Converts a temporal (old and new API) to a {@link ZonedDateTime}.
     *
     * @param temporal any of the Java Time API temporal objects
     * @return a zoned date time
     */
    public static ZonedDateTime toZonedDateTime(Object temporal) {
        if (temporal == null) return null;
        if (temporal instanceof LocalDate) {
            return ((LocalDate) temporal).atStartOfDay(ZoneId.systemDefault());
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).atZone(ZoneId.systemDefault());
        } else if (temporal instanceof ZonedDateTime) {
            return (ZonedDateTime) temporal;
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toZonedDateTime();
        } else if (temporal instanceof Date) {
            return ((Date) temporal).toInstant().atZone(ZoneId.systemDefault());
        } else if (temporal instanceof Number) {
            return Instant.ofEpochMilli(((Number) temporal).longValue()).atZone(ZoneId.systemDefault());
        } else {
            throw new IllegalArgumentException("Cannot convert temporal '" + temporal + " to a zoned date/time");
        }
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

    /**
     * Converts a temporal to a SQL timestamp.
     *
     * @param temporal the temporal
     * @return the timestamp, null if temporal was null
     */
    public static Timestamp toTimestamp(Temporal temporal) {
        if (temporal == null) return null;
        Instant instant;
        if (temporal instanceof LocalDateTime) {
            instant = ((LocalDateTime) temporal).toInstant(UTC_OFFSET);
        } else if (temporal instanceof LocalDate) {
            instant = ((LocalDate) temporal).atStartOfDay().toInstant(UTC_OFFSET);
        } else if (temporal instanceof ZonedDateTime) {
            instant = ((ZonedDateTime) temporal).toInstant();
        } else if (temporal instanceof OffsetDateTime) {
            instant = ((OffsetDateTime) temporal).toInstant();
        } else {
            throw new IllegalArgumentException("Value not a temporal: " + temporal);
        }
        return new Timestamp(instant.toEpochMilli());
    }

    /**
     * Returns a temporal which contains JVM zone (or offset) if the type is a local one (so no zone, which means JVM zone).
     * <p>
     * Some types (like date) will have the midnight attached in order to be able to get a zone
     *
     * @param value the temporal to convert
     * @return a new temporal with zone
     */
    public static Temporal withSystemZone(Temporal value) {
        if (value == null) return null;
        if (value instanceof ChronoLocalDateTime) {
            return ((ChronoLocalDateTime<?>) value).atZone(ZoneId.systemDefault());
        } else if (value instanceof ChronoLocalDate) {
            return ((ChronoLocalDate) value).atTime(java.time.LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault());
        } else if (value instanceof java.time.LocalTime) {
            return ((java.time.LocalTime) value).atOffset(toOffset(ZoneId.systemDefault()));
        } else {
            return value;
        }
    }

    /**
     * Converts a zone to a zone offset.
     *
     * @param zoneId the zone
     * @return a non-null instance
     */
    public static ZoneOffset toOffset(ZoneId zoneId) {
        java.time.Instant instant = java.time.Instant.now();
        return zoneId.getRules().getOffset(instant);
    }

    /**
     * Returns how must time passed compared with <code>now</code>, basically System.currentTimeMillis() - time.
     *
     * @param time an arbitrary time; accepted types: Date, DateTime, Number/String (instant)
     * @return time duration between now and time, <code>Long.MIN_VALUE</code> if time is null, 0 or less
     */
    public static long millisSince(Object time) {
        if (time == null) return Long.MIN_VALUE;
        long millis;
        if (time instanceof Date) {
            millis = ((Date) time).getTime();
        } else if (time instanceof Number) {
            millis = ((Number) time).longValue();
        } else if (time instanceof Instant) {
            millis = ((Instant) time).toEpochMilli();
        } else if (time instanceof String) {
            millis = Long.parseLong((String) time);
        } else {
            throw new IllegalArgumentException("Invalid time object: " + time);
        }
        if (millis <= 0) return Long.MIN_VALUE;
        long duration = currentTimeMillis() - millis;
        return duration <= 0 ? 0 : duration;
    }

    /**
     * Returns a timestamp of 1 hour ago.
     *
     * @return millis since epoch
     */
    public static long oneHourAgo() {
        return currentTimeMillis() - MILLISECONDS_IN_HOUR;
    }

    /**
     * Returns a timestamp of 1 hour ago.
     *
     * @return millis since epoch
     */
    public static long oneDayAgo() {
        return currentTimeMillis() - MILLISECONDS_IN_DAY;
    }
}
