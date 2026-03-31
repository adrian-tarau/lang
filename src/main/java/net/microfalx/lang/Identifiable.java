package net.microfalx.lang;

/**
 * An interface for an object which can be identified.
 *
 * @param <T> the identifier type.
 */
public interface Identifiable<T> {

    /**
     * Creates an identifiable which carries only the identifier.
     *
     * @param id  the identifier
     * @param <T> the identifier type
     * @return a non-null instance
     */
    static <T> Identifiable<T> create(T id) {
        return new ObjectUtils.IdentifiableImpl<>(id);
    }

    /**
     * Returns the identifier
     *
     * @return the identifier, null if not available
     */
    T getId();
}
