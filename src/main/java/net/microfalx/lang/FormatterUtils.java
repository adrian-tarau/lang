package net.microfalx.lang;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import static net.microfalx.lang.StringUtils.NA_STRING;
import static net.microfalx.lang.TimeUtils.toZonedDateTime;

public class FormatterUtils {

    private static final String DEFAULT_NULL_TEMPORAL = NA_STRING;

    public static final long K = 1000;
    public static final long M = 1000 * K;
    public static final long G = 1000 * M;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatterUTC = dateTimeFormatter.withZone(TimeUtils.UTC_ZONE);
    private static final DateTimeFormatter dateTimeWithMilliFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss SSS");

    /**
     * Formats an object which can be converted to a date object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDate(Object value) {
        return formatDateTime(dateFormatter, value, null);
    }

    /**
     * Formats an object which can be converted to a date object.
     *
     * @param value    the value to format
     * @param timeZone the time zone
     * @return the string representation
     */
    public static String formatDate(Object value, ZoneId timeZone) {
        return formatDateTime(dateFormatter, value, timeZone);
    }

    /**
     * Formats an object which can be converted to a time object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatTime(Object value) {
        return formatDateTime(timeFormatter, value, null);
    }

    /**
     * Formats an object which can be converted to a time object.
     *
     * @param value    the value to format
     * @param timeZone the time zone
     * @return the string representation
     */
    public static String formatTime(Object value, ZoneId timeZone) {
        return formatDateTime(timeFormatter, value, timeZone);
    }

    /**
     * Formats an object which can be converted to a date/time object.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTime(Object value) {
        return formatDateTime(dateTimeFormatter, value, null);
    }

    /**
     * Formats an object which can be converted to a date/time object.
     *
     * @param value    the value to format
     * @param timeZone the time zone
     * @return the string representation
     */
    public static String formatDateTime(Object value, ZoneId timeZone) {
        return formatDateTime(dateTimeFormatter, value, timeZone);
    }

    /**
     * Formats an object which can be converted to a date/time object using UTC zone.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTimeUTC(Object value) {
        return formatDateTime(dateTimeFormatterUTC, value, TimeUtils.UTC_ZONE);
    }

    /**
     * Formats an object which can be converted to a date/time including milliseconds.
     *
     * @param value the value to format
     * @return the string representation
     */
    public static String formatDateTimeWithMillis(Object value) {
        return formatDateTime(dateTimeWithMilliFormatter, value, null);
    }

    /**
     * Formats a temporal.
     *
     * @param temporal the value to format.
     * @return the string representation
     */
    public static String formatTemporal(Temporal temporal) {
        return formatTemporal(temporal, ZoneId.systemDefault());
    }

    /**
     * Formats a temporal.
     *
     * @param temporal the value to format.
     * @param timeZone the time zone
     * @return the string representation
     */
    public static String formatTemporal(Temporal temporal, ZoneId timeZone) {
        if (temporal == null) return DEFAULT_NULL_TEMPORAL;
        if (temporal instanceof LocalDate) {
            return formatDate(temporal, timeZone);
        } else if (temporal instanceof LocalDateTime || temporal instanceof ZonedDateTime || temporal instanceof OffsetDateTime) {
            return formatDateTime(temporal, timeZone);
        } else if (temporal instanceof LocalTime || temporal instanceof OffsetTime) {
            return formatTime(temporal, timeZone);
        } else {
            throw new IllegalArgumentException("Unknown temporary: " + temporal.getClass());
        }
    }

    /**
     * Formats a temporal (old and new API) with in a given time zone.
     *
     * @param formatter the formatter
     * @param temporal  the temporal
     * @param timeZone  the zone, can be null for system zone
     * @return the formatted temporal
     */
    public static String formatDateTime(DateTimeFormatter formatter, Object temporal, ZoneId timeZone) {
        if (temporal == null) return DEFAULT_NULL_TEMPORAL;
        if (timeZone == null) timeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = toZonedDateTime(temporal);
        zonedDateTime = zonedDateTime.withZoneSameInstant(timeZone);
        return formatter.format(zonedDateTime);
    }

    /**
     * Formats a number of bytes.
     *
     * @param value the number
     * @return the formatted number
     */
    public static String formatBytes(Object value) {
        if (!(value instanceof Number)) return NA_STRING;
        Number number = (Number) value;
        int decimals = 0;
        String suffix = "";
        if (number.longValue() > 10 * G) {
            number = number.doubleValue() / (double) G;
            suffix = "GB";
        } else if (number.longValue() > 10 * M) {
            number = number.doubleValue() / (double) M;
            suffix = "MB";
        } else if (number.longValue() > 10 * K) {
            number = number.doubleValue() / (double) K;
            suffix = "KB";
        }
        boolean isFloating = number instanceof Float || number instanceof Double;
        if (isFloating) {
            double valueAsDouble = number.doubleValue();
            return String.format("%,." + decimals + "f", valueAsDouble) + suffix;
        } else {
            long valueAsLong = number.longValue();
            return String.format("%,d", valueAsLong) + suffix;
        }
    }

    /**
     * Formats a number.
     *
     * @param value the number
     * @return the formatted number
     */
    public static String formatNumber(Object value) {
        return formatNumber(value, 1);
    }

    /**
     * Formats a number.
     *
     * @param value the number
     * @return the formatted number
     */
    public static String formatNumber(Object value, int decimals) {
        if (!(value instanceof Number)) return NA_STRING;
        Number number = (Number) value;
        String suffix = "";
        if (number.longValue() > 10 * G) {
            number = number.doubleValue() / (double) G;
            suffix = "b";
        } else if (number.longValue() > 10 * M) {
            number = number.doubleValue() / (double) M;
            suffix = "m";
        } else if (number.longValue() > 10 * K) {
            number = number.doubleValue() / (double) K;
            suffix = "k";
        }
        boolean isFloating = number instanceof Float || number instanceof Double;
        if (isFloating) {
            double valueAsDouble = number.doubleValue();
            return String.format("%,." + decimals + "f", valueAsDouble) + suffix;
        } else {
            long valueAsLong = number.longValue();
            return String.format("%,d", valueAsLong) + suffix;
        }
    }


}
