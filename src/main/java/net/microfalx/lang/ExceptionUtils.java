package net.microfalx.lang;

import java.io.PrintWriter;
import java.io.StringWriter;

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

    /**
     * Marks the thread as interrupted and rethrows the exception.
     *
     * @param exception an interrupted exception
     * @return a fake return to avoid an unnecessary "return null" in functions when we know an exception will be raised
     */
    public static <T> T rethrowInterruptedException(InterruptedException exception) {
        Thread.currentThread().interrupt();
        throwException(exception);
        return null;
    }

    /**
     * Dumps an exception stacktrace as a string.
     *
     * @param throwable an exception
     * @return exception stacktrace, N/A if null
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) return "N/A";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void doThrowException(Throwable exception) throws E {
        requireNonNull(exception);
        throw (E) exception;
    }


}
