package net.microfalx.lang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.StringUtils.NA_STRING;
import static net.microfalx.lang.StringUtils.defaultIfEmpty;

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
        return throwException(exception);
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

    /**
     * Returns the exception message from the root exception.
     *
     * @param throwable the exception
     * @return the message
     * @see org.apache.commons.lang3.exception.ExceptionUtils#getRootCauseMessage(Throwable)
     */
    public static String getRootCauseMessage(Throwable throwable) {
        return defaultIfEmpty(org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage(throwable), NA_STRING);
    }

    /**
     * Returns the root cause exception.
     *
     * @param throwable the throwable
     * @return the root cause exception
     * @see org.apache.commons.lang3.exception.ExceptionUtils#getRootCause
     */
    public static Throwable getRootCause(Throwable throwable) {
        return org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(throwable);
    }

    /**
     * Returns the SQL exception if the root cause exception is a database exception
     *
     * @param throwable the exception
     * @return the vendor specific error code, -1 if not a SQL exception or cannot be extracted
     */
    public static int getSQLErrorCode(Throwable throwable) {
        Throwable rootCause = getRootCause(throwable);
        return rootCause instanceof SQLException ? ((SQLException) rootCause).getErrorCode() : -1;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void doThrowException(Throwable exception) throws E {
        requireNonNull(exception);
        throw (E) exception;
    }


}
