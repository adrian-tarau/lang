package net.microfalx.lang;

import java.lang.reflect.Array;
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
     * Converts an object to its string representation.
     *
     * @param value the value
     * @return the string representation, null if the object is null
     */
    public static String toString(Object value) {
        return value != null ? value.toString() : null;
    }
}
