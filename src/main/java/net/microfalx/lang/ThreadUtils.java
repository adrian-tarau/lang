package net.microfalx.lang;

import java.time.Duration;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.ExceptionUtils.rethrowException;

/**
 * Utilities around threads.
 */
public class ThreadUtils {

    /**
     * Sleeps for a given amount of time.
     *
     * @param duration the duration
     */
    public static void sleep(Duration duration) {
        if (duration == null) return;
        sleepMillis(duration.toMillis());
    }

    /**
     * Sleeps a number of microseconds.
     *
     * @param micros the number of microseconds
     */
    public static void sleepMicros(float micros) {
        try {
            Thread.sleep(0L, (int) (micros * 1000));
        } catch (InterruptedException e) {
            rethrowException(e);
        }
    }

    /**
     * Sleeps a number of milliseconds.
     *
     * @param millis the number of milliseconds
     */
    public static void sleepMillis(float millis) {
        try {
            Thread.sleep((long) millis);
        } catch (InterruptedException e) {
            rethrowException(e);
        }
    }

    /**
     * Sleeps a number of seconds.
     *
     * @param seconds the number of seconds
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void sleepSeconds(int seconds) {
        sleepMillis(seconds * 1000L);
    }

    /**
     * Interrupts current thread.
     */
    public static void interrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Marks the thread as interrupted and rethrows the exception
     *
     * @param exception an interrupted exception
     */
    @SuppressWarnings({"SameReturnValue", "ResultOfMethodCallIgnored"})
    public static <T> T rethrowInterruptedException(InterruptedException exception) {
        Thread.currentThread().interrupt();
        throwException(exception);
        return null;
    }

    /**
     * Rethrow a checked exception
     *
     * @param exception an exception
     */
    @SuppressWarnings("SameReturnValue")
    public static <T> T throwException(Throwable exception) {
        ThreadUtils.doThrowException(exception);
        return null;
    }

    /**
     * Returns the top of the call stack (what is executing right now).
     *
     * @param thread the thread, can be NULL
     * @return the top stack, null if not available
     */
    public static String getTopStack(Thread thread) {
        if (thread == null) return null;
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        if (stackTraceElements.length == 0) return null;
        StackTraceElement stackTraceElement = stackTraceElements[0];
        return getStackTraceDescription(stackTraceElement);
    }

    /**
     * Returns whether the thread is running native code.
     *
     * @param thread the thread, can be NULL
     * @return {@code true} if in native code, {@code false} otherwise
     */
    public static boolean isInNative(Thread thread) {
        if (thread == null) return false;
        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        if (stackTraceElements.length == 0) return false;
        return stackTraceElements[0].isNativeMethod();
    }

    /**
     * Returns a description of a stack trace element.
     *
     * @param stackTraceElement the stack trace
     * @return the description, null if the stack is null
     */
    public static String getStackTraceDescription(StackTraceElement stackTraceElement) {
        if (stackTraceElement == null) return null;
        String name = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
        if (stackTraceElement.getLineNumber() > 0) name += ":" + stackTraceElement.getLineNumber();
        return name;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void doThrowException(Throwable exception) throws E {
        requireNonNull(exception);
        throw (E) exception;
    }
}
