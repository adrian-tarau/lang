package net.microfalx.lang;

/**
 * An interface for an object which can be identified.
 *
 * @param <T> the identifier type.
 */
public interface Identifiable<T> {

    /**
     * Returns the identifier
     *
     * @return the identifier, null if not available
     */
    T getId();
}
