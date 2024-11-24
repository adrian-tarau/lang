package net.microfalx.lang;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import static net.microfalx.lang.StringUtils.defaultIfEmpty;

/**
 * Utilities around multi-line strings.
 */
public class TextUtils {

    public static final int SMALL_INDENT = 2;
    public static final int MEDIUM_INDENT = 4;
    public static final int LARGE_INDENT = 8;

    public static final int HEADER_LENGTH = 130;
    public static final int MIN_HEADER_LENGTH = 20;

    public static final String ABBREVIATE_MARKER = "...";

    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Inserts a given number of spaces in front of each line
     *
     * @param text   the multi-line text to space
     * @param spaces how many space characters to insert at the beginning of each line
     */
    public static String insertSpaces(String text, int spaces) {
        return insertSpaces(text, spaces, false, false, false);
    }

    /**
     * Inserts a given number of spaces in front of each line
     *
     * @param text           the multi-line text to space
     * @param spaces         how many space characters to insert at the beginning of each line
     * @param spaceFirstLine <code>true</code> to insert spaces for first line of text, <code>false</code> to keep the first line as is
     */
    public static String insertSpaces(String text, int spaces, boolean spaceFirstLine) {
        return insertSpaces(text, spaces, false, false, spaceFirstLine);
    }

    /**
     * Inserts a given number of spaces in front of each line of text.
     *
     * @param text            the multi-line text to space
     * @param spaces          how many space characters to insert at the beginning of each line
     * @param useVerticalLine <code>true</code> to insert "|" in front of each appended line, before spaces, <code>false</code> otherwise
     * @param markTerminalRow <code>true</code> to insert "\" in front of the last appended line, before spaces, <code>false</code> otherwise
     * @param spaceFirstLine  <code>true</code> to insert spaces for first line of text, <code>false</code> to keep the first line as is
     */
    public static String insertSpaces(String text, int spaces, boolean useVerticalLine, boolean markTerminalRow, boolean spaceFirstLine) {
        if (text == null) {
            return StringUtils.EMPTY_STRING;
        }
        StringBuilder builder = new StringBuilder(text.length() + 20);
        appendTextWithSpaces(builder, text, spaces, useVerticalLine, markTerminalRow, spaceFirstLine);
        return builder.toString();
    }

    /**
     * Appends a multi-line string to the given builder inserting a number of spaces in front of each line.
     *
     * @param builder         the destination appender
     * @param text            the multi-line text to append
     * @param spaces          how many space characters to insert
     * @param useVerticalLine <code>true</code> to insert "|" in front of each appended line, before spaces, <code>false</code> otherwise
     * @param markTerminalRow <code>true</code> to insert "\" in front of the last appended line, before spaces, <code>false</code> otherwise
     * @param spaceFirstLine  <code>true</code> to insert spaces for first line of text, <code>false</code> to keep the first line as is
     */
    public static StringBuilder appendTextWithSpaces(StringBuilder builder, String text, int spaces, boolean useVerticalLine, boolean markTerminalRow, boolean spaceFirstLine) {
        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        String spacer = StringUtils.getStringOfChar(' ', spaces);
        String line;
        String nextLine = null;
        int lineCount = 0;
        try {
            line = reader.readLine();
            if (line != null) {
                nextLine = reader.readLine();
            }
            while (line != null) {
                if (lineCount != 0) {
                    builder.append("\n");
                }
                if (useVerticalLine) {
                    if (nextLine != null) {
                        builder.append('|');
                    } else if (markTerminalRow) {
                        builder.append('\\');
                    } else {
                        builder.append('|');
                    }
                }
                if (lineCount != 0 || spaceFirstLine) {
                    builder.append(spacer);
                }
                builder.append(line);
                lineCount++;
                line = nextLine;
                nextLine = reader.readLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Should not happen", e);
        }
        return builder;
    }

    /**
     * Abbreviates the text at the end (if longer than expected length).
     *
     * @param text      the text to abbreviate
     * @param maxLength the maximum width of the abbreviated text (including the marker)
     * @return the same text or an abbreviated
     */
    public static String abbreviate(String text, int maxLength) {
        return abbreviate(text, maxLength, ABBREVIATE_MARKER);
    }

    /**
     * Abbreviates the text at the end (if longer than expected length).
     *
     * @param text      the text to abbreviate
     * @param maxLength the maximum width of the abbreviated text (including the marker)
     * @param marker    the text to be added to indicate the abbreviation (more follows)
     * @return the same text or an abbreviated
     */
    public static String abbreviate(String text, int maxLength, String marker) {
        return org.apache.commons.lang3.StringUtils.abbreviate(text, marker, maxLength);
    }

    /**
     * Abbreviates the text in the middle (if longer than expected length).
     *
     * @param text      the text to abbreviate
     * @param maxLength the maximum width of the abbreviated text (including the marker)
     * @return the same text or an abbreviated
     */
    public static String abbreviateMiddle(String text, int maxLength) {
        return abbreviateMiddle(text, maxLength, ABBREVIATE_MARKER);
    }

    /**
     * Abbreviates the text in the middle (if longer than expected length).
     *
     * @param text      the text to abbreviate
     * @param maxLength the maximum width of the abbreviated text (including the marker)
     * @param marker    the text to be added to indicate the abbreviation (more follows)
     * @return the same text or an abbreviated
     */
    public static String abbreviateMiddle(String text, int maxLength, String marker) {
        return org.apache.commons.lang3.StringUtils.abbreviateMiddle(text, marker, maxLength);
    }

    /**
     * Appends white spaces at the beginning and end of the string to match the new length.
     *
     * @param text   the text
     * @param length the lenght of the new text
     * @return a non-null instance
     */
    public static String appendSpaces(String text, int length) {
        if (text.length() < length) {
            int size = (length - text.length()) / 2;
            String space = StringUtils.getStringOfChar(' ', size);
            text = space + text + space;
        }
        return org.apache.commons.lang3.StringUtils.abbreviate(text, length);
    }

    /**
     * Returns a header used to separate sections in logs.
     *
     * @param text the value of the header
     * @return the header with separators
     */
    public static String getHeader(String text) {
        return getHeader(text, HEADER_LENGTH, '~');
    }

    /**
     * Returns a header used to separate sections in logs.
     *
     * @param text   the value of the header
     * @param length the length of the header
     * @return the header with separators
     */
    public static String getHeader(String text, int length) {
        return getHeader(text, length, '~');
    }

    /**
     * Returns a header used to separate sections in logs.
     *
     * @param text      the value of the header
     * @param length    the length of the header
     * @param separator the separator used to generate the header
     * @return the header with separators
     */
    public static String getHeader(String text, int length, char separator) {
        if (length < MIN_HEADER_LENGTH) length = MIN_HEADER_LENGTH;
        text = defaultIfEmpty(text, StringUtils.NA_STRING);
        int marginSize = Math.max(length / 6, (length - text.length()) / 2) / 2;
        int contentSize = length - 2 * marginSize - 2;
        String separatorText = StringUtils.getStringOfChar(separator, marginSize);
        StringBuilder builder = new StringBuilder(300);
        builder.append(separatorText).append(' ');
        builder.append(appendSpaces(text, contentSize));
        builder.append(' ').append(separatorText);
        return org.apache.commons.lang3.StringUtils.abbreviate(builder.toString(), length);
    }
}
