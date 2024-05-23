package net.microfalx.lang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HashingTest {

    private Hashing hashing;

    @BeforeEach
    void createHasher() {
        hashing = Hashing.create();
    }


    @Test
    void create() {
        assertNotNull(hashing);
    }

    @Test
    void get() {
        assertEquals("13d549740a4f94df8c5489eadb5075b7", Hashing.get("I am writing java code"));
    }

    @Test
    void updateWithNull() {
        assertNotNull(hashing.update(null));
    }

    @Test
    void updateWithString() {
        assertNotNull(hashing.update("I am writing java code"));
    }

    @Test
    void updateWithNumber() {
        assertNotNull(hashing.update(Integer.valueOf("7")));
        assertNotNull(hashing.update(Long.valueOf("7")));
        assertNotNull(hashing.update(Double.valueOf("7")));
    }

    @Test
    void updateWithByteArray() {
        assertNotNull(hashing.update(new byte[]{}));
    }

    @Test
    void updateWithEnum() {
        assertNotNull(hashing.update(EnumUtilsTest.Enum1.A));
    }
    @Test
    void updateWithTemporal() {
        assertNotNull(hashing.update(LocalDateTime.now()));
    }

    @Test
    void updateWithCollections() {
        assertNotNull(hashing.update(Collections.EMPTY_LIST));
        assertNotNull(hashing.update(Collections.EMPTY_MAP));
    }

    @Test
    void bits() {
        assertEquals(128, hashing.bits());
    }

    @Test
    void asInt() {
        assertEquals(0, hashing.asInt());
    }

    @Test
    void asLong() {
        assertEquals(0L, hashing.asLong());
    }

    @Test
    void asString() {
        assertEquals("00000000000000000000000000000000", hashing.asString());
    }

    @Test
    void asBytes() {
        assertNotNull(hashing.asBytes());
    }

    @Test
    void hash() {
        assertEquals("13d549740a4f94df8c5489eadb5075b7", Hashing.hash("I am writing java code"));
    }

    @Test
    void longToId() {
        assertEquals("0", Hashing.longToId("I am writing java code".getBytes(), 5));
    }
}