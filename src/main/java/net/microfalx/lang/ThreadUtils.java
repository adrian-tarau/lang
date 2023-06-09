package net.microfalx.lang;

import java.time.Duration;

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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void sleepMicros(long micros) {
        try {
            Thread.sleep(0L, (int) (micros * 1000));
        } catch (InterruptedException e) {
            ExceptionUtils.throwException(e);
        }
    }

    /**
     * Sleeps a number of milliseconds.
     *
     * @param millis the number of milliseconds
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            ExceptionUtils.throwException(e);
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
}
