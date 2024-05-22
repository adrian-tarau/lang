package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JvmUtilsTest {

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
    void replacePlaceholders() {
        assertEquals("lsa123",JvmUtils.replacePlaceholders("lsa123"));
    }
}