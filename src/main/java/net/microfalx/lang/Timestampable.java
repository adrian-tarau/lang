package net.microfalx.lang;

import java.time.temporal.Temporal;

/**
 * An interface for objects which carries the time when the object was created and updated.
 * <p>
 * Some object might not support updates, in this case the update is the same as creation time.
 */
public interface Timestampable<T extends Temporal> {

    /**
     * Returns the instant in time when the object was created.
     *
     * @return creation time.
     */
    T getCreatedAt();

    /**
     * Returns the instant in time when the object was updated.
     *
     * @return modification time
     */
    default T getUpdatedAt() {
        return getCreatedAt();
    }
}
