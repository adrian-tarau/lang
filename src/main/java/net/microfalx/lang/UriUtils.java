package net.microfalx.lang;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.StringUtils.*;

/**
 * Various utilities around URIs
 */
public class UriUtils {

    public static final String SLASH = "/";

    /**
     * Returns whether the URI is valid and can be parsed based on {@link URI} rules.
     *
     * @param uri the URI
     * @return {@code true} if valid, {@code false} otherwise
     */
    @SuppressWarnings("ConstantConditions")
    public static boolean isValidUri(String uri) {
        if (StringUtils.isEmpty(uri)) return false;
        try {
            return URI.create(uri) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses a URI from a string.
     * <p>
     * The method detects invalid characters and replace them.
     *
     * @param uri the URI
     * @return the URI, NULL if the original string was NULL or EMPTY
     */
    public static URI parseUri(String uri) {
        if (StringUtils.isEmpty(uri)) {
            return null;
        } else {
            if (!isValidUri(uri)) uri = escapeUnsafe(uri);
            return URI.create(uri);
        }
    }

    /**
     * Parses a URL from a string.
     * <p>
     * The method detects invalid characters and replace them.
     *
     * @param url the URL
     * @return the URI, NULL if the original string was NULL or EMPTY
     */
    public static URL parseUrl(String url) {
        URI uri = parseUri(url);
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            return ExceptionUtils.throwException(e);
        }
    }

    /**
     * Appends an URI fragment to the existing URI.
     * <p>
     * If the URI already has a fragment, the fragment is replaced.
     *
     * @param uri      the URI
     * @param fragment the fragment
     * @return the URI with the fragment
     */
    public static URI appendFragment(URI uri, String fragment) {
        requireNonNull(uri);
        return appendFragment(uri.toASCIIString(), fragment);
    }

    /**
     * Appends a URI fragment to the existing URI.
     * <p>
     * If the URI already has a fragment, the fragment is replaced.
     *
     * @param uri      the URI
     * @param fragment the fragment
     * @return the URI with the fragment
     */
    public static URI appendFragment(String uri, String fragment) {
        requireNonNull(uri);
        int lastHash = uri.lastIndexOf('#');
        if (lastHash != -1) uri = uri.substring(0, lastHash);
        if (isNotEmpty(fragment)) uri += "#" + fragment;
        return parseUri(uri);
    }

    /**
     * Removes the fragment from a URI.
     *
     * @param uri the URI
     * @return a new URi without a fragment
     */
    public static URI removeFragment(URI uri) {
        if (uri == null) return null;
        try {
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null);
        } catch (URISyntaxException e) {
            return ExceptionUtils.throwException(e);
        }
    }

    /**
     * Returns whether the path points to the root of the domain (null, empty or "/").
     *
     * @param path the path
     * @return {@code true} if root, {@code false} otherwise
     */
    public static boolean isRoot(String path) {
        return StringUtils.isEmpty(path) || SLASH.equals(path);
    }

    /**
     * Returns whether the URI's path points to the root of the domain (null, empty or "/").
     *
     * @param uri the path
     * @return {@code true} if root, {@code false} otherwise
     */
    public static boolean isRoot(URI uri) {
        requireNonNull(uri);
        return isRoot(uri.getPath());
    }

    /**
     * Joins the paths, creating ab absolute path out of all components.
     *
     * @param paths the paths
     * @return the absolute path
     */
    public static String joinPaths(String... paths) {
        if (paths == null) return SLASH;
        StringBuilder builder = new StringBuilder();
        builder.append('/');
        for (String path : paths) {
            builder.append(removeEndSlash(removeStartSlash(path))).append('/');
        }
        return removeEndSlash(builder.toString());
    }

    /**
     * Tries to parse the URI and in case of fixable issues it will correct the URI to be accepted by {@link URI}.
     * <p>
     * These correction are based on RFC2396 recommendations, and it will not fix {@code unfixable} problems.
     *
     * @param uri the URI
     * @return a fixed URI, null if the original URI was null
     */
    public static String escapeUnsafe(String uri) {
        if (uri == null) return null;
        for (; ; ) {
            try {
                return URI.create(uri).toASCIIString();
            } catch (Exception e) {
                URISyntaxException se = null;
                if (e instanceof IllegalArgumentException && e.getCause() instanceof URISyntaxException) {
                    se = (URISyntaxException) e.getCause();
                }
                if (se != null) {
                    int index = se.getIndex();
                    String escapedUri = escapeCharacterAtPosition(uri, index);
                    if (escapedUri == null) break;
                    uri = escapedUri;
                } else {
                    return ExceptionUtils.throwException(e);
                }
            }
        }
        return uri;
    }

    private static String escapeCharacterAtPosition(String value, int index) {
        String result = value.substring(0, index);
        char c = value.charAt(index);
        if (!isUnsafeAndEscapable(c)) return null;
        result += "%" + Integer.toHexString(c);
        result += value.substring(index + 1);
        return result;
    }

    private static boolean isUnsafeAndEscapable(char c) {
        for (char unsafeUriChar : UNSAFE_URI_CHARS) {
            if (c == unsafeUriChar) return true;
        }
        return false;
    }

    public static final char[] UNSAFE_URI_CHARS = new char[]{' ', '<', '>', '{', '}', '[', ']', '|', '\\', '^'};
}
