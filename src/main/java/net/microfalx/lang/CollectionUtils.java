package net.microfalx.lang;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.*;

/**
 * Utilities around collections.
 */
public class CollectionUtils {

    /**
     * Returns a non-null collection.
     *
     * @param items the original collection
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Collection<T> asCollection(Collection<T> items) {
        return items != null ? items : emptyList();
    }

    /**
     * Returns a non-null collection.
     *
     * @param items the original collection
     * @param <K>   the key type
     * @param <V>   the value type
     * @return an immutable collection
     */
    public static <K, V> Collection<V> asCollection(Map<K, V> items) {
        return items != null ? items.values() : emptyList();
    }

    /**
     * Returns an non-null set.
     *
     * @param items the original set
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Set<T> asSet(Set<T> items) {
        return items != null ? items : Collections.emptySet();
    }

    /**
     * Returns an non-null map.
     *
     * @param items the original map
     * @param <K>   the key type
     * @param <V>   the value type
     * @return an immutable collection
     */
    public static <K, V> Map<K, V> asMap(Map<K, V> items) {
        return items != null ? items : emptyMap();
    }

    /**
     * Returns an immutable collection from a regular collection.
     *
     * @param items the original collection, can be NULL
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Collection<T> immutableCollection(Collection<T> items) {
        return items != null ? unmodifiableCollection(items) : emptyList();
    }

    /**
     * Returns an immutable set from a regular set.
     *
     * @param items the original set, can be NULL
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Set<T> immutableSet(Set<T> items) {
        return items != null ? unmodifiableSet(items) : Collections.emptySet();
    }

    /**
     * Returns an immutable collection from a regular collection.
     *
     * @param items the original map, can be NULL
     * @param <K>   the key type
     * @param <V>   the value type
     * @return an immutable collection
     */
    public static <K, V> Map<K, V> immutableMap(Map<K, V> items) {
        return items != null ? unmodifiableMap(items) : emptyMap();
    }
}
