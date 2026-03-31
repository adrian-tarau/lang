package net.microfalx.lang;

/**
 * A reference to
 *
 * @param <T> the identifier of the external reference.
 */
public interface ExternalReference<T> extends Identifiable<T> {

    /**
     * Creates an identifiable which carries only the identifier of a domain object
     * external to the current process.
     * <p>
     * Usually used instead of {@link Identifiable} to signal the external aspect of the identifier.
     *
     * @param id  the identifier
     * @param <T> the identifier type
     * @return a non-null instance
     */
    static <T> ExternalReference<T> create(T id) {
        return new ObjectUtils.ExternalReferenceImpl<>(id);
    }
}
