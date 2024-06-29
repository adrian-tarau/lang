package net.microfalx.lang;

import java.time.temporal.Temporal;

/**
 * An interface for objects which carries the time when the object was created and modified.
 * <p>
 * Some object might not support updates, in this case the modify time is the same as creation time.
 */
public interface Timestampable<T extends Temporal> {

    /**
     * Returns the instant in time when the object was created.
     *
     * @return creation time.
     */
    T getCreatedAt();

    /**
     * Returns the instant in time when the object was modified.
     *
     * @return modification time
     */
    default T getModifiedAt() {
        return getCreatedAt();
    }
}
