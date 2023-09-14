package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectUtilsTest {

    @Test
    void isNotNull() {
        assertFalse(ObjectUtils.isNotNull(null, null));
        assertFalse(ObjectUtils.isNotNull("a", null));
        assertFalse(ObjectUtils.isNotNull(null, "a"));
        assertTrue(ObjectUtils.isNotNull("b", "a"));
    }

    @Test
    void isNull() {
        assertTrue(ObjectUtils.isNull(null, null));
        assertTrue(ObjectUtils.isNull("a", null));
        assertTrue(ObjectUtils.isNull(null, "a"));
        assertFalse(ObjectUtils.isNull("b", "a"));
    }
}