package net.microfalx.lang;

import java.util.StringTokenizer;

import static net.microfalx.lang.ArgumentUtils.requireNotEmpty;
import static net.microfalx.lang.StringUtils.EMPTY_STRING;

/**
 * A class which provides support for <a href="https://semver.org/">SemVer2</a>.
 */
public class Version {

    public static final int NO_VALUE = -1;
    private static char SEPARATOR = '.';
    private static char PRE_RELEASE_SEPARATOR = '-';
    private static char BUILD_NO_SEPARATOR = '+';
    private static final String SNAPSHOT = "-SNAPSHOT";

    private final String value;

    private int major = NO_VALUE;
    private int minor = NO_VALUE;
    private int patch = NO_VALUE;
    private int build = NO_VALUE;
    private int preRelease = NO_VALUE;
    private boolean snapshot;

    public static Version parse(String version) {
        return new Version(version);
    }

    Version(String value) {
        requireNotEmpty(value);
        this.value = value;
        parse();
    }

    public String getValue() {
        return value;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public int getBuild() {
        return build;
    }

    public int getPreRelease() {
        return preRelease;
    }

    private void parse() {
        snapshot = value.toUpperCase().contains(SNAPSHOT);
        String normalizedValue = StringUtils.replaceAll(value, SNAPSHOT, EMPTY_STRING);
        String[] parts = StringUtils.split(normalizedValue, ".");
        major = parseNumber(parts[0]);
        if (parts.length > 1) minor = parseNumber(parts[1]);
        if (parts.length > 2) {
            String patchString = parts[2];
            if (patchString.contains("-") || patchString.contains("+")) {
                StringTokenizer tokenizer = new StringTokenizer(patchString, "+-", true);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith("-") && tokenizer.hasMoreTokens()) {
                        preRelease = parseNumber(tokenizer.nextToken());
                    } else if (token.startsWith("+") && tokenizer.hasMoreTokens()) {
                        build = parseNumber(tokenizer.nextToken());
                    } else {
                        patchString = token;
                    }
                }
            }
            patch = parseNumber(patchString);
        }
    }

    private int parseNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return NO_VALUE;
        }
    }

    public String toTag() {
        if (isSnapshot()) {
            return major + "." + minor + ".latest";
        } else {
            return toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major).append(SEPARATOR).append(minor);
        if (patch != NO_VALUE) builder.append(SEPARATOR).append(patch);
        if (preRelease != NO_VALUE) builder.append(PRE_RELEASE_SEPARATOR).append(preRelease);
        if (build != NO_VALUE) builder.append(BUILD_NO_SEPARATOR).append(build);
        return builder.toString();
    }
}
