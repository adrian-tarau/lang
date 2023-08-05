package net.microfalx.lang;

import net.microfalx.lang.annotation.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumUtilsTest {

    @Test
    void fromName() {
        assertEquals(Enum1.A, EnumUtils.fromName(Enum1.class, "a"));
        assertEquals(Enum1.A, EnumUtils.fromName(Enum1.class, "A"));
        assertEquals(Enum1.B, EnumUtils.fromName(Enum1.class, "Z", Enum1.B));

        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A"));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A1"));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "a1"));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A", Enum2.A));
    }

    @Test
    void toName() {
        assertEquals("A", EnumUtils.toName(Enum1.A));
        assertEquals("A1", EnumUtils.toName(Enum2.A));
    }

    enum Enum1 {
        A,
        B,
        C,
        A_B
    }

    enum Enum2 {

        @Name("A1")
        A,

        @Name("B1")
        B,

        @Name("C1")
        C
    }

}