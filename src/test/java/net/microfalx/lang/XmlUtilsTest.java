package net.microfalx.lang;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultDocument;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class XmlUtilsTest {


    @Test
    void loadDocument() {
        assertThrows(IllegalArgumentException.class, () -> XmlUtils.loadDocument(new
                ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6})));
        Document document = loadDocument("test1.xml");
        assertNotNull(document);
        assertEquals("UTF-8", document.getXMLEncoding());
        assertNull(document.getDocType());
        assertEquals("", document.getText());
        assertNull(document.getName());
    }

    @Test
    void loadDocumentWithString() throws IOException {
        String xmlContent = getDocumentAsString("test1.xml");
        Document document = XmlUtils.loadDocument(xmlContent);
        assertEquals("", document.getText());
        assertNull(document.getXMLEncoding());
        assertNull(document.getName());
        assertNull(document.getXMLEncoding());
    }


    @Test
    void loadDocumentWithReader() throws IOException {
        String xmlContent = getDocumentAsString("test1.xml");
        Document document = XmlUtils.loadDocument(new StringReader(xmlContent));
        assertEquals("", document.getText());
        assertNull(document.getXMLEncoding());
        assertNull(document.getName());
        assertNull(document.getXMLEncoding());
    }

    @Test
    void loadDocumentWithFile() throws IOException {
        Document document = XmlUtils.loadDocument(new File("src" + File.separator + "test" + File.separator +
                "resources" + File.separator + "test1.xml"));
        assertEquals("", document.getText());
        assertNull(document.getXMLEncoding());
        assertNull(document.getName());
        assertNull(document.getXMLEncoding());
    }

    @Test
    void createDocument() {
        assertNotNull(XmlUtils.createDocument());
    }

    @Test
    void selectElements() {
        // TODO
    }

    @Test
    void testSelectElements() {
        //TODO
    }

    @Test
    void getRequiredElementText() {
        Document document = loadDocument("file2.xml");
        assertEquals("John Doe", XmlUtils.getRequiredElementText(document.getRootElement()
                .element("person"), "name"));
        assertEquals("30", XmlUtils.getRequiredElementText(document.getRootElement()
                .element("person"), "age"));
        assertEquals("Seattle", XmlUtils.getRequiredElementText(document.getRootElement()
                .element("person"), "city"));
        assertThrows(IllegalArgumentException.class,
                () -> XmlUtils.getRequiredElementText(document.getRootElement(), "city"));
    }

    @Test
    void getElementTextAsInteger() {
        Document document = loadDocument("file2.xml");
        assertEquals(1, XmlUtils.getElementTextAsInteger(document.getRootElement()
                .element("person"), "name", 1));
        assertEquals(30, XmlUtils.getElementTextAsInteger(document.getRootElement()
                .element("person"), "age", 1));

    }

    @Test
    void getElementTextAsBoolean() {
        Document document = loadDocument("file2.xml");
        assertTrue(XmlUtils.getElementTextAsBoolean(document.getRootElement()
                .element("person"), "name", true));

    }

    @Test
    void getElementText() {
        Document document = loadDocument("file2.xml");
        assertEquals("", XmlUtils.getElementText(document.getRootElement().element("person")));
        assertEquals("John Doe", XmlUtils.getElementText(document.getRootElement().element("person")
                .element("name")));
    }

    @Test
    void getElementTextWithTrim() {
        Document document = loadDocument("file2.xml");
        assertEquals("\n        \n        \n        \n    ", XmlUtils.getElementText(document.getRootElement()
                .element("person"), false));
        assertEquals("Seattle", XmlUtils.getElementText(document.getRootElement().element("person")
                        .element("city"), false));
    }

    @Test
    void getElementTextWithChildName() {
        Document document = loadDocument("file2.xml");
        assertEquals("Seattle",XmlUtils.getElementText(document.getRootElement().element("person"),
                "city"));
        assertEquals("", XmlUtils.getElementText(document.getRootElement(), "person"));
    }

    @Test
    void getElementTextWithDefaultValue() {
        Document document = loadDocument("file2.xml");
        assertEquals("30", XmlUtils.getElementText(document.getRootElement().element("person"),
                "age", "default value"));
        assertEquals("", XmlUtils.getElementText(document.getRootElement(), "person",
                "default value"));
    }

    @Test
    void getElementTextWithDefaultValueAndTrim() {
        Document document = loadDocument("file2.xml");
        assertEquals("30", XmlUtils.getElementText(document.getRootElement().element("person"),
                "age", "default value", false));
        assertEquals("", XmlUtils.getElementText(document.getRootElement(), "person",
                "default value", true));
    }

    @Test
    void getAttributeWithBoolean() {
        Document document = loadDocument("file2.xml");
        assertTrue(XmlUtils.getAttribute(document.getRootElement().element("person"), "name",
                true));

    }

    @Test
    void getAttributeWithInteger() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","7").addAttribute("gender",
                "female");;
        assertEquals(7,XmlUtils.getAttribute(element,"count", 0));
        assertEquals(78, XmlUtils.getAttribute(element,
                "gender", 78));


    }

    @Test
    void getAttributeWithDouble() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","7.0").addAttribute("gender",
                "male");
        assertEquals(7d,XmlUtils.getAttribute(element,"count", 0d));
        assertEquals(78d, XmlUtils.getAttribute(element,
                "gender", 78d));

    }

    @Test
    void getAttributeWithString() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","7.0");
        assertEquals("7.0",XmlUtils.getAttribute(element,"count"));
        assertNull(XmlUtils.getAttribute(document.getRootElement().element("person"), "name"));

    }

    @Test
    void getAttributeWithStringAndDefaultValue() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","547");
        assertEquals("547",XmlUtils.getAttribute(element,"count","999"));
        assertEquals("default", XmlUtils.getAttribute(document.getRootElement().element("person"),
                "name", "default"));

    }

    @Test
    void getRequiredAttribute() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","547");
        assertEquals("547",XmlUtils.getRequiredAttribute(element,"count"));
        assertThrows(IllegalArgumentException.class, () ->
                XmlUtils.getRequiredAttribute(document.getRootElement().element("person"),
                        null));

    }

    @Test
    void getAttributeWithEnum() {
        Document document = loadDocument("file2.xml");
        assertEquals(EnumUtilsTest.Enum1.B,XmlUtils.getAttribute(document.getRootElement(),"name",
                EnumUtilsTest.Enum1.class, EnumUtilsTest.Enum1.B));
    }

    @Test
    void removeAttribute() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","547");
        assertTrue(XmlUtils.removeAttribute(element,"count"));
        assertFalse(XmlUtils.removeAttribute(document.getRootElement().element("person"), "name"));
    }

    @Test
    void hasAttribute() {
        Document document = loadDocument("file2.xml");
        Element element= document.getRootElement().addAttribute("count","547");
        assertTrue(XmlUtils.hasAttribute(element,"count"));
        assertFalse(XmlUtils.hasAttribute(document.getRootElement().element("person"), "name"));
    }

    @Test
    void dumpXML() throws IOException {
        Node node = new DefaultDocument();
        Document document = loadDocument("file2.xml");
        node.setDocument(document);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n", XmlUtils.dumpXML(node));
    }

    @Test
    void formatXML() throws IOException {
        String xmlContent = getDocumentAsString("file2.xml");
        assertThat(XmlUtils.formatXML(xmlContent)).contains("<root>","</root>","<person>","</person>");
    }

    @Test
    void formatXMLWithCompact() throws IOException {
        String xmlContent = getDocumentAsString("file2.xml");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><person><name>John Doe</name><age>30</" +
                        "age><city>Seattle</city></person><person><name>Jane Smith</name><age>25</age><city>Portland</" +
                        "city></person></root>", XmlUtils.formatXML(xmlContent,true));
    }

    private Document loadDocument(String fileName) {
        return XmlUtils.loadDocument(getXmlStream(fileName));
    }

    private InputStream getXmlStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private String getDocumentAsString(String filename) throws IOException {
        return IOUtils.getInputStreamAsString(getXmlStream(filename));
    }
}