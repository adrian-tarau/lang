package net.microfalx.lang;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around exceptions.
 */
public class ExceptionUtils {

    /**
     * Rethrow a checked exception
     *
     * @param exception an exception
     */
    @SuppressWarnings("SameReturnValue")
    public static <T> T throwException(Throwable exception) {
        doThrowException(exception);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void doThrowException(Throwable exception) throws E {
        requireNonNull(exception);
        throw (E) exception;
    }

}
