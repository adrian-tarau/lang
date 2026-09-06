package net.microfalx.lang;

/**
 * An interface implemented by objects which can provide their deep size and estimated number
 * of references to other objects without introspection.
 */
public interface Sizeable {

    /**
     * Returns the deep size of the object in bytes.
     */
    long getSizeOf();

    /**
     * Returns the estimated number of references (elements) to other objects.
     */
    int getCountOf();
}
