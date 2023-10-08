package net.microfalx.lang;

/**
 * An interface for initializable objects.
 */
public interface Initializable {

    /**
     * Invoked to initialize the object with a specific context.
     *
     * @param context initialization context
     */
    void initialize(Object... context);
}
