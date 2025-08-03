package net.microfalx.lang;

import com.google.common.hash.Hasher;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Map;

import static net.microfalx.lang.ExceptionUtils.rethrowException;

/**
 * A class which helps to calculate a hash from objects.
 */
@SuppressWarnings("UnstableApiUsage")
public final class Hashing {

    private final Hasher hasher = com.google.common.hash.Hashing.murmur3_128().newHasher();

    /**
     * An empty hash
     */
    public static String EMPTY = Hashing.get(null);

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
            updateNumber((Number) value);
        } else if (value instanceof Boolean) {
            hasher.putBoolean((Boolean) value);
        } else if (value instanceof byte[]) {
            hasher.putBytes((byte[]) value);
        } else if (value instanceof char[]) {
            hasher.putString(String.valueOf((char[]) value), StandardCharsets.UTF_8);
        } else if (value instanceof Enum) {
            hasher.putString(((Enum<?>) value).name(), StandardCharsets.UTF_8);
        } else if (value instanceof Temporal) {
            updateTemporal((Temporal) value);
        } else {
            updateOther(value);
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
    public int asInt() {
        return hasher.hash().asInt();
    }

    /**
     * Returns the hash as a long.
     *
     * @return the hash
     */
    public long asLong() {
        return hasher.hash().asLong();
    }

    /**
     * Returns the hash as a string.
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

    private void updateNumber(Number number) {
        if (number instanceof Integer) {
            hasher.putInt((Integer) number);
        } else if (number instanceof Long) {
            hasher.putLong((Long) number);
        } else if (number instanceof Float) {
            hasher.putFloat((Float) number);
        } else if (number instanceof Double) {
            hasher.putDouble((Double) number);
        } else {
            hasher.putInt(number.intValue());
        }
    }

    private void updateTemporal(Temporal temporal) {
        ZonedDateTime zonedDateTime = TimeUtils.toZonedDateTime(temporal);
        hasher.putLong(zonedDateTime.toInstant().toEpochMilli());
    }

    private void updateOther(Object value) {
        if (value instanceof Collection<?>) {
            for (Object cvalue : ((Collection<?>) value)) {
                update(cvalue);
            }
        } else if (value instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                update(entry.getKey());
                update(entry.getValue());
            }
        } else if (value instanceof InputStream) {
            update((InputStream) value);
        } else if (value instanceof URI) {
            update(((URI) value).toASCIIString());
        } else if (value instanceof URL) {
            update(((URL) value).toExternalForm());
        } else if (value instanceof Reader) {
            update((Reader) value);
        } else if (ObjectUtils.isArray(value)) {
            ObjectUtils.forEach(value, this::update);
        } else {
            throwUnsupportedType(value);
        }
    }

    private void update(InputStream inputStream) {
        byte[] buffer = new byte[IOUtils.BUFFER_SIZE];
        try {
            int length;
            do {
                length = inputStream.read(buffer, 0, buffer.length);
                if (length > 0) update(buffer);
            } while (length > 0);
        } catch (IOException e) {
            rethrowException(e);
        }
    }

    private void update(Reader reader) {
        char[] buffer = new char[IOUtils.BUFFER_SIZE];
        try {
            int length;
            do {
                length = reader.read(buffer, 0, buffer.length);
                if (length > 0) update(buffer);
            } while (length > 0);
        } catch (IOException e) {
            rethrowException(e);
        }
    }

    private void throwUnsupportedType(Object value) {
        throw new IllegalArgumentException("Unsupported type: " + ClassUtils.getName(value));
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
