package net.microfalx.lang;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Utilities around objects.
 */
public class ObjectUtils {

    public static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * Returns if the object is "empty": a null object, an empty string({@link CharSequence}) or an empty collection.
     * Any other object type returns false(object not "empty")
     *
     * @param object an object instance
     * @return true if object is considered "empty"(does not carry out information)
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return StringUtils.isEmpty((CharSequence) object);
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else {
            return false;
        }
    }

    /**
     * Returns if the object is not empty.
     *
     * @param object an object instance
     * @return true if object is considered "empty"(does not carry out information)
     * @see #isEmpty(Object)
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * Returns if the object is not empty.
     *
     * @param object1 an first object instance
     * @param object2 an seconds object instance
     * @return true if objects are equal
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) return true;
        if (object1 == null || object2 == null) return false;
        return object1.equals(object2);
    }

    /**
     * Returns a default value if the input is null.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the original value or a default
     */
    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Returns the array length.
     * <p/>
     * It uses {@link #toArray(Object)} to get an array in case of null or not an array (converts to an array with one element).
     *
     * @param object the array reference
     * @return the array length
     * @see Array#getLength(Object)
     */
    public static int getArrayLength(Object object) {
        Object[] objects = toArray(object);
        return Array.getLength(objects);
    }

    /**
     * Returns whether the object is an array.
     *
     * @param object the object to test
     * @return {@code true} if an array, {@code false} otherwise
     */
    public static boolean isArray(Object object) {
        if (object == null) return false;
        return object.getClass().isArray();
    }

    /**
     * Converts the object to an array. If the object is already an array, it creates a copy of the array.
     *
     * @param object the object to convert
     * @return the object as an array or an empty array if the object is null
     */
    public static Object[] toArray(Object object) {
        if (object == null) return EMPTY_ARRAY;
        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            return Arrays.copyOf(array, array.length);
        } else {
            return new Object[]{object};
        }
    }

    /**
     * Converts an object to its string representation.
     *
     * @param value the value
     * @return the string representation, null if the object is null
     */
    public static String asString(Object value) {
        return value != null ? value.toString() : null;
    }

    /**
     * Converts an object to its string representation.
     *
     * @param value the value
     * @return the string representation, null if the object is null
     */
    public static String toString(Object value) {
        return value != null ? value.toString() : null;
    }
}
