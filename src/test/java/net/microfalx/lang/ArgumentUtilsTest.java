package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentUtilsTest {

    @Test
    void requireNonNull() {
        assertNotNull(ArgumentUtils.requireNonNull(Integer.valueOf("1")));
        assertThrows(IllegalArgumentException.class, () -> ArgumentUtils.requireNonNull(null));
    }

    @Test
    void requireNotEmpty() {
        assertNotEquals("", ArgumentUtils.requireNotEmpty(" "));
        assertThrows(IllegalArgumentException.class, () -> ArgumentUtils.requireNotEmpty(""));
    }

    @Test
    void requireBoundedWithIntegers() {
        assertEquals(3, ArgumentUtils.requireBounded(3, 1, 5));
        assertThrows(IllegalArgumentException.class,() -> ArgumentUtils.requireBounded(0,1,5));
        assertThrows(IllegalArgumentException.class,() -> ArgumentUtils.requireBounded(6,1,5));
    }

    @Test
    void requireBoundedWithLongs() {
        assertEquals(3L, ArgumentUtils.requireBounded(3L, 1L, 5L));
        assertThrows(IllegalArgumentException.class,() -> ArgumentUtils.requireBounded(0L,1L,5L));
        assertThrows(IllegalArgumentException.class,() -> ArgumentUtils.requireBounded(6L,1L,5L));
    }
}