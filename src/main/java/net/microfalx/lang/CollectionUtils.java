package net.microfalx.lang;

import java.util.*;

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
     * Returns an immutable list from a regular collection.
     *
     * @param items the original collection, can be NULL
     * @param <T>   the item type
     * @return an immutable collection
     */
    public static <T> List<T> immutableList(List<T> items) {
        return items != null ? unmodifiableList(items) : emptyList();
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

    /**
     * Returns an iterable which wraps an iterator and can be consumed once.
     *
     * @param iterator the iterator
     * @param <T>      the data type
     * @return a non-null instance
     */
    public static <T> List<T> toList(Iterator<T> iterator) {
        if (iterator == null) return new ArrayList<>();
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            T value = iterator.next();
            list.add(value);
        }
        return list;
    }

    /**
     * Returns a list out of an iterable.
     *
     * @param iterable the iterator
     * @param <T>      the data type
     * @return a non-null instance
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        return iterable == null ? new ArrayList<>() : toList(iterable.iterator());
    }

    /**
     * Returns an iterable which wraps an iterator and can be consumed once.
     *
     * @param iterator the iterator
     * @param <T>      the data type
     * @return a non-null instance
     */
    public static <T> Iterable<T> toIterable(Iterator<T> iterator) {
        return iterator == null ? Collections.emptyList() : new OnceIterable<>(iterator);
    }

    static class OnceIterable<T> implements Iterable<T> {

        private final Iterator<T> iterator;
        private boolean consumed;

        OnceIterable(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public Iterator<T> iterator() {
            if (consumed) throw new IllegalStateException("The iterator was already consumed");
            consumed = true;
            return iterator;
        }
    }
}
