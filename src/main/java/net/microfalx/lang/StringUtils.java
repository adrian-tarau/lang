package net.microfalx.lang;

import java.util.StringTokenizer;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around strings.
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Returns whether the string is empty.
     *
     * @param value the string to validate
     * @return {@code true} if empty, @{code false} otherwise
     */
    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }

    /**
     * Returns whether the string is not empty.
     *
     * @param value the string to validate
     * @return {@code true} if empty, @{code false} otherwise
     */
    public static boolean isNotEmpty(CharSequence value) {
        return !isEmpty(value);
    }

    /**
     * Returns a default value if the input is null or empty.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the original value or a default
     */
    public static String defaultIfEmpty(String value, String defaultValue) {
        return isEmpty(value) ? defaultValue : value;
    }

    /**
     * Returns a default value if the input is null.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the original value or a default
     */
    public static String defaultIfNull(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Converts a String to an identifier.
     * <p>
     * The identifier contains only chars, digits and "_" (or "-" if allowed).
     *
     * @param value the path
     * @return the id
     */
    public static String toIdentifier(String value, boolean allowDash) {
        requireNonNull(value);

        StringBuilder builder = new StringBuilder(value.length());
        char prevChar = 0x00;
        for (int index = 0; index < value.length(); index++) {
            char c = value.charAt(index);
            if (Character.isDigit(c) || Character.isAlphabetic(c) || (allowDash && c == '-')) {
                builder.append(c);
            } else {
                c = '_';
                if (prevChar != c) {
                    builder.append(c);
                }
            }
            prevChar = c;
        }

        String identifier = builder.toString();
        if (identifier.startsWith("_")) {
            identifier = identifier.substring(1);
        }
        if (identifier.endsWith("_")) {
            identifier = identifier.substring(0, identifier.length() - 1);
        }
        return identifier.toLowerCase();
    }

    /**
     * Splits a string using a list of delimiters.
     *
     * @param string the string
     * @param delims the delimiters
     * @return a non-null instance
     */
    public static String[] split(String string, String delims) {
        if (string == null) return EMPTY_STRING_ARRAY;
        StringTokenizer st = new StringTokenizer(string, delims, false);
        String[] values = new String[st.countTokens()];
        int index = 0;
        while (st.hasMoreElements()) {
            String value = (String) st.nextElement();
            values[index++] = value;
        }

        return values;
    }

    /**
     * Removes backslash or forward slash from the value if found at the beginning of the string.
     *
     * @param value the string value
     * @return the string value without backslash or forward slash
     */
    public static String removeStartSlash(String value) {
        if (isEmpty(value)) return value;
        char c = value.charAt(0);
        if (c == '/' || c == '\\') return value.substring(1);
        return value;
    }



    /**
     * Removes backslash or forward slash from the value if found at the end of the string.
     *
     * @param value the string value
     * @return the string value without backslash or forward slash
     */
    public static String removeEndSlash(String value) {
        if (isEmpty(value)) return value;
        char c = value.charAt(value.length() - 1);
        if (c == '/' || c == '\\') return value.substring(0, value.length() - 1);
        return value;
    }

    /**
     * Adds forward slash at the end of the value, if needed.
     *
     * @param value the string value
     * @return the string value without backslash or forward slash
     */
    public static String addEndSlash(String value) {
        if (isEmpty(value)) return "/";
        char c = value.charAt(value.length() - 1);
        if (c != '/') value += "/";
        return value;
    }

    /**
     * Adds forward slash at the beginning of the value, if needed.
     *
     * @param value the string value
     * @return the string value without backslash or forward slash
     */
    public static String addStartSlash(String value) {
        if (isEmpty(value)) return "/";
        char c = value.charAt(0);
        if (c != '/') value = "/" + value;
        return value;
    }
}
