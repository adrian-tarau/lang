package net.microfalx.lang;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {

    private static String text;

    @BeforeAll
    static void createMultiLineText() {
        text = "I\n" +
               "am\n" +
               "writing\n" +
               "java\n" +
               "code\n";
    }


    @Test
    void insertSpaces() {
        assertEquals("I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2));
        assertEquals("", TextUtils.insertSpaces(null, 2));
    }

    @Test
    void insertSpacesWithNoSpaceInFirstLine() {
        assertEquals("I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, false));
        assertEquals("", TextUtils.insertSpaces(null, 2, false));
    }

    @Test
    void insertSpacesWithSpaceInFirstLine() {
        assertEquals("  I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, true));
        assertEquals("", TextUtils.insertSpaces(null, 2, true));
    }

    @Test
    void insertSpacesWithVerticalLinesAndSlashesAndNoSpaceInFirstLine() {
        assertEquals("|I\n" +
                     "|  am\n" +
                     "|  writing\n" +
                     "|  java\n" +
                     "\\  code", TextUtils.insertSpaces(text, 2, true, true, false));
        assertEquals("|I\n" +
                     "|  am\n" +
                     "|  writing\n" +
                     "|  java\n" +
                     "|  code", TextUtils.insertSpaces(text, 2, true, false, false));
        assertEquals("I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, false, true, false));
        assertEquals("I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, false, false, false));

        assertEquals("", TextUtils.insertSpaces(null, 2, true, true,
                false));
    }


    @Test
    void insertSpacesWithVerticalLinesAndSlashesAndSpaceInFirstLine() {
        assertEquals("|  I\n" +
                     "|  am\n" +
                     "|  writing\n" +
                     "|  java\n" +
                     "\\  code", TextUtils.insertSpaces(text, 2, true, true, true));
        assertEquals("|  I\n" +
                     "|  am\n" +
                     "|  writing\n" +
                     "|  java\n" +
                     "|  code", TextUtils.insertSpaces(text, 2, true, false, true));
        assertEquals("  I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, false, true, true));
        assertEquals("  I\n" +
                     "  am\n" +
                     "  writing\n" +
                     "  java\n" +
                     "  code", TextUtils.insertSpaces(text, 2, false, false, true));

        assertEquals("", TextUtils.insertSpaces(null, 2, true, true,
                true));
    }

    @Test
    void appendTextWithSpaces() {
        assertNotNull(TextUtils.appendTextWithSpaces(new StringBuilder(), text, 2, true,
                true, true));
    }

    @Test
    void getHeader() {
        assertEquals("~~~~    demo    ~~~~", TextUtils.getHeader("demo", 20));
        assertEquals("=====     demo     =====", TextUtils.getHeader("demo", 25, '='));
        assertEquals("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                                demo                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", TextUtils.getHeader("demo"));
    }

    @Test
    void isBinaryContent() {
        assertFalse(TextUtils.isBinaryContent("abcdefghijklmnopqrstuvwxyz"));
        assertFalse(TextUtils.isBinaryContent("abcde4343fghij2434klm+dsds"));
        assertTrue(TextUtils.isBinaryContent("abcd\u00012\u0003"));
    }
}