package net.microfalx.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

/**
 * Utilities around I/O operations.
 */
public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static final int BUFFER_SIZE = 128 * 1024;

    /**
     * Reads the content of the stream as a String.
     * <p>
     * It expects the String in UTF-8 encoding.
     *
     * @param inputStream the input stream
     * @return the content of the stream as String
     * @throws IOException if an I/O error occurs
     */
    public static String getInputStreamAsString(InputStream inputStream) throws IOException {
        byte[] bytes = getInputStreamAsBytes(inputStream);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Reads the content of the stream into a byte buffer and closes the stream.
     *
     * @param inputStream the input stream
     * @return the content of the stream as byte[]
     * @throws IOException if an I/O error occurs
     */
    public static byte[] getInputStreamAsBytes(InputStream inputStream) throws IOException {
        requireNonNull(inputStream);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        appendStream(out, inputStream);
        return out.toByteArray();
    }

    /**
     * Creates a buffered output stream out of a given file.
     *
     * @param file the file
     * @return a buffered stream, the same object if the stream is already buffered, or it does not require any buffer
     */
    public static OutputStream getBufferedOutputStream(File file) throws IOException {
        return getBufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * Creates a buffered output stream out of a given output stream if required.
     *
     * @param outputStream the output stream
     * @return a buffered stream, the same object if the stream is already buffered, or it does not require any buffer
     */
    public static OutputStream getBufferedOutputStream(OutputStream outputStream) {
        requireNonNull(outputStream);
        if (outputStream instanceof BufferedOutputStream) {
            return outputStream;
        } else if (outputStream instanceof ByteArrayOutputStream) {
            return outputStream;
        } else {
            return new BufferedOutputStream(outputStream, BUFFER_SIZE);
        }
    }

    /**
     * Creates a buffered writer out of a file.
     *
     * @param file the file
     * @return a buffered writer
     */
    public static Writer getBufferedWriter(File file) throws IOException {
        requireNonNull(file);
        return getBufferedWriter(new FileWriter(file));
    }

    /**
     * Creates a buffered writer out of a given writer if required.
     *
     * @param writer the writer
     * @return a buffered writer, the same object if the writer is already buffered, or it does not require any buffer
     */
    public static Writer getBufferedWriter(Writer writer) {
        requireNonNull(writer);
        if (writer instanceof BufferedWriter) {
            return writer;
        } else {
            return new BufferedWriter(writer, BUFFER_SIZE);
        }
    }

    /**
     * Creates a buffered input stream out of a given file.
     *
     * @param file the file
     * @return a buffered stream, the same object if the stream is already buffered, or it does not require any buffer
     */
    public static InputStream getBufferedInputStream(File file) throws IOException {
        return getBufferedInputStream(new FileInputStream(file));
    }

    /**
     * Creates a buffered input stream out of a given input stream if required.
     *
     * @param inputStream the input stream
     * @return a buffered stream, the same object if the stream is already buffered, or it does not require any buffer
     */
    public static InputStream getBufferedInputStream(InputStream inputStream) {
        requireNonNull(inputStream);
        if (inputStream instanceof BufferedInputStream) {
            return inputStream;
        } else if (inputStream instanceof ByteArrayInputStream) {
            return inputStream;
        } else {
            return new BufferedInputStream(inputStream, BUFFER_SIZE);
        }
    }

    /**
     * Creates a buffered reader out of a given reader if required.
     *
     * @param reader the reader
     * @return a buffered stream, the same object if the stream is already buffered or it does not require any buffer
     */
    public static Reader getBufferedReader(Reader reader) {
        requireNonNull(reader);
        if (reader instanceof BufferedReader) {
            return reader;
        } else {
            return new BufferedReader(reader, BUFFER_SIZE);
        }
    }

    /**
     * Returns an input stream which decompresses the data compressed by
     * {@link #getCompressedOutputStream(OutputStream)}.
     *
     * @param inputStream the input stream
     * @return a non-null instance
     * @see #getCompressedOutputStream(OutputStream)
     */
    public static InputStream getComporessedInputStream(InputStream inputStream) throws IOException {
        requireNonNull(inputStream);
        return new GZIPInputStreamWrapper(getBufferedInputStream(inputStream));
    }

    /**
     * Returns an output stream which compresses all the data and writes it to the given output stream.
     *
     * @param outputStream the output stream
     * @return a non-null instance
     * @see #getComporessedInputStream(InputStream)
     */
    public static OutputStream getCompressedOutputStream(OutputStream outputStream) throws IOException {
        requireNonNull(outputStream);
        return new GZIPOutputStreamWrapper(getBufferedOutputStream(outputStream));
    }

    /**
     * Creates an input stream wrapper  that prevents the underlying input stream from being closed.
     *
     * @param inputStream the original input stream
     * @return a non-null instance
     */
    public static InputStream getUnclosableInputStream(InputStream inputStream) {
        if (inputStream == null) inputStream = new ByteArrayInputStream(new byte[0]);
        return new UnclosableInputStream(inputStream);
    }

    /**
     * Creates an output stream wrapper that prevents the underlying output stream from being closed.
     *
     * @param outputStream the original output stream
     * @return a non-null instance
     */
    public static OutputStream getUnclosableOutputStream(OutputStream outputStream) {
        if (outputStream == null) outputStream = new ByteArrayOutputStream();
        return new UnclosableOuputStream(outputStream);
    }

    /**
     * Copies the input stream content into the output stream. Streams
     * are automatically buffered if they do not implement BufferedInputStream/BufferedOutputStream.
     * <p>
     * The output stream is closed at the end.
     *
     * @param out destination stream
     * @param in  source stream
     * @return number of byte copied from source to destination
     * @throws IOException
     */
    public static long appendStream(OutputStream out, InputStream in) throws IOException {
        return appendStream(out, in, true);
    }

    /**
     * Copies the input stream content into the output stream. Streams
     * are automatically buffered if they do not implement BufferedInputStream/BufferedOutputStream.
     *
     * @param out     destination stream
     * @param in      source stream
     * @param release if true closes the output stream after completion
     * @return number of byte copied from source to destination
     * @throws IOException
     */
    public static long appendStream(OutputStream out, InputStream in, boolean release) throws IOException {
        requireNonNull(out);
        requireNonNull(in);
        // makes sure streams are buffered
        out = getBufferedOutputStream(out);
        in = getBufferedInputStream(in);
        byte[] buffer = new byte[BUFFER_SIZE];
        long totalCopied = 0;
        int count;
        boolean successful = false;
        try {
            while ((count = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, count);
                totalCopied += count;
            }
            out.flush();
            if (release) {
                out.close();
            }
            successful = true;
        } finally {
            closeQuietly(in);
            if (!successful) {
                // if not successful(flush or close did not complete, close the output stream even if release = false to it will not remain open
                closeQuietly(out);
            }
        }
        return totalCopied;
    }

    /**
     * Copies the reader content into the writer. Streams
     * are automatically buffered if they do not implement BufferedInputStream/BufferedOutputStream.
     * <p>
     * The writer is closed at the end.
     *
     * @param out destination stream
     * @param in  source stream
     * @return number of byte copied from source to destination
     * @throws IOException
     */
    public static long appendStream(Writer out, Reader in) throws IOException {
        return appendStream(out, in, true);
    }

    /**
     * Copies the reader content into the writer. Streams
     * are automatically buffered if they do not implement BufferedInputStream/BufferedOutputStream.
     *
     * @param out     destination stream
     * @param in      source stream
     * @param release if true closes the output stream after completion
     * @return number of byte copied from source to destination
     * @throws IOException
     */
    public static long appendStream(Writer out, Reader in, boolean release) throws IOException {
        requireNonNull(out);
        requireNonNull(in);
        // makes sure streams are buffered
        out = getBufferedWriter(out);
        in = getBufferedReader(in);
        char[] buffer = new char[BUFFER_SIZE];
        long totalCopied = 0;
        int count;
        boolean successful = false;
        try {
            while ((count = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, count);
                totalCopied += count;
            }
            out.flush();
            if (release) {
                out.close();
            }
            successful = true;
        } finally {
            closeQuietly(in);
            if (!successful) {
                // if not successful(flush or close did not complete, close the output stream even if release = false to it will not remain open
                closeQuietly(out);
            }
        }
        return totalCopied;
    }

    /**
     * Creates the file if it does not exist.
     *
     * @param file the file to initialize
     * @throws IOException if the file could not be created
     */
    public static void initializeFile(File file) throws IOException {
        requireNonNull(file);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed to create file: " + file.getAbsolutePath());
            }
        }
    }

    public static void closeQuietly(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to close input stream (type " + is.getClass().getName() + "), reason: " + e.getMessage());
        }
    }

    public static void closeQuietly(OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to close output stream (type " + out.getClass().getName() + "), reason: " + e.getMessage());
        }
    }

    public static void closeQuietly(Closeable out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to close stream, reason: " + e.getMessage());
        }
    }

    public static void closeQuietly(AutoCloseable out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to close stream, reason: " + e.getMessage());
        }
    }



    static class UnclosableInputStream extends FilterInputStream {

        public UnclosableInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            // do not close
        }
    }

    static class UnclosableOuputStream extends FilterOutputStream {

        public UnclosableOuputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void close() throws IOException {
            // do not close
        }
    }

    static class GZIPOutputStreamWrapper extends GZIPOutputStream {

        GZIPOutputStreamWrapper(OutputStream out) throws IOException {
            super(out);
            def.setLevel(Deflater.BEST_SPEED);
        }
    }

    static class GZIPInputStreamWrapper extends GZIPInputStream {

        public GZIPInputStreamWrapper(InputStream in) throws IOException {
            super(in);
        }
    }
}
