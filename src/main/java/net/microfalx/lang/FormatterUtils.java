package net.microfalx.lang;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import static net.microfalx.lang.NumberUtils.*;
import static net.microfalx.lang.StringUtils.EMPTY_STRING;
import static net.microfalx.lang.StringUtils.NA_STRING;
import static net.microfalx.lang.TimeUtils.toZonedDateTime;

public class FormatterUtils {

    private static final String DEFAULT_NULL_TEMPORAL = NA_STRING;

    public static final long K = 1000;
    public static final long M = 1000 * K;
    public static final long G = 1000 * M;

    public static final long MICROS_IN_MILLI = 1000;
    public static final long NANOS_IN_MILLI = 1000 * MICROS_IN_MILLI;
    public static final long NANOS_IN_MICRO = 1000;
    public static final long MILLIS_IN_SECOND = 1000;
    public static final long MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND;
    public static final long MILLIS_IN_HOUR = 60 * MILLIS_IN_MINUTE;
    public static final long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;

    public static final double MIN_FORMATABLE_DOUBLE = 0.000001;

    public static final String NA_VALUE = "-";

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
        if (value instanceof LocalTime) {
            return ((LocalTime)value).format(timeFormatter);
        } else if (value instanceof OffsetTime) {
            return ((OffsetTime)value).format(timeFormatter);
        } else {
            return value.toString();
        }
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
     * Formats a numeric value as throughput.
     * <p>
     * If the value cannot be formatted, it returns {@link #NA_VALUE}
     *
     * @param value the value to format
     * @param unit  the unit
     * @return the formatted value
     */
    public static String formatThroughput(Object value, String unit) {
        return formatThroughput(value, null, unit, NA_VALUE);
    }


    /**
     * Formats a numeric value as throughput.
     * <p>
     * If the value cannot be formatted, it returns {@link #NA_VALUE}
     *
     * @param value    the value to format
     * @param duration the amount of time passed
     * @param unit     the unit
     * @return the formatted value
     */
    public static String formatThroughput(Object value, Duration duration, String unit) {
        return formatThroughput(value, duration, unit, NA_VALUE);
    }

    /**
     * Formats a numeric value as throughput.
     * <p>
     * The units are usually "t" (transactions), "r" (requests), "b" (bytes). When bytes is used, numbers are formatted
     * using KB, MB, GB.
     *
     * @param value        the value to format
     * @param duration     the amount of time passed since the value was tracked; NULL if the value is already a throughput
     * @param unit         the unit , if null or empty "r" (requests)
     * @param defaultValue the default value
     * @return the formatted value
     */
    public static String formatThroughput(Object value, Duration duration, String unit, String defaultValue) {
        if (value == null) return defaultValue;
        if (StringUtils.isEmpty(unit)) unit = "r";
        double valueAsDouble = NumberUtils.toNumber(value, ZERO_DOUBLE).doubleValue();
        double throughput;
        if (duration != null) {
            throughput = throughput(value, duration, ZERO_DOUBLE);
        } else {
            throughput = valueAsDouble;
        }
        if (throughput < MIN_FORMATABLE_DOUBLE) return defaultValue;
        String text;
        if ("b".equalsIgnoreCase(unit)) {
            text = formatBytes(isInteger(throughput) ? (long) throughput : throughput);
        } else {
            text = formatNumber(isInteger(throughput) ? (long) throughput : throughput, 1, null) + unit;
        }
        return text + "/s";
    }

    /**
     * Formats a duration.
     * <p>
     * A duration can be a number (millis) or a {@link Duration}. If the value is a floating point or duration
     * has nanoseconds only, the value will be formatted in micro-seconds
     *
     * @param value the value to format
     * @return the formated value
     */
    public static String formatDuration(Object value) {
        return formatDuration(value, NA_STRING);
    }

    /**
     * Formats a duration.
     * <p>
     * A duration can be a number (millis) or a {@link Duration}.
     *
     * @param value        the value to format
     * @param defaultValue the default value
     * @return the formated value
     */
    public static String formatDuration(Object value, String defaultValue) {
        if (value == null || value instanceof String) return defaultValue;
        long millis = -1;
        long nano = -1;
        if (value instanceof Number) {
            float floatValue = ((Number) value).floatValue();
            if (floatValue > 0 && floatValue < 1) {
                nano = (long) (floatValue * NANOS_IN_MILLI);
            } else {
                millis = ((Number) value).longValue();
            }
        } else if (value instanceof Duration) {
            Duration duration = (Duration) value;
            if (duration.toMillis() == 0) {
                nano = duration.getNano();
                if (nano == 0) millis = 0;
            } else {
                millis = duration.toMillis();
            }
        }
        if (millis < 0) {
            if (nano < 0) {
                return defaultValue;
            } else {
                if (nano < NANOS_IN_MICRO) {
                    return nano + "ns";
                } else {
                    return (nano / NANOS_IN_MICRO) + "μs";
                }
            }
        } else {
            if (millis < K) {
                return millis + "ms";
            } else if (millis < MILLIS_IN_MINUTE) {
                int seconds = (int) (millis / MILLIS_IN_SECOND);
                millis = millis - seconds * MILLIS_IN_SECOND;
                return seconds + "s" + (millis > 0 ? " " + millis + "ms" : EMPTY_STRING);
            } else if (millis < MILLIS_IN_HOUR) {
                int minutes = (int) (millis / MILLIS_IN_MINUTE);
                int seconds = (int) ((millis - minutes * MILLIS_IN_MINUTE) / MILLIS_IN_SECOND);
                return minutes + "m" + (seconds > 0 ? " " + seconds + "s" : EMPTY_STRING);
            } else if (millis < MILLIS_IN_DAY) {
                int hours = (int) (millis / MILLIS_IN_HOUR);
                int minutes = (int) ((millis - hours * MILLIS_IN_HOUR) / MILLIS_IN_MINUTE);
                return hours + "h" + (minutes > 0 ? " " + minutes + "m" : EMPTY_STRING);
            } else {
                int hours = (int) (millis / MILLIS_IN_HOUR);
                return hours + "h";
            }
        }
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
        } else {
            suffix = "B";
        }
        return formatNumber(number, decimals, suffix);
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
        return formatNumber(number, decimals, suffix);
    }

    /**
     * Formats a number.
     *
     * @param number   the number
     * @param decimals the number of decimals if floating point
     * @param suffix   a suffix to be added at the end, if given
     * @return the formatted number
     */
    public static String formatNumber(Number number, int decimals, String suffix) {
        boolean isFloating = number instanceof Float || number instanceof Double;
        String text;
        if (isFloating) {
            double valueAsDouble = number.doubleValue();
            text = String.format("%,." + decimals + "f", valueAsDouble);
        } else {
            long valueAsLong = number.longValue();
            text = String.format("%,d", valueAsLong);
        }
        if (StringUtils.isNotEmpty(suffix)) text += suffix;
        return text;
    }


}
