package net.microfalx.lang;

/**
 * An interface for an object which needs to be released after it is not needed.
 */
public interface Releasable extends AutoCloseable {

    /**
     * Called to release the resources.
     */
    void release();

    @Override
    default void close() {
        release();
    }
}
