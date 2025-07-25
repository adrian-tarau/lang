package net.microfalx.lang;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utilities around objects.
 */
public class ObjectUtils {

    public static final Object[] EMPTY_ARRAY = new Object[0];

    private final static int MAXIMUM_IDENTIFIER_LENGTH = 100;

    /**
     * Returns if the object is "empty": a null object, an empty string({@link CharSequence}) or an empty collection.
     * Any other object type returns false (object not "empty")
     *
     * @param object an object instance
     * @return true if an object is considered "empty" (does not carry out information)
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
        } else if (object instanceof Optional) {
            return ((Optional) object).isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Returns if the object is "empty": a null object, an empty string({@link CharSequence}) or an empty collection.
     * Any other object type returns false (object not "empty")
     *
     * @param object              an object instance
     * @param includeEmptyStrings {@code true} to tread empty strings as empty, {@code false} to ignore them
     * @return true if an object is considered "empty" (does not carry out information)
     */
    public static boolean isEmpty(Object object, boolean includeEmptyStrings) {
        boolean empty = isEmpty(object);
        if (empty) return true;
        if (!includeEmptyStrings) return false;
        Object[] values = toArray(object);
        for (Object value : values) {
            if (value instanceof String && StringUtils.isNotEmpty((String) value)) return false;
        }
        return true;
    }

    /**
     * Returns if the object is not empty.
     *
     * @param object an object instance
     * @return true if an object is considered "empty" (does not carry out information)
     * @see #isEmpty(Object)
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * Returns if the object is not empty.
     *
     * @param firstObject  an first object instance
     * @param secondObject an seconds object instance
     * @return true if objects are equal
     */
    public static boolean equals(Object firstObject, Object secondObject) {
        if (firstObject == secondObject) return true;
        if (firstObject == null || secondObject == null) return false;
        return firstObject.equals(secondObject);
    }

    /**
     * Returns whether the value is serializable.
     *
     * @param value the value
     * @return {@code} true if serializable, {@code false} otherwise
     */
    public static boolean isSerializable(Object value) {
        return value instanceof Serializable;
    }

    /**
     * Returns whether both objects or not null.
     *
     * @param firstObject  the first object
     * @param secondObject the second object
     * @return {@code} true if both are not null, {@code false} otherwise
     */
    public static boolean isNotNull(Object firstObject, Object secondObject) {
        return !isNull(firstObject, secondObject);
    }

    /**
     * Returns whether both or at least one object is null.
     *
     * @param firstObject  the first object
     * @param secondObject the second object
     * @return {@code} true if at least one is null, {@code false} otherwise
     */
    public static boolean isNull(Object firstObject, Object secondObject) {
        return !(firstObject != null && secondObject != null);
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
     * Returns the name of the object.
     *
     * @param value the value
     * @return the name
     * @see Nameable
     */
    public static String getName(Object value) {
        if (value == null) return null;
        if (value instanceof Nameable) {
            return ((Nameable) value).getName();
        } else {
            return value.toString();
        }
    }

    /**
     * Returns the description of the object.
     *
     * @param value the value
     * @return the name
     * @see Nameable
     */
    public static String getDescription(Object value) {
        if (value == null) return null;
        if (value instanceof Descriptable) {
            return ((Descriptable) value).getDescription();
        } else {
            return value.toString();
        }
    }

    /**
     * Compares two objects.
     * <p>
     * The method expects that the objects implement {@link Comparable} otherwise the result will always be 0
     *
     * @param o1 the first object
     * @param o2 the second object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> int compare(Optional<T> o1, Optional<T> o2) {
        if (o1 == o2) return 0;
        if (o1 == null || o1.isEmpty()) return -1;
        if (o2 == null || o2.isEmpty()) return 1;
        return compare(o1.get(), o2.get());
    }

    /**
     * Compares two objects.
     * <p>
     * The method expects that the objects implement {@link Comparable} otherwise the result will always be 0
     *
     * @param o1 the first object
     * @param o2 the second object
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static int compare(Object o1, Object o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        if (o1 instanceof Comparable && o2 instanceof Comparable && o1.getClass() == o2.getClass()) {
            return ((Comparable) o1).compareTo(o2);
        } else {
            return 0;
        }
    }

    /**
     * Executes an action for each object in the array, if an array or for the object itself.
     *
     * @param object the object or object array
     * @param action the action
     */
    public static void forEach(Object object, Consumer<? super Object> action) {
        ArgumentUtils.requireNonNull(action);
        if (object == null) return;
        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (Object o : array) {
                action.accept(o);
            }
        } else {
            action.accept(object);
        }
    }

    /**
     * Returns a unique string identifier from an object.
     * <p>
     * The method tries to use {@link Identifiable}, otherwise it resorts to a fast checksum on all members of the instances.
     * <p>
     * A short string is also considered an identifier.
     *
     * @param value the value
     * @return the identifier
     * @see StringUtils#toIdentifier(String)
     * @see Identifiable
     */
    public static String toIdentifier(Object value) {
        if (value instanceof Identifiable) {
            return StringUtils.toIdentifier(toString(((Identifiable<?>) value).getId()));
        } else if (value instanceof String && ((String) value).length() <= MAXIMUM_IDENTIFIER_LENGTH) {
            return StringUtils.toIdentifier((String) value);
        } else {
            return StringUtils.toIdentifier(ClassUtils.getName(value)) + "_" + value.hashCode();
        }
    }

    /**
     * Converts an object to its string representation.
     *
     * @param value the value
     * @return the string representation, null if the object is null
     */
    @Deprecated
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

    /**
     * Converts an array of objects to an array of string representation.
     *
     * @param value the value
     * @return the string array representation
     */
    public static String[] toStringArray(Object value) {
        Object[] array = toArray(value);
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = toString(array[i]);
        }
        return result;
    }

    /**
     * Clones the object.
     * <p>
     * First it tried to serialize the object. If not possible, it will create another object and copy fields with field.
     *
     * @param value the value
     * @param <T>   the object type
     * @return a clone
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T value) {
        if (value == null) return null;
        if (value instanceof Serializable) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(buffer);
                oos.writeObject(value);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
                return (T) ois.readObject();
            } catch (IOException e) {
                // it should never happen, it is in memory
                return ExceptionUtils.rethrowExceptionAndReturn(e);
            } catch (ClassNotFoundException e) {
                // this could happen but bubble the problem without exposing this
                return ExceptionUtils.rethrowExceptionAndReturn(e);
            }
        } else {
            throw new IllegalStateException("Not implemented");
        }
    }
}
