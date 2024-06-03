package net.microfalx.lang;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.StringUtils.isEmpty;

/**
 * Utilities around XML.
 */
public class XmlUtils {

    /**
     * Loads an XML file from a stream.
     *
     * @param is the input stream
     * @return a non-null instance
     */
    public static Document loadDocument(InputStream is) {
        SAXReader saxReader = createSAXReader();
        try {
            return saxReader.read(is);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Loads an XML file from a stream.
     *
     * @param xml the XML content
     * @return a non-null instance
     */
    public static Document loadDocument(String xml) {
        return loadDocument(new StringReader(xml));
    }

    /**
     * Loads an XML file from a stream.
     *
     * @param reader the reader
     * @return a non-null instance
     */
    public static Document loadDocument(Reader reader) {
        SAXReader saxReader = createSAXReader();
        try {
            return saxReader.read(reader);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Loads an XML file from a file.
     *
     * @param file the file
     * @return a non-null instance
     */
    public static Document loadDocument(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        return loadDocument(fileReader);
    }

    /**
     * Creates an empty XML document.
     *
     * @return a non-null instance
     */
    public static Document createDocument() {
        return DocumentHelper.createDocument();
    }

    /**
     * Selects a list of elements using an XPath expression.
     *
     * @param document        the document
     * @param xpathExpression the XPath expression
     * @return a non-null instance
     */
    public static List<Element> selectElements(Document document, String xpathExpression) {
        return selectElements(document.getRootElement(), xpathExpression);
    }

    /**
     * Selects a list of elements using an XPath expression.
     *
     * @param element         the element
     * @param xpathExpression the XPath expression
     * @return a non-null instance
     */
    public static List<Element> selectElements(Element element, String xpathExpression) {
        List<Element> elements = new ArrayList<>();
        List<Node> nodes = element.selectNodes(xpathExpression);
        for (Node node : nodes) {
            if (node instanceof Element) elements.add((Element) node);
        }
        return elements;
    }

    /**
     * Returns the text behind an child element.
     *
     * @param element the parent element
     * @param name    the name of the child element
     * @return the text
     */
    public static String getRequiredElementText(Element element, String name) {
        Element child = element.element(name);
        if (child == null) {
            throw new IllegalArgumentException("Element " + name + " is required in " + element.getName());
        }
        String textTrim = child.getTextTrim();
        if (isEmpty(textTrim)) {
            throw new IllegalArgumentException("Element " + name + " cannot be empty in " + element.getName());
        }

        return textTrim;
    }

    /**
     * Returns the text behind an element as an integer.
     *
     * @param element      the parent element
     * @param name         the name of the child element
     * @param defaultValue the default value
     * @return the int value
     */
    public static int getElementTextAsInteger(Element element, String name, int defaultValue) {
        String text = getElementText(element, name);
        if (text == null) return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the text behind an element as a boolean.
     *
     * @param element      the parent element
     * @param name         the name of the child element
     * @param defaultValue the default value
     * @return the int value
     */
    public static boolean getElementTextAsBoolean(Element element, String name, boolean defaultValue) {
        String text = getElementText(element, name);
        if (text == null) return defaultValue;
        return asBoolean(text, defaultValue);
    }

    /**
     * Returns the text from an element.
     *
     * @param element the element
     * @return the text, can be NULL
     */
    public static String getElementText(Element element) {
        return getElementText(element, true);
    }

    /**
     * Returns the text from an element.
     *
     * @param element the element
     * @param trim    <code>true</code> to trim the text, <code>false</code> otherwise
     * @return the text, can be NULL
     */
    public static String getElementText(Element element, boolean trim) {
        requireNonNull(element);
        if (trim) {
            return element.getTextTrim();
        } else {
            return element.getText();
        }
    }

    /**
     * Returns the text behind an element.
     *
     * @param element the parent element
     * @param name    the name of the child element
     * @return the text
     */
    public static String getElementText(Element element, String name) {
        return getElementText(element, name, null);
    }

    /**
     * Returns the text behind an element.
     *
     * @param element      the parent element
     * @param name         the name of the child element
     * @param defaultValue the default value
     * @return the text
     */
    public static String getElementText(Element element, String name, String defaultValue) {
        return getElementText(element, name, defaultValue, true);
    }

    /**
     * Returns the text behind an element.
     *
     * @param element      the parent element
     * @param name         the name of the child element
     * @param defaultValue the default value
     * @param trim         <code>true</code> to trim the text, <code>false</code> otherwise
     * @return the text
     */
    public static String getElementText(Element element, String name, String defaultValue, boolean trim) {
        Element child = element.element(name);
        if (child == null) return defaultValue;
        if (trim) {
            return child.getTextTrim();
        } else {
            return child.getText();
        }
    }

    /**
     * Returns the attribute of an element as a boolean.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @param defaultValue  the default value
     * @return the value as boolean
     */
    public static boolean getAttribute(Element element, String attributeName, boolean defaultValue) {
        String value = getAttribute(element, attributeName, (String) null);
        if (value == null) return defaultValue;
        return asBoolean(value, defaultValue);
    }

    /**
     * Returns the attribute of an element as an integer.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @param defaultValue  the default value
     * @return the value as integer
     */
    public static Integer getAttribute(Element element, String attributeName, Integer defaultValue) {
        String value = getAttribute(element, attributeName, (String) null);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the attribute of an element as a double.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @param defaultValue  the default value
     * @return the value as double
     */
    public static Double getAttribute(Element element, String attributeName, Double defaultValue) {
        String value = getAttribute(element, attributeName, (String) null);
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Returns the attribute of an element as a string.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @return the value as string, null if not available
     */
    public static String getAttribute(Element element, String attributeName) {
        return getAttribute(element, attributeName, (String) null);
    }

    /**
     * Returns the attribute of an element as a string.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @param defaultValue  the default value
     * @return the value as string
     */
    public static String getAttribute(Element element, String attributeName, String defaultValue) {
        Attribute attribute = element.attribute(attributeName);
        if (attribute == null) return defaultValue;
        return attribute.getValue();
    }

    /**
     * Returns the required attribute of an element as a string.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @return the value as string
     */
    public static String getRequiredAttribute(Element element, String attributeName) {
        Attribute attribute = element.attribute(attributeName);
        if (attribute == null)
            throw new IllegalArgumentException("Attribute " + attributeName + " is required in " + element.getName());
        return attribute.getValue();
    }

    /**
     * Returns the attribute of an element as an enum.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @param defaultEnum   the default value
     * @return the value as string
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum> T getAttribute(Element element, String attributeName, Class<T> enumClass, T defaultEnum) {
        String defaultValue = null;
        if (defaultEnum != null) defaultValue = defaultEnum.name();
        String value = getAttribute(element, attributeName, defaultValue);
        if (isEmpty(value)) return defaultEnum;
        value = value.toUpperCase().replace('-', '_');
        return (T) Enum.valueOf(enumClass, value);
    }

    /**
     * Removes an attribute.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @return <code>true</code> if was removed, <code>false</code> otherwise
     */
    public static boolean removeAttribute(Element element, String attributeName) {
        Attribute attr = element.attribute(attributeName);
        if (attr != null) {
            element.remove(attr);
            return true;
        }
        return false;
    }

    /**
     * Returns whether an element has a give attribute.
     *
     * @param element       the element
     * @param attributeName the attribute name
     * @return <code>true</code> if exists, <code>false</code> otherwise
     */
    public static boolean hasAttribute(Element element, String attributeName) {
        return element.attribute(attributeName) != null;
    }

    /**
     * Writes an XML node to a string.
     *
     * @param node the node
     * @return the XML
     * @throws IOException if an I/O exception occurs
     */
    public static String dumpXML(Node node) throws IOException {
        requireNonNull(node);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(node);
        writer.flush();
        return out.toString();
    }

    /**
     * Formats an XML.
     *
     * @param xml the XML content
     * @return the formatted XML
     * @throws IOException if an I/O exception occurs
     */
    public static String formatXML(String xml) throws IOException {
        return formatXML(xml, false);
    }

    /**
     * Formats an XML.
     *
     * @param xml     the XML content
     * @param compact <code>true</code> to compact the XML, <code>false</code> for pretty print
     * @return the formatted XML
     * @throws IOException if an I/O exception occurs
     */
    public static String formatXML(String xml, boolean compact) throws IOException {
        requireNonNull(xml);
        Document document = loadDocument(xml);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputFormat format = compact ? OutputFormat.createCompactFormat() : OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.flush();
        return out.toString();
    }

    private static boolean asBoolean(String text, boolean defaultValue) {
        return StringUtils.asBoolean(text, defaultValue);
    }

    private static SAXReader createSAXReader() {
        SAXReader reader = new SAXReader();
        reader.setIgnoreComments(true);
        reader.setMergeAdjacentText(true);
        reader.setValidation(false);
        return reader;
    }
}
