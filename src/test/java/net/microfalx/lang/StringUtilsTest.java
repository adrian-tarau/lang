package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void append() {
        StringBuilder builder = new StringBuilder();
        StringUtils.append(builder, "test1");
        StringUtils.append(builder, "test2");
        StringUtils.append(builder, null);
        StringUtils.append(builder, "test2", '|');
        assertEquals("test1,test2|test2", builder.toString());
    }

}