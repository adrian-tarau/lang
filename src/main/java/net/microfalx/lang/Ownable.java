package net.microfalx.lang;

/**
 * An interface for an object which can be owned by another object.
 */
public interface Ownable<T> {

    /**
     * Returns the owner that created the object.
     *
     * @return the owner, null if not known
     */
    T getCreatedBy();

    /**
     * Returns the owner that updated the object last time.
     *
     * @return the owner, null if not known
     */
    T getModifiedBy();
}
