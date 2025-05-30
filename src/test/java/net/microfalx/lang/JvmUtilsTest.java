package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JvmUtilsTest {

    @Test
    void isClient() {
        assertFalse(JvmUtils.isClient());
    }

    @Test
    void getUserName() {
        assertNotNull(JvmUtils.getUserName());
    }

    @Test
    void getHomeDirectory() {
        assertNotNull(JvmUtils.getHomeDirectory());
    }

    @Test
    void getVariableDirectory() {
        assertNotNull(JvmUtils.getVariableDirectory());
    }

    @Test
    void getWorkingDirectory() {
        assertNotNull(JvmUtils.getWorkingDirectory());
    }

    @Test
    void getTemporaryDirectory() {
        assertNotNull(JvmUtils.getTemporaryDirectory());
    }

    @Test
    void getSharedMemoryDirectory() {
        assertNotNull(JvmUtils.getSharedMemoryDirectory());
    }

    @Test
    void getCacheDirectory() {
        assertNotNull(JvmUtils.getCacheDirectory());
    }

    @Test
    void replacePlaceholders() {
        assertEquals("tmp", JvmUtils.replacePlaceholders("tmp"));
    }

    @Test
    void isHomeWritable() {
        assertTrue(JvmUtils.isHomeWritable());
    }


}