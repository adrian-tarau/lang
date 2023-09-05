package net.microfalx.lang;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.time.Duration.ofSeconds;
import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around concurrency.
 */
public class ConcurrencyUtils {

    /**
     * The default wait across all wait utilities.
     */
    public static final Duration DEFAULT_WAIT = ofSeconds(60);

    /**
     * Retruns whether the latch has reached the countdown to zero.
     *
     * @param latch the latch
     * @return <code>true</code> if it reached zero, <code>false</code> otherwise
     */
    public static boolean isComplete(CountDownLatch latch) {
        requireNonNull(latch);
        return latch.getCount() == 0;
    }

    /**
     * Waits for a latch to reach the end of the count-down for a maximum of {@link #DEFAULT_WAIT} seconds.
     *
     * @param latch the latch
     * @return <code>true</code> if the latched reached zero, <code>false</code> otherwise
     */
    public static boolean await(CountDownLatch latch) {
        return await(latch, DEFAULT_WAIT);
    }

    /**
     * Waits for a latch to reach the end of the count-down.
     *
     * @param latch    the latch
     * @param duration the duration, can be null to use default
     * @return <code>true</code> if the latched reached zero, <code>false</code> otherwise
     */
    @SuppressWarnings("DataFlowIssue")
    public static boolean await(CountDownLatch latch, Duration duration) {
        requireNonNull(latch);
        requireNonNull(duration);
        try {
            return latch.await(duration.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return ThreadUtils.throwException(e);
        }
    }

    /**
     * Waits for a latch to reach the end of the count-down.
     * <p>
     * If the latch did not reach the count-down, the consumer is called.
     *
     * @param latch the latch
     */
    public static void await(CountDownLatch latch, Duration duration, Consumer<CountDownLatch> consumer) {
        if (!await(latch, duration)) consumer.accept(latch);
    }
}
