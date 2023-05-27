package net.microfalx.lang;

import static net.microfalx.lang.ObjectUtils.isEmpty;

/**
 * Utility methods for validation of method arguments.
 */
public class ArgumentUtils {

    /**
     * Checks that the specified object reference is not {@code null}.
     *
     * @param value the object reference to check for nullity
     * @param <T>   the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull(T value) {
        if (value == null) throw new IllegalArgumentException("Argument cannot be NULL");
        return value;
    }

    /**
     * Checks that the specified object reference is not {@code empty}.
     *
     * @param value the object reference to check for nullity
     * @param <T>   the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotEmpty(T value) {
        if (isEmpty(value)) throw new IllegalArgumentException("Argument cannot be empty");
        return value;
    }

    /**
     * Checks that the specified integer is within bounds.
     *
     * @param value   the value to check
     * @param minimum the minimum expected value, inclusive
     * @param maximum the maximum expected value, inclusive
     * @return the value
     */
    public static int requireBounded(int value, int minimum, int maximum) {
        if (value < minimum) throw new IllegalArgumentException("A minimum value of " + minimum + " is expected");
        if (value > maximum) throw new IllegalArgumentException("A maximum value of " + maximum + " is expected");
        return value;
    }

    /**
     * Checks that the specified long is within bounds.
     *
     * @param value   the value to check
     * @param minimum the minimum expected value, inclusive
     * @param maximum the maximum expected value, inclusive
     * @return the value
     */
    public static long requireBounded(long value, long minimum, long maximum) {
        if (value < minimum) throw new IllegalArgumentException("A minimum value of " + minimum + " is expected");
        if (value > maximum) throw new IllegalArgumentException("A maximum value of " + maximum + " is expected");
        return value;
    }
}
