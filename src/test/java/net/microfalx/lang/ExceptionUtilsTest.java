package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionUtilsTest {

    @Test
    void getRootCause() {
    }

    @Test
    void getRootCauseClass() {
    }

    @Test
    void getRootCauseName() {
        assertEquals("Illegal State",ExceptionUtils.getRootCauseName(new IllegalStateException("Demo")));
        assertEquals("I/O",ExceptionUtils.getRootCauseName(new IOException("Demo")));
    }
}