package net.microfalx.lang;

import java.io.File;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.StringUtils.isEmpty;

/**
 * Utilities around files.
 */
public class FileUtils {

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
    public static File validateDirectoryExists(File directory) {
        requireNonNull(directory);
        if (!directory.exists()) directory.mkdirs();
        if (!directory.exists()) {
            ThreadUtils.throwException(new IllegalStateException("Directory '" + directory.getAbsolutePath() + "' could not be created"));
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
