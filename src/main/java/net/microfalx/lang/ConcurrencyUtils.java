package net.microfalx.lang;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofSeconds;
import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.ExceptionUtils.rethrowExceptionAndReturn;
import static net.microfalx.lang.FormatterUtils.formatDuration;
import static net.microfalx.lang.ThreadUtils.sleepMillis;

/**
 * Utilities around concurrency.
 */
public class ConcurrencyUtils {

    /**
     * The default wait across all wait utilities.
     */
    public static final Duration DEFAULT_WAIT = ofSeconds(30);

    /**
     * A maximum sleep time for exponential growth
     */
    private static final int MAX_SLEEP_TIME = 50;

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
     * Try to acquire the lock, waiting indefinitely.
     *
     * @param lock the lock
     * @return {@code true} if the lock was acquired, {@code false} otherwise
     * @throws InterruptedException if the thread was interrupted
     * @see Lock#tryLock(long, TimeUnit)
     */
    public static boolean tryLock(Lock lock) throws InterruptedException {
        return tryLock(lock, DEFAULT_WAIT);
    }

    /**
     * Try to acquire the lock-up to a given timeout.
     *
     * @param lock    the lock
     * @param timeout the maximum wait time
     * @return {@code true} if the lock was acquired, {@code false} otherwise
     * @throws InterruptedException if the thread was interrupted
     * @see Lock#tryLock(long, TimeUnit)
     */
    public static boolean tryLock(Lock lock, Duration timeout) throws InterruptedException {
        requireNonNull(lock);
        requireNonNull(timeout);
        return lock.tryLock(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Try to acquire the lock-up to a given timeout and if successful call the supplier.
     *
     * @param lock     the lock
     * @param supplier the supplier to invoke if the lock is acquired
     * @return the result, null if the lock could not be acquired
     * @throws InterruptedException if the thread was interrupted
     * @see Lock#tryLock(long, TimeUnit)
     */
    public static <T> Optional<T> withTryLock(Lock lock, Supplier<T> supplier) throws InterruptedException, TimeoutException {
        return withTryLock(lock, supplier, DEFAULT_WAIT);
    }

    /**
     * Try to acquire the lock up to a given timeout and if successful call the supplier.
     * <p>
     * If the lock cannot be acquired in the allowed time, the method will throw a {@link TimeoutException}.
     *
     * @param lock     the lock
     * @param supplier the supplier to invoke if the lock is acquired
     * @param timeout  the maximum wait time
     * @return the result, null if the lock could not be acquired
     * @throws InterruptedException if the thread was interrupted
     * @see Lock#tryLock(long, TimeUnit)
     */
    public static <T> Optional<T> withTryLock(Lock lock, Supplier<T> supplier, Duration timeout) throws InterruptedException, TimeoutException {
        requireNonNull(lock);
        requireNonNull(timeout);
        if (lock.tryLock(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
            try {
                return Optional.ofNullable(supplier.get());
            } finally {
                lock.unlock();
            }
        } else {
            throw new TimeoutException("Timeout waiting for lock '" + lock + "' to be acquired, timeout '" + formatDuration(timeout) + "'");
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
     * Waits for the condition to become true.
     *
     * @param condition the condition
     * @param timeout   the timeout to wait for all futures to be completed
     * @return {@code true} if the condition was met, {@code false} otherwise
     */
    public static boolean waitForCondition(AtomicBoolean condition, boolean reverse, Duration timeout) {
        return waitForCondition(condition::get, reverse, timeout);
    }

    /**
     * Waits for the condition to become true.
     *
     * @param condition the condition
     * @param timeout   the timeout to wait for all futures to be completed
     * @param reverse   {@code true} to revert the condition before evaluation, {@code false} otherwise
     * @return {@code true} if the condition was met, {@code false} otherwise
     */
    public static boolean waitForCondition(Supplier<Boolean> condition, boolean reverse, Duration timeout) {
        requireNonNull(condition);
        requireNonNull(timeout);
        float waitTime = 0.5f;
        long endTime = currentTimeMillis() + timeout.toMillis();
        while (currentTimeMillis() < endTime) {
            boolean satisfied = (!condition.get() && !reverse) || (condition.get() && reverse);
            if (satisfied) break;
            sleepMillis(waitTime);
            waitTime = Math.max(1.2f * waitTime, MAX_SLEEP_TIME);
        }
        return reverse != condition.get();
    }

    /**
     * Waits for the future to be competed (successful or not) up to a maximum time and returns the result.
     *
     * @param future the future
     * @param <T>    the future data type
     * @return the future
     * @see #DEFAULT_WAIT
     */
    public static <T> T getResult(Future<T> future) {
        return getResult(future, DEFAULT_WAIT);
    }

    /**
     * Waits for the future to be competed (successful or not) up to a maximum time and returns the result.
     *
     * @param future  the future
     * @param timeout the timeout to wait for all futures to be completed
     * @param <T>     the future data type
     * @return the future
     */
    public static <T> T getResult(Future<T> future, Duration timeout) {
        requireNonNull(future);
        waitForFutures(Collections.singleton(future), timeout);
        try {
            return future.get();
        } catch (Exception e) {
            return rethrowExceptionAndReturn(e);
        }
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
     * @return the number of futures which are still pending
     */
    public static int waitForFutures(Collection<? extends Future<?>> futures) {
        return waitForFutures(futures, DEFAULT_WAIT);
    }

    /**
     * Waits for all futures to be competed (successful or not) up to a maximum time.
     *
     * @param futures the futures
     * @param timeout the timeout to wait for all futures to be completed
     * @return the number of futures which are still pending
     */
    public static int waitForFutures(Collection<? extends Future<?>> futures, Duration timeout) {
        requireNonNull(futures);
        requireNonNull(timeout);
        float waitTime = 0.5f;
        int pending = 0;
        long endTime = currentTimeMillis() + timeout.toMillis();
        while (currentTimeMillis() < endTime) {
            pending = 0;
            for (Future<?> future : futures) {
                if (!future.isDone()) pending++;
            }
            if (pending == 0) break;
            sleepMillis(waitTime);
            waitTime = Math.min(1.2f * waitTime, MAX_SLEEP_TIME);
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
        requireNonNull(futures);
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
     * @param timeout     the timeout to wait for some of the futures to finish
     * @param minComplete the minimum number of futures to be completed before returns (or timeout)
     * @return the number of futures which are still pending
     */
    public static int drainFutures(Collection<? extends Future<?>> futures, Duration timeout, int minComplete) {
        requireNonNull(futures);
        minComplete = Math.max(1, Math.min(minComplete, futures.size()));
        float waitTime = 0.5f;
        long endTime = currentTimeMillis() + timeout.toMillis();
        Collection<Future<?>> doneFutures = new ArrayList<>();
        while (currentTimeMillis() < endTime) {
            for (Future<?> future : futures) {
                if (future.isDone()) doneFutures.add(future);
            }
            if (doneFutures.size() >= minComplete) break;
            sleepMillis(waitTime);
            waitTime = Math.min(1.2f * waitTime, MAX_SLEEP_TIME);
            doneFutures = new ArrayList<>();
        }
        for (Future<?> future : doneFutures) {
            futures.remove(future);
        }
        return doneFutures.size();
    }
}
