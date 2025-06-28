package net.microfalx.lang;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static net.microfalx.lang.ArgumentUtils.requireNotEmpty;
import static net.microfalx.lang.ExceptionUtils.rethrowExceptionAndReturn;
import static net.microfalx.lang.StringUtils.isEmpty;

/**
 * Encryption utilities.
 */
public class EncryptionUtils {

    private static volatile String key = "GQsmX783Z5x2u2iA";
    private static volatile String seed = "x9qI636KL3DgrBnh";
    private static final String algorithm = "AES/CBC/PKCS5PADDING";

    /**
     * Returns the encryption key.
     *
     * @return a non-null instance
     */
    public static String getKey() {
        return key;
    }

    /**
     * Changes the encryption key.
     *
     * @param key the new key
     */
    public static void setKey(String key) {
        requireNotEmpty(key);
        EncryptionUtils.key = key;
    }

    /**
     * Returns the seed used to encrypt.
     *
     * @return a non-null instance
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Changes the seed.
     *
     * @param seed the new seed
     */
    public static void setSeed(String seed) {
        requireNotEmpty(seed);
        EncryptionUtils.seed = seed;
    }

    /**
     * Encrypts a text with a symmetric algorithm.
     *
     * @param value the value to encrypt
     * @return the encrypted value
     */
    public static String encrypt(String value) {
        return encrypt(value, key, seed);
    }

    /**
     * Encrypts a text with a symmetric algorithm.
     *
     * @param value the value to encrypt
     * @param key   the encryption key
     * @param seed  the seed
     * @return the encrypted value
     */
    public static String encrypt(String value, String key, String seed) {
        if (isEmpty(value)) return value;
        try {
            IvParameterSpec iv = new IvParameterSpec(seed.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return rethrowExceptionAndReturn(e);
        }
    }

    /**
     * Decrypts a text with a symmetric algorithm.
     *
     * @param value the value to decrypt
     * @return the decrypted value
     */
    public static String decrypt(String value) {
        return decrypt(value, key, seed);
    }

    /**
     * Decrypts a text with a symmetric algorithm.
     *
     * @param value the value to decrypt
     * @return the decrypted value
     */
    public static String decrypt(String value, String key, String seed) {
        if (isEmpty(value)) return value;
        try {
            IvParameterSpec iv = new IvParameterSpec(seed.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(value));
            return new String(original);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            // we presume this exceptions means "not encrypted"
            return value;
        } catch (Exception e) {
            return rethrowExceptionAndReturn(e);
        }
    }

    /**
     * Returns whether the text is encrypted.
     *
     * @param value the value to test
     * @return {@code true} if encrypted, {@code false} otherwise
     */
    public static boolean isEncrypted(String value) {
        String decryptedValue = decrypt(value);
        return !ObjectUtils.equals(value, decryptedValue);
    }
}
