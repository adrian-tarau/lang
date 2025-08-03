package net.microfalx.lang;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Long.toHexString;
import static java.lang.System.currentTimeMillis;
import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.IOUtils.appendStream;
import static net.microfalx.lang.IOUtils.getBufferedWriter;
import static net.microfalx.lang.StringUtils.isEmpty;

/**
 * Utilities around files.
 */
public class FileUtils {

    public static final String STORE_NAME = "microfalx";
    public static final String ALL_FILES = "*.*";

    /**
     * Returns the file extension out of the file name.
     *
     * @param fileName the file name
     * @return the file extension, null if the file has no extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) return null;
        int index = fileName.lastIndexOf('.');
        if (index == -1) return null;
        return fileName.substring(index + 1);
    }

    /**
     * Returns the file name without file extension.
     *
     * @param fileName the file name
     * @return the file name
     */
    public static String removeFileExtension(String fileName) {
        if (fileName == null) return null;
        int index = fileName.lastIndexOf('.');
        if (index == -1) return fileName;
        return fileName.substring(0, index);
    }

    /**
     * Returns the file name out of a path.
     *
     * @param path the path
     * @return the file name
     */
    public static String getFileName(String path) {
        if (isEmpty(path)) return path;
        int index = path.lastIndexOf('/');
        if (index == -1) index = path.lastIndexOf('\\');
        if (index == -1) return path;
        return path.substring(index + 1);
    }

    /**
     * Returns the parent for a given path.
     *
     * @param path the path
     * @return the parent
     */
    public static String getParentPath(String path) {
        if (isEmpty(path)) return path;
        int index = path.lastIndexOf('/');
        if (index == -1) index = path.lastIndexOf('\\');
        if (index == -1) return null;
        return path.substring(0, index);
    }

    /**
     * Returns the content type for a given file name.
     *
     * @param fileName the file name
     * @return the content type
     */
    public static String getContentType(String fileName) {
        fileName = FileUtils.getFileName(fileName).toLowerCase();
        for (Map.Entry<String, String> entry : mimeTypes.entrySet()) {
            if (fileName.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return URLConnection.guessContentTypeFromName(fileName);
    }

    /**
     * Validated the directory, creates the directory if it does not exist and fails if the directory cannot be created.
     *
     * @param directory the directory
     * @return the directory, after validation
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File validateDirectoryExists(File directory) {
        requireNonNull(directory);
        if (!directory.exists()) {
            directory.mkdirs();
            if (!directory.exists()) {
                throw new IllegalStateException("Directory '" + directory.getAbsolutePath() + "' does not exist and could not be created");
            }
        }
        return directory;
    }

    /**
     * Validated the parent directory, creates the directory if it does not exist and fails if the directory cannot be created.
     *
     * @param file the file
     * @return the file, after validation
     */
    public static File validateFileExists(File file) {
        requireNonNull(file);
        validateDirectoryExists(file.getParentFile());
        return file;
    }

    /**
     * Removes a file and ignores if it fails.
     *
     * @param file the file to remove
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void remove(File file) {
        requireNonNull(file);
        try {
            file.delete();
        } catch (Exception e) {
            // ignore, just in case
        }
    }

    /**
     * Removes all files in a directory that match the given GLOB pattern.
     * <p>
     * Glob pattern: character '*' any multiple characters, '?' matches any character
     *
     * @param directory the directory to search in
     * @param pattern   the pattern to match file names against
     * @param maxDepth  the maximum depth to search, 1 means only the directory itself
     * @see FileSystem#getPathMatcher
     */
    public static void remove(File directory, String pattern, int maxDepth) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        remove(directory, matcher, maxDepth);
    }

    /**
     * Removes all files in a directory that match the given pattern.
     *
     * @param directory the directory to search in
     * @param pattern   the pattern to match file names against
     * @param maxDepth  the maximum depth to search, 1 means only the directory itself
     */
    public static void remove(File directory, Pattern pattern, int maxDepth) {
        remove(directory, path -> pattern.matcher(path.getFileName().toString()).matches(), maxDepth);
    }

    /**
     * Removes all files in a directory that match the given pattern.
     *
     * @param directory the directory to search in
     * @param matcher   the file matcher
     * @param maxDepth  the maximum depth to search, 1 means only the directory itself
     */
    public static void remove(File directory, PathMatcher matcher, int maxDepth) {
        maxDepth = Math.max(maxDepth, 1);
        try {
            try (Stream<Path> paths = Files.walk(directory.toPath(), maxDepth, FileVisitOption.FOLLOW_LINKS)) {
                paths.filter(path -> matcher.matches(path.getFileName()))
                        .forEach(path -> remove(path.toFile()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns whether the directory is writable.
     * <p>
     * The method tries to write (under a short retry loop) a small hidden file to validate the assumption.
     *
     * @return {@code true} if writable, {@code false} otherwise
     */
    public static boolean isDirectoryWritable(File directory) {
        long sleep = 10;
        for (int i = 0; i < 5; i++) {
            File file = new File(directory, "." + STORE_NAME + "_" + toHexString(currentTimeMillis()));
            try {
                appendStream(getBufferedWriter(file), new StringReader(STORE_NAME));
                return true;
            } catch (Exception e) {
                // ignore
            } finally {
                FileUtils.remove(file);
            }
            ThreadUtils.sleepMillis(sleep);
            sleep *= 2;
        }
        return false;
    }

    /**
     * Returns the path using UNIX file separators.
     *
     * @param path the path
     * @return the path suitable for UNIX environment
     */
    public static String toUnix(Path path) {
        requireNonNull(path);
        if (JvmUtils.isWindows()) {
            String pathAsString = path.toString();
            return StringUtils.replaceAll(pathAsString, "\\", "/");
        } else {
            return path.toString();
        }
    }

    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put(".json", "application/json");
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".js", "text/javascript");
        mimeTypes.put(".eot", "application/vnd.ms-fontobject");
        mimeTypes.put(".ttf", "application/x-font-ttf");
        mimeTypes.put(".otf", "application/x-font-opentype");
        mimeTypes.put(".woff", "application/x-font-woff");
        mimeTypes.put(".woff2", "application/x-font-woff2");
    }
}
