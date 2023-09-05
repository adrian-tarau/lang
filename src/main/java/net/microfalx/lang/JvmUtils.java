package net.microfalx.lang;

import java.io.File;

import static net.microfalx.lang.StringUtils.removeEndSlash;

/**
 * Various utilities around JVM.
 */
public class JvmUtils {

    private static File homeDirectory;
    private static File varDirectory;
    private static File tmpDirectory;
    private static File workingDirectory;

    /**
     * Returns the OS user's name which owns the JVM process.
     *
     * @return a non-null instance
     */
    public static String getUserName() {
        String userName = System.getProperty("user.name");
        return StringUtils.isNotEmpty(userName) ? userName : "na";
    }

    /**
     * Returns the user's home directory.
     *
     * @return a non-null instance
     */
    public static File getHomeDirectory() {
        if (homeDirectory != null) return homeDirectory;
        String homeDirectory = System.getProperty("user.home");
        if (homeDirectory == null) throw new IllegalStateException("JVM does not provide system property 'user.home'");
        JvmUtils.homeDirectory = new File(removeEndSlash(homeDirectory));
        return JvmUtils.homeDirectory;
    }

    /**
     * Returns the user's variable data directory.
     *
     * @return a non-null instance
     */
    public static File getVariableDirectory() {
        if (varDirectory != null) return varDirectory;
        String varDirectory = System.getProperty("user.home.var");
        if (varDirectory == null) varDirectory = "/var" + getHomeDirectory();
        JvmUtils.varDirectory = new File(varDirectory);
        return JvmUtils.varDirectory;
    }

    /**
     * Returns the working directory.
     *
     * @return a non-null instance
     */
    public static File getWorkingDirectory() {
        if (workingDirectory != null) return workingDirectory;
        String workingDirectory = System.getProperty("user.dir");
        if (workingDirectory == null)
            throw new IllegalStateException("JVM does not provide system property 'user.dir'");
        JvmUtils.workingDirectory = new File(removeEndSlash(workingDirectory));
        return JvmUtils.workingDirectory;
    }

    /**
     * Returns the temporary directory.
     *
     * @return a non-null instance
     */
    public static File getTemporaryDirectory() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null) throw new IllegalStateException("JVM does not provide system property 'java.io.tmpdir'");
        JvmUtils.tmpDirectory = new File(getHomeDirectory(), "tmp");
        return FileUtils.validateDirectoryExists(JvmUtils.tmpDirectory);
    }

    /**
     * Returns the shared memory directory. If there is no shared memory, it will use the temporary directory
     * and create a subdirectory called "shm".
     *
     * @return a non-null file
     */
    public static File getSharedMemoryDirectory() {
        File directory = new File("/run/shm");
        if (!directory.exists()) directory = new File("/dev/shm");
        if (!directory.exists()) {
            directory = new File(getTemporaryDirectory(), "shm");
            return FileUtils.validateDirectoryExists(directory);
        } else {
            return FileUtils.validateDirectoryExists(new File(directory, "microfalx"));
        }
    }

    /**
     * Replaces standard System properties placeholders.
     *
     * @param value the value.
     * @return the value with variables replaces
     */
    public static String replacePlaceholders(String value) {
        if (StringUtils.isEmpty(value)) return value;
        value = org.apache.commons.lang3.StringUtils.replaceOnce(value, "${user.home}", getHomeDirectory().getAbsolutePath());
        value = org.apache.commons.lang3.StringUtils.replaceOnce(value, "${user.dir}", getHomeDirectory().getAbsolutePath());
        value = org.apache.commons.lang3.StringUtils.replaceOnce(value, "${shm.home}", getHomeDirectory().getAbsolutePath());
        value = org.apache.commons.lang3.StringUtils.replaceOnce(value, "${tmp.home}", getHomeDirectory().getAbsolutePath());
        return value;
    }
}
