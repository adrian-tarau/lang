package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ThreadUtilsTest {

    @Test
    void sleep() {
        long start = System.nanoTime();
        ThreadUtils.sleep(Duration.ofSeconds(2));
        long duration = System.nanoTime() - start;
        assertThat(duration).isBetween(1_500_000_000L, 2_500_000_000L);
    }

    @Test
    void sleepMicros() {
        long start = System.nanoTime();
        ThreadUtils.sleepMicros(4.5F);
        long duration = System.nanoTime() - start;
        assertThat(duration).isBetween(1_000_000L, 6_000_000L);
    }

    @Test
    void sleepMillis() {
        long start = System.nanoTime();
        ThreadUtils.sleepMillis(2F);
        long duration = System.nanoTime() - start;
        assertThat(duration).isBetween(1_000_000L, 4_000_000L);
    }

    @Test
    void sleepSeconds() {
        long start = System.nanoTime();
        ThreadUtils.sleepSeconds(3);
        long duration = System.nanoTime() - start;
        assertThat(duration).isBetween(2_000_000_000L, 4_000_000_000L);
    }

    @Test
    void interrupt() {
        assertFalse(Thread.currentThread().isInterrupted());
        ThreadUtils.interrupt();
        assertTrue(Thread.currentThread().isInterrupted());

    }

    @Test
    void rethrowInterruptedException() throws InterruptedException {
        assertThrows(InterruptedException.class,
                () -> ThreadUtils.rethrowInterruptedException(new InterruptedException()));
    }

    @Test
    void throwException() {
        assertThrows(InterruptedException.class,
                () -> ThreadUtils.throwException(new InterruptedException()));
    }
}