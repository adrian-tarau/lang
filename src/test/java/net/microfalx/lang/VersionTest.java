package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionTest {

    @Test
    void parseSimple() {
        assertEquals("1.0", Version.parse("1.0").toString());
        assertEquals("1.0", Version.parse("1.0").toTag());
        assertEquals("1.0", Version.parse("1.0-SNAPSHOT").toString());
        assertEquals("1.0.latest", Version.parse("1.0-SNAPSHOT").toTag());
        assertEquals("1.0.0", Version.parse("1.0.0").toString());
        assertEquals("1.0.0", Version.parse("1.0.0-SNAPSHOT").toString());
        assertEquals("1.0.0", Version.parse("1.0.0").toTag());
        assertEquals("1.0.latest", Version.parse("1.0.0-SNAPSHOT").toTag());
        assertEquals("1.0.1", Version.parse("1.0.1").toString());
        assertEquals("1.0.1", Version.parse("1.0.1").toTag());
        assertEquals("2.3.11", Version.parse("2.3.11").toString());
        assertEquals("2.3.11", Version.parse("2.3.11").toTag());
    }

    @Test
    void parseBuild() {
        assertEquals("1.0.0+10", Version.parse("1.0.0+10").toString());
    }

    @Test
    void parsePreRelease() {
        assertEquals("1.0.0-2", Version.parse("1.0.0-2").toString());
    }

    @Test
    void withBuild() {
        assertEquals("1.0.0+123", Version.parse("1.0.0").withBuild(123).toString());
    }

    @Test
    void compareTo() {
        assertEquals(0, Version.parse("1.0").compareTo(Version.parse("1.0")));
        assertEquals(-1, Version.parse("1.0").compareTo(Version.parse("1.1")));
        assertEquals(1, Version.parse("1.1").compareTo(Version.parse("1.0")));
        assertEquals(0, Version.parse("1.0.0").compareTo(Version.parse("1.0.0")));
        assertEquals(-1, Version.parse("1.0.0").compareTo(Version.parse("1.0.1")));
        assertEquals(1, Version.parse("1.0.1").compareTo(Version.parse("1.0.0")));
        assertEquals(-1, Version.parse("1.0.0+10").compareTo(Version.parse("1.0.0+20")));
        assertEquals(1, Version.parse("1.0.0+20").compareTo(Version.parse("1.0.0+10")));
    }

}