package net.microfalx.lang;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class UriUtilsTest {

    @Test
    void isValidUri() {
        assertFalse(UriUtils.isValidUri(""));
        assertTrue(UriUtils.isValidUri("https://www.google.com/"));
        assertFalse(UriUtils.isValidUri("https://www.google.com/>test"));
    }

    @Test
    void parseUri() {
        assertEquals("https", UriUtils.parseUri("https://www.google.com/").getScheme());
        assertNull(UriUtils.parseUri(""));
        assertNull(UriUtils.parseUri("https://www.google.com/").getQuery());
        assertEquals("www.google.com", UriUtils.parseUri("https://www.google.com/").getHost());
        assertEquals("www.google.com", UriUtils.parseUri("https://www.google.com/").getAuthority());
        assertEquals(-1, UriUtils.parseUri("https://www.google.com/").getPort());
        assertNull(UriUtils.parseUri("https://www.google.com/").getRawUserInfo());
        assertEquals("https://www.google.com/", UriUtils.parseUri("https://www.google.com/").toASCIIString());
    }

    @Test
    void parseUrl() {
        assertEquals(443, UriUtils.parseUrl("https://www.google.com/").getDefaultPort());
        assertNull(UriUtils.parseUrl("https://www.google.com/").getQuery());
        assertEquals("www.google.com", UriUtils.parseUrl("https://www.google.com/").getHost());
        assertEquals("www.google.com", UriUtils.parseUrl("https://www.google.com/").getAuthority());
        assertEquals(-1, UriUtils.parseUrl("https://www.google.com/").getPort());
        assertNull(UriUtils.parseUrl("https://www.google.com/").getUserInfo());
        assertEquals("https://www.google.com/", UriUtils.parseUrl("https://www.google.com/").toExternalForm());
        assertThrows(MalformedURLException.class, () -> UriUtils.parseUrl("htp:/invalid-url"));
    }

    @Test
    void appendFragmentWithUri() {
        assertEquals(URI.create("https://www.example.com/page#section2"),
                UriUtils.appendFragment(URI.create("https://www.example.com/page"), "section2"));
    }

    @Test
    void appendFragmentWithString() {
        assertEquals(URI.create("https://www.example.com/page#section2"),
                UriUtils.appendFragment("https://www.example.com/page", "section2"));
    }

    @Test
    void removeFragment() {
        URI uriWithFragment = UriUtils.appendFragment("https://www.example.com/page", "section2");
        assertEquals(URI.create("https://www.example.com/page"),UriUtils.removeFragment(uriWithFragment));
        assertNull(UriUtils.removeFragment(null));
    }

    @Test
    void isRootWithString() {
        assertTrue(UriUtils.isRoot(""));
        assertFalse(UriUtils.isRoot(URI.create("https://www.example.com/page").getPath()));
        assertTrue(UriUtils.isRoot(URI.create("https://www.example.com/").getPath()));
    }

    @Test
    void isRootWithUri() {
        assertFalse(UriUtils.isRoot(URI.create("https://www.example.com/page")));
        assertTrue(UriUtils.isRoot(URI.create("https://www.example.com/")));
    }

    @Test
    void joinPaths() {
        assertEquals("/",UriUtils.joinPaths(null));
        assertEquals("/page/data/id",UriUtils.joinPaths("/page","/data","/id"));
    }

    @Test
    void escapeUnsafe() {
        assertNull(UriUtils.escapeUnsafe(null));
        assertEquals("http://example.com/%20test",UriUtils.escapeUnsafe("http://example.com/ test"));
        assertEquals("http://example.com/%3etest",UriUtils.escapeUnsafe("http://example.com/>test"));
    }
}