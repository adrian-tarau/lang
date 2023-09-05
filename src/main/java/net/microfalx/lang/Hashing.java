package net.microfalx.lang;

import com.google.common.hash.Hasher;

import java.nio.charset.StandardCharsets;

/**
 * A class which helps to calculate a hash from objects.
 */
@SuppressWarnings("UnstableApiUsage")
public final class Hashing {

    private final Hasher hasher = com.google.common.hash.Hashing.murmur3_128().newHasher();

    /**
     * Creates a new instance with a 128bit hashing algorithm.
     *
     * @return a non-null instance
     */
    public static Hashing create() {
        return new Hashing();
    }

    /**
     * Returns a hash value out of a single object.
     *
     * @param value the value to hash
     * @return a non-null string
     */
    public static String get(Object value) {
        return create().update(value).asString();
    }

    private Hashing() {
    }

    /**
     * Updates the hash with an object.
     *
     * @param value the value
     * @return self
     */
    public Hashing update(Object value) {
        if (value == null) {
            hasher.putLong(0);
        } else if (value instanceof String) {
            hasher.putString((String) value, StandardCharsets.UTF_8);
        } else if (value instanceof Number) {
            if (value instanceof Long) {
                hasher.putLong((Long) value);
            } else if (value instanceof Integer) {
                hasher.putLong((Integer) value);
            } else {
                hasher.putInt(((Number) value).intValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported type: " + ClassUtils.getName(value));
        }
        return this;
    }

    /**
     * Returns the number of bits in the hash.
     *
     * @return a positive integer
     */
    public int bits() {
        return hasher.hash().bits();
    }

    /**
     * Returns the hash as an integer.
     *
     * @return the hash
     */
    public long asInt() {
        return hasher.hash().asInt();
    }

    /**
     * Returns the hash as an long.
     *
     * @return the hash
     */
    public long asLong() {
        return hasher.hash().asLong();
    }

    /**
     * Returns the hash as an string.
     *
     * @return the hash
     */
    public String asString() {
        return hasher.hash().toString();
    }

    /**
     * Returns the hash as bytes.
     *
     * @return the hash
     */
    public byte[] asBytes() {
        return hasher.hash().asBytes();
    }

    /**
     * Calculates a hash of the value, usually an URI.
     *
     * @param value the value
     * @return a non-null instance
     */
    public static String hash(String value) {
        Hasher hasher = com.google.common.hash.Hashing.murmur3_128().newHasher();
        if (value != null) hasher.putBytes(value.getBytes());
        return hasher.hash().toString();
    }

    public static String longToId(byte[] data, int offset) {
        long value = data[offset++] + (long) data[offset++] << 8 + (long) data[offset++] << 16 + (long) data[offset++] << 24 + (long) data[offset++] << 32 + (long) data[offset++] << 40 + (long) data[offset++] << 48 + (long) data[offset++] << 56;
        return Long.toString(value, Character.MAX_RADIX);
    }
}
