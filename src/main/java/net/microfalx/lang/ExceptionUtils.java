package net.microfalx.lang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.StringUtils.*;

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
     * Returns the root cause description.
     * <p>
     * The root cause description has the root cause message and the type of failure in parentheses.
     *
     * @param throwable the exception
     * @return the description
     */
    public static String getRootCauseDescription(Throwable throwable) {
        return getRootCauseMessage(throwable) + " (" + getRootCauseName(throwable) + ")";
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
     * Returns the class name of the root cause exception.
     *
     * @param throwable the throwable
     * @return the root cause throwable class name
     */
    public static Class<? extends Throwable> getRootCauseClass(Throwable throwable) {
        Throwable rootCause = getRootCause(throwable);
        return rootCause != null ? rootCause.getClass() : throwable.getClass();
    }

    /**
     * Returns a label our of the root cause exception, mainly used to track the exception as a counter.
     *
     * @param throwable the throwable
     * @return the root cause label
     */
    public static String getRootCauseName(Throwable throwable) {
        if (throwable == null) return NA_STRING;
        Class<? extends Throwable> rootCauseClass = getRootCauseClass(throwable);
        String exceptionClassName = rootCauseClass.getSimpleName();
        if (exceptionClassName.endsWith("Exception")) {
            exceptionClassName = exceptionClassName.substring(0, exceptionClassName.length() - 9);
        }
        String alias = EXCEPTION_NAME_ALIAS.get(exceptionClassName);
        return alias != null ? alias : beautifyCamelCase(exceptionClassName);
    }

    /**
     * Returns a label our of the root cause exception, mainly used to track the exception as a counter.
     *
     * @param exceptionClassName the throwable
     * @return the root cause label
     */
    public static String getRootCauseName(String exceptionClassName) {
        if (exceptionClassName == null) return NA_STRING;
        exceptionClassName = ClassUtils.getSimpleName(exceptionClassName);
        if (exceptionClassName.endsWith("Exception")) {
            exceptionClassName = exceptionClassName.substring(0, exceptionClassName.length() - 9);
        }
        String alias = EXCEPTION_NAME_ALIAS.get(exceptionClassName);
        return alias != null ? alias : beautifyCamelCase(exceptionClassName);
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

    private static Map<String, String> EXCEPTION_NAME_ALIAS = new HashMap<>();

    static {
        EXCEPTION_NAME_ALIAS.put("IO", "I/O");
    }


}
