package net.microfalx.lang;

import com.google.common.math.DoubleMath;

import java.time.Duration;
import java.time.temporal.Temporal;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Various utilities around numbers.
 */
public class NumberUtils {

    public static final Double ZERO_DOUBLE = 0d;
    public static final Double ZERO_FLOAT = 0d;

    /**
     * Returns whether the floating point is an integer (has no fractional part).
     *
     * @param value the value to test
     * @return {@code true} if can be cast to an integer without loosing precision, {@code false} otherwise
     */
    public static boolean isInteger(double value) {
        return DoubleMath.isMathematicalInteger(value);
    }

    /**
     * Returns the value as a number.
     *
     * @param value        the value
     * @param defaultValue the default value (if the value cannot be converted or it is NULL)
     * @return the value as number
     */
    public static Number toNumber(Object value, Number defaultValue) {
        Number valueAsNumber;
        if (value instanceof Number) {
            valueAsNumber = ((Number) value);
        } else if (value instanceof String) {
            try {
                valueAsNumber = Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            valueAsNumber = defaultValue;
        }
        return valueAsNumber;
    }

    /**
     * Calculates a throughput.
     *
     * @param value    the value accumulated over a duration of time
     * @param temporal the time when the value started to be accumulated
     * @return the throughput, 0 if it cannot be calculated
     */
    public static Double throughput(Object value, Temporal temporal) {
        return throughput(value, temporal, ZERO_DOUBLE);
    }

    /**
     * Calculates a throughput.
     *
     * @param value        the value accumulated over a duration of time
     * @param temporal     the time when the value started to be accumulated
     * @param defaultValue the default value
     * @return the throughput, default value if it cannot be calculated
     */
    public static Double throughput(Object value, Temporal temporal, Double defaultValue) {
        if (value == null || temporal == null) return defaultValue;
        Duration duration = TimeUtils.durationSince(temporal);
        return throughput(value, duration, defaultValue);
    }

    /**
     * Calculates a throughput.
     *
     * @param value    the value accumulated over a duration of time
     * @param duration the time duration
     * @return the throughput, 0 if it cannot be calculated
     */
    public static Double throughput(Object value, Duration duration) {
        return throughput(value, duration, ZERO_DOUBLE);
    }

    /**
     * Calculates a throughput.
     *
     * @param value        the value accumulated over a duration of time
     * @param duration     the time duration
     * @param defaultValue the default value
     * @return the throughput, default value if it cannot be calculated
     */
    public static Double throughput(Object value, Duration duration, Double defaultValue) {
        if (value == null || duration == null) return defaultValue;
        requireNonNull(duration);
        value = toNumber(value, defaultValue);
        if (value == null) return defaultValue;
        double valueAsDouble = ((Number) value).doubleValue();
        float seconds = (float) duration.toMillis() / 1000f;
        double throughput = (float) valueAsDouble / seconds;
        if (throughput < 0.00001) return defaultValue;
        return throughput;
    }

    /**
     * Returns whether the value is between bounds.
     *
     * @param value the value
     * @param min   the minimum value
     * @param max   the maximum value (inclusive)
     * @return {@code true} if within bounds, {@code false} otherwise
     */
    public static boolean isBetweenInclusive(long value, long min, long max) {
        return value >= min && value <= max;
    }

    /**
     * Returns whether the value is between bounds.
     *
     * @param value the value
     * @param min   the minimum value
     * @param max   the maximum value (exclusive)
     * @return {@code true} if within bounds, {@code false} otherwise
     */
    public static boolean isBetweenExclusive(long value, long min, long max) {
        return value >= min && value < max;
    }
}
