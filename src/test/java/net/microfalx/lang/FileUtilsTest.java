package net.microfalx.lang;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileUtilsTest {

    private static File file;

    @BeforeAll
    static void createFile() throws IOException {
        file = new File("target/files/test.txt");
    }

    @Test
    void getFileExtension() {
        assertNull(FileUtils.getFileExtension(null));
        assertEquals("txt", FileUtils.getFileExtension(file.getName()));
    }

    @Test
    void getFileName() throws IOException {
        assertNull(FileUtils.getFileName(null));
        assertEquals("test.txt", FileUtils.getFileName(file.getPath()));
    }

    @Test
    void getParentPath() {
        assertEquals("target" + File.separator + "files", FileUtils.getParentPath(file.getPath()));

    }

    @Test
    void getContentType() {
        assertEquals("text/plain", FileUtils.getContentType(file.getName()));
    }

    @Test
    void validateDirectoryExists() {
        assertEquals(file.getParentFile(), FileUtils.validateDirectoryExists(file.getParentFile()));
    }

    @Test
    void validateFileExists() {
        assertEquals(new File("target" + File.separator + "files" + File.separator + "test.txt"), FileUtils.validateFileExists(file));
    }

    @AfterAll
    static void deleteFile() {
        file.deleteOnExit();
    }
}