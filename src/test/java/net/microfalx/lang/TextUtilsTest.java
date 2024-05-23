package net.microfalx.lang;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TextUtilsTest {

    private static String text;

    @BeforeAll
    static void createMultiLineText(){
        text= """
                I
                am
                writing
                java
                code
                """;
    }


    @Test
    void insertSpaces() {
        assertEquals("""
                I
                  am
                  writing
                  java
                  code""",TextUtils.insertSpaces(text,2));
        assertEquals("",TextUtils.insertSpaces(null,2));
    }

    @Test
    void insertSpacesWithNoSpaceInFirstLine() {
        assertEquals("""
                I
                  am
                  writing
                  java
                  code""",TextUtils.insertSpaces(text,2,false));
        assertEquals("",TextUtils.insertSpaces(null,2,false));
    }

    @Test
    void insertSpacesWithSpaceInFirstLine(){
        assertEquals("""
                 \s\sI
                 \s\sam
                 \s\swriting
                 \s\sjava
                 \s\scode""",TextUtils.insertSpaces(text,2,true));
        assertEquals("",TextUtils.insertSpaces(null,2,true));
    }

    @Test
    void insertSpacesWithVerticalLinesAndSlashesAndNoSpaceInFirstLine(){
        assertEquals("""
                 |I
                 |  am
                 |  writing
                 |  java
                 \\  code""",TextUtils.insertSpaces(text,2,true,true,false));
        assertEquals("""
                 |I
                 |  am
                 |  writing
                 |  java
                 |  code""",TextUtils.insertSpaces(text,2,true,false,false));
        assertEquals("""
                 I
                 \s\sam
                 \s\swriting
                 \s\sjava
                 \s\scode""",TextUtils.insertSpaces(text,2,false,true,false));
        assertEquals("""
                 I
                 \s\sam
                 \s\swriting
                 \s\sjava
                 \s\scode""",TextUtils.insertSpaces(text,2,false,false,false));

        assertEquals("",TextUtils.insertSpaces(null,2,true,true,
                false));
    }


    @Test
    void insertSpacesWithVerticalLinesAndSlashesAndSpaceInFirstLine(){
        assertEquals("""
                 |  I
                 |  am
                 |  writing
                 |  java
                 \\  code""",TextUtils.insertSpaces(text,2,true,true,true));
        assertEquals("""
                 |  I
                 |  am
                 |  writing
                 |  java
                 |  code""",TextUtils.insertSpaces(text,2,true,false,true));
        assertEquals("""
                 \s\sI
                 \s\sam
                 \s\swriting
                 \s\sjava
                 \s\scode""",TextUtils.insertSpaces(text,2,false,true,true));
        assertEquals("""
                 \s\sI
                 \s\sam
                 \s\swriting
                 \s\sjava
                 \s\scode""",TextUtils.insertSpaces(text,2,false,false,true));

        assertEquals("",TextUtils.insertSpaces(null,2,true,true,
                true));
    }

    @Test
    void appendTextWithSpaces() {
        assertNotNull(TextUtils.appendTextWithSpaces(new StringBuilder(),text,2,true,
                true,true));
    }
}