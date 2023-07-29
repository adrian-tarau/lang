package net.microfalx.lang;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Utilities around collections.
 */
public class CollectionUtils {

    /**
     * Returns an immutable collection from a regular collection.
     *
     * @param items the original collection
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Collection<T> immutableCollection(Collection<T> items) {
        return items != null ? Collections.unmodifiableCollection(items) : Collections.emptyList();
    }

    /**
     * Returns an immutable collection from a regular collection.
     *
     * @param items the original collection
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> Set<T> immutableSet(Set<T> items) {
        return items != null ? Collections.unmodifiableSet(items) : Collections.emptySet();
    }

    /**
     * Returns an immutable collection from a regular collection.
     *
     * @param items the original collection
     * @param <K>   the key type
     * @param <V>   the value type
     * @return an immutable collection
     */
    public static <K, V> Map<K, V> immutableMap(Map<K, V> items) {
        return items != null ? Collections.unmodifiableMap(items) : Collections.emptyMap();
    }
}
