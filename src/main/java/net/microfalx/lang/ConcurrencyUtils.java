package net.microfalx.lang;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
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
     * A maximum sleep time for exponential growth
     */
    private static final int MAX_SLEEP_TIME = 10;

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

    /**
     * Waits for the future to be competed (successful or not) up to a maximum time.
     *
     * @param future  the future
     * @param timeout the timeout to wait for all futures to be completed
     * @param <T>     the future data type
     * @return the future
     */
    public static <T> Future<T> waitForFuture(Future<T> future, Duration timeout) {
        waitForFutures(Collections.singleton(future), timeout);
        return future;
    }

    /**
     * Waits for all futures to be competed (successful or not) for up to 30 seconds.
     *
     * @param futures the futures
     * @param <T>     the future data type
     * @return the number of futures which are still pending
     */
    public static <T> int waitForFutures(Collection<? extends Future<T>> futures) {
        return waitForFutures(futures, Duration.ofSeconds(TimeUtils.THIRTY_SECONDS));
    }

    /**
     * Waits for all futures to be competed (successful or not) up to a maximum time.
     *
     * @param futures the futures
     * @param timeout the timeout to wait for all futures to be completed
     * @param <T>     the future data type
     * @return the number of futures which are still pending
     */
    public static <T> int waitForFutures(Collection<? extends Future<T>> futures, Duration timeout) {
        requireNonNull(futures);
        requireNonNull(timeout);
        float waitTime = 0.5f;
        int pending = 0;
        long endTime = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < endTime) {
            pending = 0;
            for (Future<T> future : futures) {
                if (!future.isDone()) pending++;
            }
            if (pending == 0) break;
            ThreadUtils.sleepMillis(waitTime);
            waitTime = Math.max(1.2f * waitTime, MAX_SLEEP_TIME);
        }
        return pending;
    }

    /**
     * Collects the results for all successful futures (ignores anything else).
     *
     * @param futures the futures
     * @param <T>     the result type
     * @return a non-null instance
     */
    public static <T> Collection<T> collectFutures(Collection<? extends Future<T>> futures) {
        ArgumentUtils.requireNonNull(futures);
        Collection<T> values = new ArrayList<>();
        for (Future<T> future : futures) {
            if (future.isDone()) {
                try {
                    values.add(future.get());
                } catch (Exception e) {
                    // don't care about reason, give what can be collected
                }
            }
        }
        return values;
    }

    /**
     * Waits for some futures to be competed (successful or not) up to a maximum time.
     * <p>
     * The method will also remove completed futures from the list of futures.
     *
     * @param futures     the futures
     * @param timeOut     the timeout to wait for some of the futures to finish
     * @param minComplete the minimum number of futures to be completed before returns (or timeout)
     * @param <T>         the future data type
     * @return the number of futures which are still pending
     */
    public static <T> int drainFutures(Collection<? extends Future<T>> futures, long timeOut, int minComplete) {
        ArgumentUtils.requireNonNull(futures);
        minComplete = Math.max(1, Math.min(minComplete, futures.size()));
        float waitTime = 0.5f;
        long endTime = System.currentTimeMillis() + timeOut;
        Collection<Future<?>> doneFutures = new ArrayList<>();
        while (System.currentTimeMillis() < endTime) {
            for (Future<T> future : futures) {
                if (future.isDone()) doneFutures.add(future);
            }
            if (doneFutures.size() >= minComplete) break;
            ThreadUtils.sleepMillis(waitTime);
            waitTime = Math.max(1.2f * waitTime, MAX_SLEEP_TIME);
            doneFutures = new ArrayList<>();
        }
        for (Future<?> future : doneFutures) {
            futures.remove(future);
        }
        return doneFutures.size();
    }
}
