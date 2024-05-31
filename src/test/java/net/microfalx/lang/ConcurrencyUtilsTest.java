package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static net.microfalx.lang.ThreadUtils.sleepMillis;
import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyUtilsTest {

    private static final Duration SMALL_WAIT = ofSeconds(1);
    private static final Duration VERY_SMALL_WAIT = ofMillis(50);

    private static final String MESSAGE = "I am writing java code";

    @Test
    void isComplete() {
        CountDownLatch latch = new CountDownLatch(2);
        assertFalse(ConcurrencyUtils.isComplete(latch));
        latch.countDown();
        assertFalse(ConcurrencyUtils.isComplete(latch));
        latch.countDown();
        assertTrue(ConcurrencyUtils.isComplete(latch));
    }


    @Test
    void await() {
        CountDownLatch latch = new CountDownLatch(1);
        assertFalse(ConcurrencyUtils.await(latch));
        latch.countDown();
        assertTrue(ConcurrencyUtils.await(latch));
    }

    @Test
    void awaitWithDuration() {
        CountDownLatch latch = new CountDownLatch(1);
        assertFalse(ConcurrencyUtils.await(latch, SMALL_WAIT));
        latch.countDown();
        assertTrue(ConcurrencyUtils.await(latch, SMALL_WAIT));
        ThreadUtils.interrupt();
        assertThrows(Exception.class, () -> ConcurrencyUtils.await(new CountDownLatch(31), ofSeconds(2)));
    }

    @Test
    void tryLock() throws InterruptedException {
        // try to lock and unlock in the same thread
        ReentrantLock lock = new ReentrantLock();
        assertTrue(ConcurrencyUtils.tryLock(lock));
        lock.unlock();
        // lock the thread in a different thread and they try to lock in the main thread
        new ThreadWithLock(lock).start();
        sleepMillis(1000);
        assertFalse(ConcurrencyUtils.tryLock(lock, VERY_SMALL_WAIT));
    }

    @Test
    void tryLockWithTimeOut() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        assertTrue(ConcurrencyUtils.tryLock(lock, ofSeconds(1)));
        lock.unlock();
    }

    @Test
    void withTryLock() throws InterruptedException, TimeoutException {
        ReentrantLock lock = new ReentrantLock();
        assertEquals(31, ConcurrencyUtils.withTryLock(lock, () -> 31).orElse(0));
    }

    @Test
    void withTryLockWithTimeOut() throws InterruptedException, TimeoutException {
        ReentrantLock lock = new ReentrantLock();
        new ThreadWithLock(lock).start();
        sleepMillis(1000);
        assertThrows(TimeoutException.class, () -> {
            ConcurrencyUtils.withTryLock(lock, () -> 31,
                    ofMillis(100)).get();
        });
    }

    @Test
    void awaitWithLatchStillActive() {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean consumerCalled = new AtomicBoolean();
        ConcurrencyUtils.await(latch, ofMillis(100), l -> {
            consumerCalled.set(true);
        });
        assertTrue(consumerCalled.get());
    }

    @Test
    void awaitWithLatchReachedZero() {
        CountDownLatch latch = new CountDownLatch(1);
        latch.countDown();
        AtomicBoolean consumerCalled = new AtomicBoolean();
        ConcurrencyUtils.await(latch, ofMillis(100), l -> {
            consumerCalled.set(true);
        });
        assertFalse(consumerCalled.get());
    }

    @Test
    void waitForCondition() {
        assertFalse(ConcurrencyUtils.waitForCondition(new AtomicBoolean(false), false, ofSeconds(1)));
        assertTrue(ConcurrencyUtils.waitForCondition(new AtomicBoolean(true), false, ofSeconds(1)));

        assertTrue(ConcurrencyUtils.waitForCondition(new AtomicBoolean(false), true, ofSeconds(1)));
        assertFalse(ConcurrencyUtils.waitForCondition(new AtomicBoolean(true), true, ofSeconds(1)));
    }

    @Test
    void waitForConditionWithSupplier() {
        assertFalse(ConcurrencyUtils.waitForCondition(() -> false, false, ofSeconds(1)));
        assertTrue(ConcurrencyUtils.waitForCondition(() -> true, false, ofSeconds(1)));

        assertTrue(ConcurrencyUtils.waitForCondition(() -> false, true, ofSeconds(1)));
        assertFalse(ConcurrencyUtils.waitForCondition(() -> true, true, ofSeconds(1)));
    }

    @Test
    void getResult() {
        Future<String> future = CompletableFuture.completedFuture("I am writing java code");
        assertEquals("I am writing java code", ConcurrencyUtils.getResult(future));
        Future<Void> incompleteFuture = executeWithDelay(Duration.ofMillis(200));
        assertFalse(incompleteFuture.isDone());
        sleepMillis(1_000);
        assertTrue(incompleteFuture.isDone());
    }

    @Test
    void getResultWithTimeOut() {
        Future<String> future = CompletableFuture.completedFuture(MESSAGE);
        assertEquals(MESSAGE, ConcurrencyUtils.getResult(future, ofSeconds(2)));
    }

    @Test
    void waitForFuture() throws ExecutionException, InterruptedException {
        Future<String> future = CompletableFuture.completedFuture(MESSAGE);
        assertEquals(MESSAGE, ConcurrencyUtils.waitForFuture(future, ofSeconds(2)).get());
        assertTrue(future.isDone());

        future = executeWithDelay(ofMillis(500), MESSAGE);
        assertEquals(MESSAGE, ConcurrencyUtils.waitForFuture(future, ofSeconds(2)).get());
        assertTrue(future.isDone());

        future = executeWithDelay(ofSeconds(5), MESSAGE);
        ConcurrencyUtils.waitForFuture(future, ofSeconds(2));
        assertFalse(future.isDone());
    }

    @Test
    void waitForFutures() {
        Future<String> future1 = CompletableFuture.completedFuture(MESSAGE);
        Future<String> future2 = CompletableFuture.completedFuture(MESSAGE);
        assertEquals(0, ConcurrencyUtils.waitForFutures(Arrays.asList(future1, future2)));

        future1 = executeWithDelay(ofSeconds(2), MESSAGE);
        future2 = executeWithDelay(ofSeconds(4), MESSAGE);
        assertEquals(0, ConcurrencyUtils.waitForFutures(Arrays.asList(future1, future2)));
    }

    @Test
    void waitForFuturesWithTimeOut() {
        Future<String> future = CompletableFuture.completedFuture(MESSAGE);
        assertEquals(0, ConcurrencyUtils.waitForFutures(Collections.singletonList(future),
                ofSeconds(6)));
    }

    @Test
    void collectFutures() {
        Future<String> future1 = CompletableFuture.completedFuture(MESSAGE);
        assertEquals(Collections.singletonList(MESSAGE),
                ConcurrencyUtils.collectFutures(Collections.singletonList(future1)));

        Future<String> future2 = CompletableFuture.failedFuture(new IOException("Something is wrong"));
        assertEquals(Collections.singletonList(MESSAGE),
                ConcurrencyUtils.collectFutures(Arrays.asList(future1, future2)));
    }

    @Test
    void drainFuturesAllDone() {
        Future<String> future1 = CompletableFuture.completedFuture(MESSAGE);
        Future<String> future2 = CompletableFuture.completedFuture(MESSAGE);
        Future<String> future3 = CompletableFuture.completedFuture(MESSAGE);
        List<Future<String>> list = new ArrayList<>(Arrays.asList(future1,future2, future3));
        assertEquals(3, ConcurrencyUtils.drainFutures(list, Duration.ofMillis(500), 1));
    }

    @Test
    void drainFuturesSomeDone() {
        Future<String> future1 = CompletableFuture.completedFuture(MESSAGE);
        Future<String> future2 = CompletableFuture.completedFuture(MESSAGE);
        Future<String> future3 = executeWithDelay(Duration.ofSeconds(5), MESSAGE);
        List<Future<String>> list = new ArrayList<>(Arrays.asList(future1,future2, future3));
        assertEquals(2, ConcurrencyUtils.drainFutures(list, Duration.ofMillis(500), 1));
    }

    private Future<Void> executeWithDelay(Duration duration) {
        return executeWithDelay(duration, null);
    }

    private <T> Future<T> executeWithDelay(Duration duration, T value) {
        return CompletableFuture.supplyAsync(() -> {
            sleepMillis(duration.toMillis());
            return value;
        });
    }

    static class ThreadWithLock extends Thread {

        private final Lock lock;
        private final AtomicBoolean hasLock = new AtomicBoolean();

        public ThreadWithLock(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                hasLock.set(true);
                // this is some code to simulate a task which takes 2 seconds to run
                ThreadUtils.sleep(ofSeconds(2));
            } finally {
                lock.unlock();
            }
            hasLock.set(false);
        }
    }


}