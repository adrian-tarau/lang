package net.microfalx.lang;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("cbc357ccb763df2852fee8c4fc7d55f2", hashing.update(null).asString());
    }

    @Test
    void updateWithString() {
        assertEquals("13d549740a4f94df8c5489eadb5075b7", hashing.update("I am writing java code").asString());
    }

    @Test
    void updateWithInteger() {
        assertEquals("fb1d467eb669277fa55b582f5c2215d2", hashing.update(7).asString());
    }

    @Test
    void updateWithLong() {
        assertEquals("b28f7f259329e130556e3f9718717e0e", hashing.update(7L).asString());
    }

    @Test
    void updateWithFloat() {
        assertEquals("4cad39b2a8037d0fcc02d711e17e616b", hashing.update(10.5).asString());
    }

    @Test
    void updateWithDouble() {
        assertEquals("64af85a0f547925b8d275675da3cb6aa", hashing.update(7.5d).asString());
    }

    @Test
    void updateWithByteArray() {
        assertArrayEquals(new byte[]{14, -122, -104, -85, 27, -93, -108, -2, -83, 26, -101, -41, -112, 76, -116, -22},
                hashing.update(new byte[]{1, 2, 3, 4, 5}).asBytes());
    }

    @Test
    void updateWithEnum() {
        assertEquals("7ab1299ab7c25f033799dd469cf27d38",hashing.update(EnumUtilsTest.Enum1.A).asString());
    }

    @Test
    void updateWithTemporal() {
        assertEquals("d7d721d51d2deadcf9973f75f96b48e1",hashing.update(LocalDate.of(2020,7,
                6)).asString());
    }

    @Test
    void updateWithCollection() {
        assertEquals("0bbcef46454e4ab3aa08886b85e7c5a8",hashing.update(List.of(1,2,3,4,5)).asString());
    }

    @Test
    void updateWithMap(){
        hashing.update(new TreeMap<>(Map.of(0,1,1,2,
                2,3)));
        assertEquals("6d2d0c8ff3e97663de4b5ab964be4a72",hashing.asString());
    }

    @Test
    void updateWithInputStream() {
        hashing.update(new ByteArrayInputStream(new byte[]{1,2,3,4,5}));
        assertEquals("6cabbcb76703edb57fc0ac4ded509779",hashing.asString());
    }

    @Test
    void updateWithReader() {
        hashing.update(new StringReader("test"));
        assertEquals("f4550809b2d30e648d672dea814d2408",hashing.asString());
    }

    @Test
    void bits() {
        hashing.update(100);
        assertEquals(128, hashing.bits());
    }

    @Test
    void asInt() {
        hashing.update(1);
        assertEquals(-1356281090, hashing.asInt());
    }

    @Test
    void asLong() {
        hashing.update(500);
        assertEquals(-8775179033787206680L, hashing.asLong());
    }

    @Test
    void asString() {
        hashing.update(8000);
        assertEquals("66cf7b0725b9170c50e83f0d1dc8fad0", hashing.asString());
    }

    @Test
    void asBytes() {
        hashing.update(1);
        assertArrayEquals(new byte[]{-2,-54,40,-81,-11,-93,-107,-120,64,-66,-23,-123,-18,125,-28,-45},hashing.asBytes());
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