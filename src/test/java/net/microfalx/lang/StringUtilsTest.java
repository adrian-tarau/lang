package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {


    @Test
    void isEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty(null));
        assertFalse(StringUtils.isEmpty("This string is not empty"));
    }

    @Test
    void isNotEmpty() {
        assertFalse(StringUtils.isNotEmpty(""));
        assertFalse(StringUtils.isNotEmpty(null));
        assertTrue(StringUtils.isNotEmpty("This string is not empty"));
    }

    @Test
    void appendWithComma() {
        StringBuilder builder = new StringBuilder();
        StringUtils.append(builder, "test1");
        StringUtils.append(builder, "test2");
        StringUtils.append(builder, "test3");
        assertEquals("test1,test2,test3", builder.toString());
    }


    @Test
    void appendWithCharacterSeparator() {
        StringBuilder builder = new StringBuilder();
        StringUtils.append(builder, "test1", '|');
        StringUtils.append(builder, "test2", '|');
        StringUtils.append(builder, null, '|');
        StringUtils.append(builder, "test2", '|');
        assertEquals("test1|test2|test2", builder.toString());
    }

    @Test
    void appendWithStringSeparator() {
        StringBuilder builder = new StringBuilder();
        StringUtils.append(builder, "test1", " ok ");
        StringUtils.append(builder, "test2", " ok ");
        StringUtils.append(builder, "test3", " ok ");
        assertEquals("test1 ok test2 ok test3", builder.toString());
    }

    @Test
    void defaultIfEmpty() {
        String value = "";
        String defaultValue = "The string is empty";
        assertEquals("The string is empty", StringUtils.defaultIfEmpty(value, defaultValue));
        value = "This string is not empty";
        assertEquals(value, StringUtils.defaultIfEmpty(value, defaultValue));
    }

    @Test
    void defaultIfNull() {
        String value = null;
        String defaultValue = "The string is null";
        assertEquals("The string is null", StringUtils.defaultIfNull(value, defaultValue));
        value = "This string is not null";
        assertEquals(value, StringUtils.defaultIfNull(value, defaultValue));
    }

    @Test
    void emptyIfNull() {
        assertEquals("", StringUtils.emptyIfNull(null));
        String value = "This string is not null";
        assertEquals(value, StringUtils.emptyIfNull(value));
    }

    @Test
    void nullIfEmpty() {
        String value = "";
        assertNull(null, StringUtils.nullIfEmpty(value));
        value = "This string is not empty";
        assertNotNull(StringUtils.nullIfEmpty(value));
    }

    @Test
    void contains() {
        String text = "This is a text";
        assertTrue(StringUtils.contains(text, "text"));
        assertFalse(StringUtils.contains(null, "java"));
        assertTrue(StringUtils.contains(null, null));
    }

    @Test
    void containsNewLines() {
        assertFalse(StringUtils.containsNewLines(null));
        String text = """
                                        
                                        
                """;
        assertTrue(StringUtils.containsNewLines(text));
    }

    @Test
    void containsWhiteSpaces() {
        String text = "I am writing Java code";
        assertFalse(StringUtils.containsWhiteSpaces(null));
        assertTrue(StringUtils.containsWhiteSpaces(text));
    }

    @Test
    void containsWhiteSpacesOnly() {
        String text = "               ";
        assertFalse(StringUtils.containsWhiteSpacesOnly(null));
        assertTrue(StringUtils.containsWhiteSpacesOnly(text));
    }

    @Test
    void toIdentifierWithNoDash() {
        assertEquals("a1b2c3_567efg", StringUtils.toIdentifier("a1B2c3!@#+-?567efg"));
        assertEquals("a1b2c3_567efg", StringUtils.toIdentifier("_a1B2c3!@#+-?567efg"));
        assertEquals("a1b2c3_567efg", StringUtils.toIdentifier("a1B2c3!@#+-?567efg_"));
    }

    @Test
    void toIdentifierWithDash() {
        assertEquals("a1b2c3_-_567efg", StringUtils.toIdentifier("a1B2c3!@#+-?567efg", true));
        assertEquals("a1b2c3_-_567efg", StringUtils.toIdentifier("_a1B2c3!@#+-?567efg", true));
        assertEquals("a1b2c3_-_567efg", StringUtils.toIdentifier("a1B2c3!@#+-?567efg_", true));
    }

    @Test
    void toLowerCase() {
        assertEquals("hello", StringUtils.toLowerCase("HELLO"));
        assertNull(StringUtils.toLowerCase(null));
    }

    @Test
    void toUpperCase() {
        assertEquals("HELLO", StringUtils.toUpperCase("hello"));
        assertNull(StringUtils.toUpperCase(null));
    }

    @Test
    void getStringOfChar() {
        assertEquals("aaaa", StringUtils.getStringOfChar('a', 4));
    }

    @Test
    void equals() {
        assertTrue(StringUtils.equals("", ""));
        assertFalse(StringUtils.equals(" ", ""));
    }

    @Test
    void equalsIgnoreCase() {
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
    }

    @Test
    void containsInArrayWithCharacters() {
        char[] chars = {'g', 'h', 'o', 'x', 'a', 'w', 'y', 'z'};
        assertFalse(StringUtils.containsInArray('a', null));
        assertFalse(StringUtils.containsInArray((char) 0x00, chars));
        assertTrue(StringUtils.containsInArray('a', chars));
    }

    @Test
    void containsInArrayWithStrings() {
        String[] strings = {"hi", "how", "are", "you", "doing"};
        assertFalse(StringUtils.containsInArray("you", null));
        assertFalse(StringUtils.containsInArray(null, strings));
        assertTrue(StringUtils.containsInArray("you", strings));
    }

    @Test
    void asBoolean() {
        assertFalse(StringUtils.asBoolean(null, false));
        assertTrue(StringUtils.asBoolean(Boolean.TRUE, false));
        assertFalse(StringUtils.asBoolean(Boolean.FALSE, true));
        assertTrue(StringUtils.asBoolean(Integer.valueOf("1"), false));
        assertFalse(StringUtils.asBoolean(Integer.valueOf("0"), true));
        assertTrue(StringUtils.asBoolean("true", false));
        assertTrue(StringUtils.asBoolean("t", false));
        assertTrue(StringUtils.asBoolean("yes", false));
        assertTrue(StringUtils.asBoolean("y", false));
        assertTrue(StringUtils.asBoolean("on", false));
        assertTrue(StringUtils.asBoolean("1", false));
        assertFalse(StringUtils.asBoolean("false", true));
        assertFalse(StringUtils.asBoolean("f", true));
        assertFalse(StringUtils.asBoolean("no", true));
        assertFalse(StringUtils.asBoolean("n", true));
        assertFalse(StringUtils.asBoolean("off", true));
        assertFalse(StringUtils.asBoolean("0", true));

    }

    @Test
    void toDashIdentifier() {
        assertEquals("a-5-b-c", StringUtils.toDashIdentifier("_a_5&b_=c_"));
    }

    @Test
    void capitalize() {
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("I", StringUtils.capitalize("     i     "));
    }

    @Test
    void capitalizeFirst() {
        assertEquals("", StringUtils.capitalizeFirst(""));
        assertEquals("I am writing java code", StringUtils.capitalizeFirst("i am writing java code"));
    }

    @Test
    void uncapitalizeFirst() {
        assertEquals("", StringUtils.uncapitalizeFirst(""));
        assertEquals("i am writing java code", StringUtils.uncapitalizeFirst("I am writing java code"));
    }

    @Test
    void capitalizeWords() {
        assertEquals("", StringUtils.capitalizeWords(""));
        assertEquals("I Am Writing Java Code", StringUtils.capitalizeWords("I$am_writing-java&code."));
    }

    @Test
    void beautifyCamelCase() {
        assertEquals("", StringUtils.beautifyCamelCase(""));
        assertEquals("Create Phone Products", StringUtils.beautifyCamelCase("createPhoneProducts"));
    }

    @Test
    void removeLineBreaksWithEllipsesWithSpaces() {
        String multiLinesText = """
                I
                am
                writing
                java 17
                code
                """;
        assertEquals("I ... am ... writing ... java 17 ... code", StringUtils.removeLineBreaks(multiLinesText));
    }

    @Test
    void removeLineBreaksWithCommas() {
        String multiLinesText = """
                I
                am
                writing
                java 17
                code
                """;
        assertEquals("I,am,writing,java 17,code", StringUtils.removeLineBreaks(multiLinesText, ","));
    }

    @Test
    void join() {
        assertEquals("I,am,writing,java,code", StringUtils.join(",", "I,am,writing,java,code"));
    }

    @Test
    void split() {
        assertNotNull(StringUtils.split("", ","));
        assertNotNull(StringUtils.split("I,am,writing,java,code", ","));
    }

    @Test
    void getMaximumLines() {
        String multiLinesText = """
                I
                am
                writing
                java 17
                code
                """;
        assertEquals("""
                I
                am
                writing""", StringUtils.getMaximumLines(multiLinesText, 3));
        assertEquals("", StringUtils.getMaximumLines("", 3));
        assertEquals(multiLinesText, StringUtils.getMaximumLines(multiLinesText, -1));
    }

    @Test
    void removeStartSlash() {
        assertEquals("box", StringUtils.removeStartSlash("\\box"));
        assertEquals("box", StringUtils.removeStartSlash("/box"));
        assertEquals("", StringUtils.removeStartSlash(""));
    }

    @Test
    void removeEndSlash() {
        assertEquals("box", StringUtils.removeEndSlash("box\\"));
        assertEquals("box", StringUtils.removeEndSlash("box/"));
        assertEquals("", StringUtils.removeEndSlash(""));
    }

    @Test
    void addEndSlash() {
        assertEquals("box/", StringUtils.addEndSlash("box"));
        assertEquals("/", StringUtils.addEndSlash(""));
    }

    @Test
    void addStartSlash() {
        assertEquals("/box", StringUtils.addStartSlash("box"));
        assertEquals("/", StringUtils.addStartSlash(""));
    }

    @Test
    void formatMessage() {
        assertEquals("Unmapped message, null/empty pattern, arguments: [1, 2, 3, ok]",
                StringUtils.formatMessage("", 1, 2, 3, "ok"));
        assertEquals("I am writing java code", StringUtils.formatMessage("I am writing java code", 1, 2, 3, "ok"));
    }
}