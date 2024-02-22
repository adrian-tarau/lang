package net.microfalx.lang;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class TimeUtils {

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
