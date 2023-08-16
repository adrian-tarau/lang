package net.microfalx.lang;

import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around strings.
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String NA_STRING = "N/A";
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
     * The identifier contains only chars, digits and "_".
     *
     * @param value the String value
     * @return the id
     */
    @SuppressWarnings("Duplicates")
    public static String toIdentifier(String value) {
        return toIdentifier(value, false);
    }

    /**
     * Converts a string to lower case.
     *
     * @param value the value, can be null
     * @return the string lower case
     */
    public static String toLowerCase(String value) {
        return value != null ? value.toLowerCase() : null;
    }

    /**
     * Converts a string to upper case.
     *
     * @param value the value, can be null
     * @return the string upper case
     */
    public static String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }

    /**
     * Returns a string containing the same character Nth times.
     *
     * @param c      the character
     * @param length the number of characters
     * @return a non-null instance
     */
    public static String getStringOfChar(char c, int length) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buffer.append(c);
        }
        return buffer.toString();
    }

    /**
     * Returns whether these two strings are equal (case sensitive).
     *
     * @param a the first string
     * @param b the seconds string
     * @return <code>true</code> if equal, <code>false</code> otherwise
     */
    public static boolean equals(String a, String b) {
        return Objects.equals(a, b);
    }

    /**
     * Returns whether these two strings are equal (case insensitive).
     *
     * @param a the first string
     * @param b the seconds string
     * @return <code>true</code> if equal, <code>false</code> otherwise
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return a == b || (a != null && a.equalsIgnoreCase(b));
    }

    /**
     * Converts a value to a boolean.
     * <p>
     * A null value or a rule mismatch will result in using the default value
     * <p>
     * The following values are considered <code>true</code>
     * - any number <> 0
     * - a boolean object with value TRUE
     * - a string = "true","yes","y", "t","on","1"
     * The following values are considered <code>false</code>
     * - any number = 0
     * - a boolean object with value FALSE
     * - a string = "false","no","n","f","off","0"
     *
     * @param value        the value
     * @param defaultValue the default value when null or no rule is matched
     * @return the boolean value
     */
    public static boolean asBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        } else {
            String valueAsString = value.toString();
            if ("y".equalsIgnoreCase(valueAsString) || "yes".equalsIgnoreCase(valueAsString) || "on".equalsIgnoreCase(valueAsString)
                    || "true".equalsIgnoreCase(valueAsString) || "t".equalsIgnoreCase(valueAsString) || "1".equalsIgnoreCase(valueAsString)) {
                return true;
            } else if ("n".equalsIgnoreCase(valueAsString) || "no".equalsIgnoreCase(valueAsString) || "off".equalsIgnoreCase(valueAsString)
                    || "false".equalsIgnoreCase(valueAsString) || "f".equalsIgnoreCase(valueAsString) || "0".equalsIgnoreCase(valueAsString)) {
                return false;
            } else {
                return defaultValue;
            }
        }
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
     * Capitalizes the given string
     *
     * @param value the string value
     * @return capitalized string
     */
    public static String capitalize(String value) {
        if (isEmpty(value)) return value;
        value = value.toLowerCase().trim();
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    /**
     * Capitalizes each word in the given string.
     * <p>
     * The method also replaces delimiter characters with space to create multiple works out of delimiters.
     *
     * @param value the string value
     * @return capitalized string
     */
    public static String capitalizeWords(String value) {
        if (isEmpty(value)) return value;
        value = value.replace('.', ' ');
        value = value.replace('_', ' ');
        value = value.replace('$', ' ');
        value = value.replace('&', ' ');
        StringBuilder rc = new StringBuilder();
        for (String word : split(value, " ")) {
            rc.append(capitalize(word)).append(" ");
        }
        return rc.toString().trim();
    }

    /**
     * Translates camel case into words separated by spaces.
     *
     * @param value the value to normalize
     * @return the beautified value
     */
    public static String beautifyCamelCase(String value) {
        if (isEmpty(value)) return value;
        StringBuilder builder = new StringBuilder(value.length() * 2);
        value = Character.toUpperCase(value.charAt(0)) + value.substring(1);
        Case prevCase = Case.NONE;
        for (int index = 0; index < value.length(); index++) {
            char c = value.charAt(index);
            Case currentCase = Character.isLowerCase(c) ? Case.LOWER_CASE : Case.UPPER_CASE;
            if (prevCase == Case.LOWER_CASE && currentCase == Case.UPPER_CASE) builder.append(" ");
            builder.append(c);
            prevCase = currentCase;
        }
        return builder.toString().trim();
    }

    /**
     * Joins an array of strings.
     *
     * @param glue  the string
     * @param parts the delimiters
     * @return a non-null instance
     * @see #split(String, String, boolean)
     */
    public static String join(String glue, String... parts) {
        StringBuilder builder = new StringBuilder(100);
        for (String part : parts) {
            if (builder.length() > 0) builder.append(glue);
            builder.append(part);
        }
        return builder.toString();
    }

    /**
     * Splits a string using a list of delimiters.
     * <p>
     * The method automatically trims the tokens.
     *
     * @param string the string
     * @param delims the delimiters
     * @return a non-null instance
     * @see #split(String, String, boolean)
     */
    public static String[] split(String string, String delims) {
        return split(string, delims, true);
    }

    /**
     * Splits a string using a list of delimiters.
     *
     * @param string the string
     * @param delims the delimiters
     * @param trim   {@code true} to trim the tokens, {@code false} otherwise
     * @return a non-null instance
     */
    public static String[] split(String string, String delims, boolean trim) {
        if (string == null) return EMPTY_STRING_ARRAY;
        StringTokenizer st = new StringTokenizer(string, delims, false);
        String[] values = new String[st.countTokens()];
        int index = 0;
        while (st.hasMoreElements()) {
            String value = (String) st.nextElement();
            values[index++] = trim ? value.trim() : value;
        }

        return values;
    }

    /**
     * Appends a value to a builder, inserting a {@code ,} if the builder always contains content.
     * <p>
     * A null value is ignored by the method.
     *
     * @param builder the builder
     * @param value   the value, null is ignored
     * @return the builder
     */
    public static StringBuilder append(StringBuilder builder, Object value) {
        return append(builder, value, ',');
    }

    /**
     * Appends a value to a builder, inserting a separator character if the builder always contains content.
     * <p>
     * A null value is ignored by the method.
     *
     * @param builder   the builder
     * @param value     the value, null is ignored
     * @param separator the separator, ignored if not printable
     * @return the builder
     */
    public static StringBuilder append(StringBuilder builder, Object value, char separator) {
        ArgumentUtils.requireNonNull(builder);
        if (value == null) return builder;
        if (builder.length() > 0 && separator >= 0x20) builder.append(separator);
        builder.append(ObjectUtils.toString(value));
        return builder;
    }

    /**
     * Returns a maximum number of lines out of a text.
     *
     * @param text         the text
     * @param maximumLines the maximum number of lines, -1 for automatic
     * @return the lines
     */
    public static String getMaximumLines(String text, int maximumLines) {
        if (isEmpty(text)) return text;
        if (maximumLines <= 0) return text;
        StringBuilder builder = new StringBuilder();
        try (Scanner sc = new Scanner(text)) {
            while (sc.hasNextLine() && maximumLines-- > 0) {
                if (builder.length() > 0) builder.append("\n");
                builder.append(sc.nextLine());
            }
        }
        return builder.toString();
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

    public enum Case {
        NONE,
        LOWER_CASE,
        UPPER_CASE
    }
}
