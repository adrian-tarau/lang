package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {


    @Test
    void isEmpty() {
        assertTrue(ObjectUtils.isEmpty(null));
        assertTrue(ObjectUtils.isEmpty(""));
        assertTrue(ObjectUtils.isEmpty(Collections.EMPTY_LIST));
        assertTrue(ObjectUtils.isEmpty(Collections.EMPTY_MAP));
        assertTrue(ObjectUtils.isEmpty(new Object[]{}));
        assertTrue(ObjectUtils.isEmpty(Optional.empty()));
    }


    @Test
    void isNotEmpty() {
        assertTrue(ObjectUtils.isNotEmpty(" "));
        assertTrue(ObjectUtils.isNotEmpty(Collections.singletonList(Integer.valueOf("7"))));
        assertTrue(ObjectUtils.isNotEmpty(Collections.singletonMap("word", 1)));
        assertTrue(ObjectUtils.isNotEmpty(new Object[]{Integer.valueOf("8")}));
        assertTrue(ObjectUtils.isNotEmpty(Optional.of(10)));
    }

    @Test
    void equals() {
        assertTrue(ObjectUtils.equals("", ""));
        assertFalse(ObjectUtils.equals(null, ""));
        assertFalse(ObjectUtils.equals("", null));
    }

    @Test
    void isSerializable() {
        assertTrue(ObjectUtils.isSerializable(Currency.class));
    }

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

    @Test
    void defaultIfNull() {
        assertEquals("This object is null", ObjectUtils.defaultIfNull(null,
                "This object is null"));
        assertEquals("This object is not null", ObjectUtils.defaultIfNull("This object is not null",
                "This object is null"));
    }

    @Test
    void getArrayLength() {
        assertEquals(0, ObjectUtils.getArrayLength(null));
        assertEquals(1, ObjectUtils.getArrayLength("I am writing java code"));
        assertEquals(5, ObjectUtils.getArrayLength(new String[]{"I", "am", "writing", "java", "code"}));
    }

    @Test
    void isArray() {
        assertTrue(ObjectUtils.isArray(new int[]{1, 2, 3, 4, 5}));
        assertFalse(ObjectUtils.isArray(null));
    }

    @Test
    void toArray() {
        assertEquals(ObjectUtils.EMPTY_ARRAY, ObjectUtils.toArray(null));
        assertNotNull(ObjectUtils.toArray("I am writing java code"));
    }

    @Test
    void toIdentifier() {
        assertEquals("java_lang_boolean_1237",ObjectUtils.toIdentifier(Boolean.FALSE));
        assertEquals("i_am_writing_java_code",ObjectUtils.toIdentifier("I am writing java code"));
    }


    @Test
    void toStringRepresentation() {
        assertNull(ObjectUtils.toString(null));
        assertEquals("7", ObjectUtils.toString(Integer.valueOf("7")));
    }

    @Test
    void copy() {
        assertEquals("1",ObjectUtils.copy(Integer.toBinaryString(1)));
        assertNull(ObjectUtils.copy(null));
    }

}