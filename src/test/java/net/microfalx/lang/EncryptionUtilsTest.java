package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilsTest {

    @Test
    void encryptAndDecrypt() {
        String value = EncryptionUtils.encrypt("demo");
        assertEquals("demo", EncryptionUtils.decrypt(value));
    }

    @Test
    void isEncrypted() {
        assertFalse(EncryptionUtils.isEncrypted("demo"));
        assertTrue(EncryptionUtils.isEncrypted(EncryptionUtils.encrypt("demo")));
    }


}