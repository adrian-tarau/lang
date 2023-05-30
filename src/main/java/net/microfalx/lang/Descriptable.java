package net.microfalx.lang;

/**
 * An interface for an object which can be described.
 */
public interface Descriptable {

    /**
     * Returns the description.
     *
     * @return the description, null if it does not have a description
     */
    String getDescription();
}
