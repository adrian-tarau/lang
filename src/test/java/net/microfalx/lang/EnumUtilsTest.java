package net.microfalx.lang;

import net.microfalx.lang.annotation.Name;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EnumUtilsTest {

    @Test
    void fromName() {
        assertEquals(Enum1.A, EnumUtils.fromName(Enum1.class, "a"));
        assertEquals(Enum1.A, EnumUtils.fromName(Enum1.class, "A"));

        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A"));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A1"));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "a1"));
    }

    @Test
    void fromNameWithDefaultValue() {
        assertEquals(Enum1.B, EnumUtils.fromName(Enum1.class, "Z", Enum1.B));
        assertEquals(Enum1.C, EnumUtils.fromName(Enum1.class, "", Enum1.C));
        assertEquals(Enum2.A, EnumUtils.fromName(Enum2.class, "A", Enum2.A));
    }


    @Test
    void fromOrdinal(){
        assertEquals(Enum1.A,EnumUtils.fromOrdinal(Enum1.class,0));
        assertThrows(IllegalArgumentException.class,() -> EnumUtils.fromOrdinal(Enum1.class,-1));
    }

    @Test
    void fromOrdinalWithDefaultValue(){
        assertEquals(Enum1.A,EnumUtils.fromOrdinal(Enum1.class,0,Enum1.B));
        assertEquals(Enum1.B,EnumUtils.fromOrdinal(Enum1.class,-1,Enum1.B));
    }

    @Test
    void toName() {
        assertEquals("A", EnumUtils.toName(Enum1.A));
        assertNull(EnumUtils.toName(null));
        assertEquals("A1", EnumUtils.toName(Enum2.A));
    }

    @Test
    void toLabel() {
        assertEquals("C1", EnumUtils.toLabel(Enum2.C));
        assertNull(EnumUtils.toLabel(null));
        assertEquals("B1", EnumUtils.toLabel(Enum2.B));
    }

    @Test
    void toNames() {
        assertIterableEquals(Collections.EMPTY_LIST,EnumUtils.toNames( null));
        Collection<String> strings= new ArrayList<>();
        strings.add("A1");
        strings.add("B1");
        strings.add("C1");
        assertIterableEquals(CollectionUtils.immutableCollection(strings),EnumUtils.toNames( Enum2.class));
    }

    @Test
    void contains() {
        assertFalse(EnumUtils.contains(Enum1.A_B,Enum1.C,Enum1.A));
        assertFalse(EnumUtils.contains(null,Enum1.C));
        assertFalse(EnumUtils.contains(Enum1.A_B,null));
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