package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgumentUtilsTest {

    @Test
    void requireNonNull() {
        assertNotNull(ArgumentUtils.requireNonNull(Integer.valueOf("1")));
        assertThrows(IllegalArgumentException.class, () -> ArgumentUtils.requireNonNull(null));
    }

    @Test
    void requireNotEmpty() {

    }

    @Test
    void requireBounded() {
    }

    @Test
    void testRequireBounded() {
    }
}