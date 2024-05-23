package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class IOUtilsTest {

    @Test
    void getInputStreamAsString() throws IOException {
        InputStream inputStream = InputStream.nullInputStream();
        assertEquals("", IOUtils.getInputStreamAsString(inputStream));
    }

    @Test
    void getInputStreamAsBytes() throws IOException {
        InputStream inputStream = InputStream.nullInputStream();
        assertNotNull(IOUtils.getInputStreamAsBytes(inputStream));
    }

    @Test
    void getBufferedOutputStreamWithFile() throws IOException {
        assertNotNull(IOUtils.getBufferedOutputStream(File.createTempFile("temp", null)));
    }

    @Test
    void getBufferedOutputStreamWithOutputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        outputStream.write(3);
        outputStream.write(4);
        outputStream.write(5);
        outputStream.write(6);
        assertNotNull(IOUtils.getBufferedOutputStream(outputStream));
        assertNotNull(IOUtils.getBufferedOutputStream(OutputStream.nullOutputStream()));
    }

    @Test
    void getBufferedWriter() throws FileNotFoundException {
        Writer writer = new PrintWriter("target" + File.separator + "files" + File.separator + "text.txt");
        assertNotNull(IOUtils.getBufferedWriter(writer));
        assertNotNull(IOUtils.getBufferedWriter(new BufferedWriter(writer)));
    }

    @Test
    void getBufferedInputStreamWithFile() throws IOException {
        assertNotNull(IOUtils.getBufferedInputStream(new File("target" + File.separator + "files" +
                File.separator + "text.txt")));
    }

    @Test
    void getBufferedInputStreamWithInputStreamWithInputStream() throws IOException {
        assertNotNull(IOUtils.getBufferedInputStream(new BufferedInputStream(InputStream.nullInputStream())));
        assertNotNull(IOUtils.getBufferedInputStream(new ByteArrayInputStream(new byte[]{})));
        assertNotNull(IOUtils.getBufferedInputStream(new FileInputStream("target" + File.separator + "files" +
                File.separator + "text.txt")));
    }

    @Test
    void getBufferedReader() {
        Reader reader = new StringReader("I am writing java code");
        assertNotNull(IOUtils.getBufferedReader(reader));
        assertNotNull(IOUtils.getBufferedReader(new BufferedReader(Reader.nullReader())));
    }

    @Test
    void getUnclosableInputStream() {
        assertNotNull(IOUtils.getUnclosableInputStream(null));
        assertNotNull(IOUtils.getUnclosableInputStream(InputStream.nullInputStream()));
    }

    @Test
    void getUnclosableOutputStream() {
        assertNotNull(IOUtils.getUnclosableOutputStream(null));
        assertNotNull(IOUtils.getUnclosableOutputStream(OutputStream.nullOutputStream()));
    }

    @Test
    void appendStream() throws IOException {
        assertEquals(0, IOUtils.appendStream(OutputStream.nullOutputStream(), InputStream.nullInputStream()));
    }

    @Test
    void appendStreamWithRelease() throws IOException {
        assertEquals(0, IOUtils.appendStream(OutputStream.nullOutputStream(), InputStream.nullInputStream(),
                false));

    }

    @Test
    void appendStreamWithReaderAndWriter() throws IOException {
        assertEquals(22, IOUtils.appendStream(new PrintWriter("target" + File.separator + "files" +
                File.separator + "text.txt"), new StringReader("I am writing java code")));
    }

    @Test
    void appendStreamWithReaderAndWriterAndRelease() throws IOException {
        assertEquals(22, IOUtils.appendStream(new PrintWriter("target" + File.separator + "files" +
                File.separator + "text.txt"), new StringReader("I am writing java code"), false));
    }

    @Test
    void closeQuietlyWithInputStream() throws IOException {
        InputStream mock = mock(InputStream.class);
        IOUtils.closeQuietly(mock);
        verify(mock, times(1)).close();
    }

    @Test
    void closeQuietlyWithOutputStream() throws IOException {
        OutputStream mock = mock(OutputStream.class);
        IOUtils.closeQuietly(mock);
        verify(mock, times(1)).close();
    }

    @Test
    void closeQuietlyWithCloseable() throws IOException {
        Closeable mock = mock(Closeable.class);
        IOUtils.closeQuietly(mock);
        verify(mock, times(1)).close();
    }

    @Test
    void closeQuietlyWithAutoCloseable() throws IOException {
        Closeable mock = mock(Closeable.class);
        IOUtils.closeQuietly(mock);
        verify(mock, times(1)).close();
    }
}