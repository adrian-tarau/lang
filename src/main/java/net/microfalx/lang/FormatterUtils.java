package net.microfalx.lang;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import static net.microfalx.lang.StringUtils.NA_STRING;

public class FormatterUtils {

    private static final String DEFAULT_NULL_TEMPORAL = NA_STRING;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatterUTC = dateTimeFormatter.withZone(ZoneId.of("UTC"));
    private static final DateTimeFormatter dateTimeWithMilliFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss SSS");

    /**
     * Formats an object which can be converted to a date object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDate(Object value) {
        return formatDateTime(dateFormatter, value);
    }

    /**
     * Formats an object which can be converted to a time object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatTime(Object value) {
        return formatDateTime(timeFormatter, value);
    }

    /**
     * Formats an object which can be converted to a date/time object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTime(Object value) {
        return formatDateTime(dateTimeFormatter, value);
    }

    /**
     * Formats an object which can be converted to a date/time object using UTC zone.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTimeUTC(Object value) {
        return formatDateTime(dateTimeFormatterUTC, value);
    }

    /**
     * Formats an object which can be converted to a date/time including milliseconds.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTimeWithMillis(Object value) {
        return formatDateTime(dateTimeWithMilliFormatter, value);
    }

    /**
     * Formats a temporal.
     *
     * @param temporal the value to format.
     * @return the string representation
     */
    public static String formatTemporal(Temporal temporal) {
        if (temporal == null) return DEFAULT_NULL_TEMPORAL;
        if (temporal instanceof LocalDate) {
            return formatDate(temporal);
        } else if (temporal instanceof LocalDateTime || temporal instanceof ZonedDateTime || temporal instanceof OffsetDateTime) {
            return formatDateTime(temporal);
        } else if (temporal instanceof LocalTime || temporal instanceof OffsetTime) {
            return formatTime(temporal);
        } else {
            throw new IllegalArgumentException("Unknown temporary: " + temporal.getClass());
        }

    }

    private static String formatDateTime(DateTimeFormatter formatter, Object value) {
        if (value == null) return DEFAULT_NULL_TEMPORAL;
        if (value instanceof Number) {
            return formatter.format(Instant.ofEpochMilli(((Number) value).longValue()));
        } else if (value instanceof Temporal) {
            return formatter.format((Temporal) value);
        } else {
            return DEFAULT_NULL_TEMPORAL;
        }
    }
}
