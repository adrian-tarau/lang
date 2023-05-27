package net.microfalx.lang;

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
}
