package net.microfalx.lang;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

/**
 * Utilities around multi line strings.
 */
public class TextUtils {

    public static final int SMALL_INDENT = 2;
    public static final int MEDIUM_INDENT = 4;
    public static final int LARGE_INDENT = 8;

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
}
