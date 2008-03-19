/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.common.xpath;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMObservableStub;
import com.volantis.mcs.eclipse.common.odom.ODOMText;
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.xml.xpath.DOMType;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import junitx.util.PrivateAccessor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test the ODOMXPath wrapper object.
 */
public class XPathTestCase
        extends TestCaseAbstract {

    /**
     * The xml string to use to create an ODOM document.
     */
    private String xml =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            "<catalog>" +
            "  <cd country=\"USA\">" +
            "    <title attributeToPreventDeletion=\"\">Empire Burlesque</title>" +
            "    <artist>Bob Dylan</artist>" +
            "    <price>10.90</price>" +
            "  </cd>" +
            "  <cd country=\"UK\">" +
            "    <title>Hide your heart</title>" +
            "    <artist>Bonnie Tyler</artist>" +
            "    <price>9.90</price>" +
            "  </cd>" +
            "  <cd country=\"USA\">" +
            "    <title>Greatest Hits</title> " +
            "    <artist>Dolly Parton</artist>" +
            "    <price>9.90</price>" +
            "  </cd>" +
            "  <cd country=\"Australia\">" +
            "    <title testAttribute=\"remove\">Down Under with Bruce</title> " +
            "    <artist>Rolf Harris</artist>" +
            "    <price testRemove=\"remove price too\"></price>" +
            "  </cd>" +
            "  <ns1:newElement xmlns:ns1=\"http://www.w3.org/2001/XMLSchema\" country=\"Nowhere\">" +
            "  </ns1:newElement>" +
            "  <ns2:newElement2  xmlns:ns2='urn:XPathTestCase' attr2='attr2Value'>" +
            "     <newElement2_2 attr2_2='attr2_2Value'>" +
            "        newElement2_2_text" +
            "     </newElement2_2>" +
            "  </ns2:newElement2>" +
            "</catalog>";

    /**
     * The ODOM factory used to construct documents
     */
    private ODOMFactory factory;

    /**
     * The JDOM document.
     */
    private Document doc;

    /**
     * The root element of the JDOM document.
     */
    private Element root;

    private static final Namespace[] NAMESPACES = new Namespace[]{
        Namespace.getNamespace("ns1", "http://www.w3.org/2001/XMLSchema")
    };

    /**
     * Dummy implementation of ODOMObservable (that isn't an ODOMElement,
     * ODOMText or ODOMAttribute)
     */
    private final ODOMObservable odomObservable = new ODOMObservableStub();


    // javadoc inherited
    protected void setUp() throws Exception {
        setUp(xml);
    }


    protected void setUp(String source) throws Exception {
        factory = new ODOMFactory();

        SAXBuilder builder = new SAXBuilder();
        builder.setFactory(factory);

        doc = builder.build(new StringReader(source));
        root = doc.getRootElement();
    }


    /**
     * Test all constructors.
     */
    public void testConstructors() throws Exception {
        String path = "/";
        Namespace[] namespacesNull = null;
        Map emptyMap = new HashMap();
        Map map = new HashMap();
        for (int i = 0; i < NAMESPACES.length; i++) {
            Namespace namespace = NAMESPACES[i];
            map.put(namespace.getPrefix(), namespace.getURI());
        }
        // Check using string path.
        verifyObjectState(new ODOMXPath(path), path, emptyMap);
        verifyObjectState(new ODOMXPath(path, namespacesNull), path, emptyMap);
        verifyObjectState(new ODOMXPath(path, NAMESPACES), path, map);

        // Check using element.
        Element parent = new Element("parent");
        String xpath = "/parent/element";
        Element element = new Element("element");
        parent.addContent(element);
        verifyObjectState(new ODOMXPath(element), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(element, namespacesNull), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(element, NAMESPACES), xpath, map);

        // Check using attribute.
        xpath = "/parent/@attribute";
        parent = new Element("parent");
        Attribute attribute = new Attribute("attribute", "");
        parent.setAttribute(attribute);
        verifyObjectState(new ODOMXPath(attribute), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(attribute, namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(attribute, NAMESPACES), xpath, map);

        // Check using text.
        xpath = "/parent/text()";
        parent = new Element("parent");
        Text text = new Text("textExample");
        parent.addContent(text);
        verifyObjectState(new ODOMXPath(text), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(text, namespacesNull), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(text, NAMESPACES), xpath, map);

        // Check using ODOMObservable.
        xpath = "/parent/textExample";
        parent = new Element("parent");
        ODOMObservable node = (ODOMElement) factory.element("textExample");
        parent.addContent((Element) node);
        verifyObjectState(new ODOMXPath(node), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(node, namespacesNull), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(node, NAMESPACES), xpath, map);

        // Check using element.
        xpath = "/parent/element";
        parent = new Element("parent");
        Element start = new Element("startElement");
        element = new Element("element");
        parent.addContent(element);
        verifyObjectState(new ODOMXPath(start, element), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(start, element, namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(start, element, NAMESPACES), xpath, map);

        // Check using attribute.
        xpath = "/parent/@attribute";
        parent = new Element("parent");
        start = new Element("startElement");
        attribute = new Attribute("attribute", "");
        parent.setAttribute(attribute);
        verifyObjectState(new ODOMXPath(start, attribute), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(start, attribute, namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(start, attribute, NAMESPACES), xpath, map);

        // Check using text.
        xpath = "/parent/text()";
        parent = new Element("parent");
        text = new Text("textExample");
        parent.addContent(text);
        start = new Element("startElement");
        verifyObjectState(new ODOMXPath(start, text), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(start, text, namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(start, text, NAMESPACES), xpath, map);

        // Check using ODOMObservable.
        xpath = "/parent/textExample";
        parent = new Element("parent");
        node = (ODOMElement) factory.element("textExample");
        parent.addContent((Element) node);
        start = new Element("startElement");
        verifyObjectState(new ODOMXPath(start, node), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(start, node, namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(start, node, NAMESPACES), xpath, map);

        // Check using ODOMObservable.
        xpath = "/xpath/to/this/value/isOK";
        ODOMXPath testXPath = new ODOMXPath("/xpath/to/this/value");
        verifyObjectState(new ODOMXPath(testXPath, "isOK"), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(testXPath, "isOK", namespacesNull), xpath,
                          emptyMap);
        verifyObjectState(new ODOMXPath(testXPath, "isOK", NAMESPACES), xpath,
                          map);

        // Check using attribute.
        xpath = "/start/element/@attribute";
        parent = new Element("parent");
        attribute = new Attribute("attribute", "");
        parent.setAttribute(attribute);
        ODOMXPath startXPath = new ODOMXPath("/start/element");
        verifyObjectState(new ODOMXPath(startXPath, attribute), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(startXPath, attribute, namespacesNull),
                          xpath, emptyMap);
        verifyObjectState(new ODOMXPath(startXPath, attribute, NAMESPACES), xpath,
                          map);

        // Check using attribute.
        xpath = "/start/element/relative/xpath";
        parent = new Element("parent");
        attribute = new Attribute("attribute", "");
        parent.setAttribute(attribute);
        startXPath = new ODOMXPath("/start/element");
        ODOMXPath relative = new ODOMXPath("relative/xpath");
        verifyObjectState(new ODOMXPath(startXPath, relative), xpath, emptyMap);
        verifyObjectState(new ODOMXPath(startXPath, relative, namespacesNull),
                          xpath, emptyMap);
        verifyObjectState(new ODOMXPath(startXPath, relative, NAMESPACES), xpath,
                          map);

    }

    /**
     * Test that the getDOMType() method behaves as expected for each of
     * the three current DOMTypes.
     */
    public void testGetDOMType() {
        ODOMXPath xPath = new ODOMXPath("/one/two/three");
        assertSame("Expected an ELEMENT_TYPE", DOMType.ELEMENT_TYPE,
                xPath.getDOMType());
        xPath = new ODOMXPath("/one/two/three@attribute");
        assertSame("Expected an ATTRIBUTE_TYPE", DOMType.ATTRIBUTE_TYPE,
                xPath.getDOMType());
        xPath = new ODOMXPath("/one/two/text()hello");
        assertSame("Expected a TEXT_TYPE", DOMType.TEXT_TYPE,
                xPath.getDOMType());
    }

    /**
     * This tests both the encoded namespaces constructor and the
     * {@link ODOMXPath#getNamespacesString} method.
     */
    public void testEncodedNamespaces() throws Exception {
        String path = "/";
        Namespace[] namespacesNull = null;
        Namespace[] namespacesOne = new Namespace[]{
            Namespace.getNamespace("first", "http://first/uri")
        };
        Namespace[] namespacesMultiple = new Namespace[]{
            Namespace.getNamespace("first", "http://first/uri"),
            Namespace.getNamespace("second", "http://second/uri")
        };
        Map emptyMap = new HashMap();
        Map mapOne = new HashMap();
        Map mapMultiple = new HashMap();
        ODOMXPath encodedNamespaces;
        ODOMXPath encodedNamespacesNull;
        ODOMXPath encodedNamespacesOne;
        ODOMXPath encodedNamespacesMultiple;
        int i;

        for (i = 0; i < namespacesOne.length; i++) {
            mapOne.put(namespacesOne[i].getPrefix(),
                       namespacesOne[i].getURI());
        }

        for (i = 0; i < namespacesMultiple.length; i++) {
            mapMultiple.put(namespacesMultiple[i].getPrefix(),
                            namespacesMultiple[i].getURI());
        }

        // These constructors have been tested in another test
        encodedNamespaces = new ODOMXPath(path);
        encodedNamespacesNull = new ODOMXPath(path, namespacesNull);
        encodedNamespacesOne = new ODOMXPath(path, namespacesOne);
        encodedNamespacesMultiple = new ODOMXPath(path, namespacesMultiple);

        verifyObjectState(new ODOMXPath(path, (String) null),
                          path, emptyMap);
        verifyObjectState(
                new ODOMXPath(path, encodedNamespaces.getNamespacesString()),
                path, emptyMap);
        verifyObjectState(
                new ODOMXPath(path, encodedNamespacesNull.getNamespacesString()),
                path, emptyMap);
        verifyObjectState(
                new ODOMXPath(path, encodedNamespacesOne.getNamespacesString()),
                path, mapOne);
        verifyObjectState(new ODOMXPath(path,
                                    encodedNamespacesMultiple.getNamespacesString()),
                          path, mapMultiple);
    }


    /**
     * Test constructors.
     */
    public void testConstructorsThatShouldAnException() throws Exception {
        try {
            new ODOMXPath((String) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath("");
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath("", (Namespace[]) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((ODOMXPath) null, "");
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((ODOMXPath) null, "/relativePath");
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath("", (String) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(null, "/relativePath", null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(odomObservable);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(odomObservable, null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(null, odomObservable, null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        new ODOMXPath((ODOMXPath) null, (ODOMXPath) null);
        try {
            new ODOMXPath(null, new ODOMXPath("/"));
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        // Test null relative ODOMXPath doesn't recurively call the same constructor.
        new ODOMXPath((ODOMXPath) null, (ODOMXPath) null);

        try {
            new ODOMXPath((Attribute) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((Attribute) null, null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((ODOMXPath) null, new Attribute("", ""));
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ODOMXPath((ODOMXPath) null, new Attribute("", ""), null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((Element) null, (Attribute) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(new Attribute("", ""));
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(new Attribute("", ""), null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ODOMXPath((Element) null, new Attribute("", ""), null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((Text) null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath((Text) null, null);
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

        try {
            new ODOMXPath(new Text(""));
            fail("Expected an exception.");
        } catch (IllegalArgumentException e) {
        }

    }


    private Map getNamespacePrivateField(XPath xpath) throws Exception {
        return (Map) PrivateAccessor.getField(xpath, "namespaceURIMap");
    }


    private void verifyObjectState(ODOMXPath xpath, String xpathStr, Map map)
            throws Exception {
        assertEquals("ODOMXPath should match: ", xpathStr,
                     xpath.getExternalForm());
        assertEquals("Namespace should match: ", map,
                     getNamespacePrivateField(xpath));
    }


    /**
     * Test the additional namespaces. In particular, test the ODOMXPath(ODOMXPath,..)
     * and ODOMXPath(String) constructors. Also need to verify that the constructors
     * that take elements as parameters pass on their namespaces to the map.
     *
     * In addition to this we need to verify that erroneous namespaces (same
     * prefix but different URI's are ignored).
     */
    public void testAdditionalNamespaces() throws Exception {
        Namespace namespaces[] = new Namespace[]{
            Namespace.getNamespace("test1",
                                   "http://www.w3.org/2001/XMLSchema1"),
            Namespace.getNamespace("test2",
                                   "http://www.w3.org/2001/XMLSchema2"),
        };
        // Test that the xpath created contains just the namespaces.
        ODOMXPath xpath = new ODOMXPath("/path", NAMESPACES);
        Map namespaceMap = new HashMap();
        for (int i = 0; i < NAMESPACES.length; i++) {
            namespaceMap.put(NAMESPACES[i].getPrefix(),
                             NAMESPACES[i].getURI());
        }
        assertEquals("Namespace should match: ", namespaceMap,
                     getNamespacePrivateField(xpath));

        // Test the combination of namespaces maps...
        ODOMXPath xpathCombined = new ODOMXPath(xpath, (String) null, namespaces);
        for (int i = 0; i < namespaces.length; i++) {
            namespaceMap.put(namespaces[i].getPrefix(), namespaces[i]);
        }
        assertEquals("Namespace should match: ", namespaceMap.size(),
                     ((Map) getNamespacePrivateField(xpathCombined)).size());
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPath() throws Exception {
        ODOMXPath xpath = new ODOMXPath("/");
        verifyResult("/", xpath.getExternalForm());
    }


    /**
     * Test the creation of a relative xpath with the root element.
     */
    public void testCreateXPathWithRoot() throws Exception {
        ODOMXPath xpath = new ODOMXPath(root);
        verifyResult("/catalog", xpath.getExternalForm());
    }


    /**
     * Test the creation of a relative xpath with the root element.
     */
    public void testCreateXPathWithRootRoot() throws Exception {
        ODOMXPath xpath = new ODOMXPath(root, root);
        verifyResult(".", xpath.getExternalForm());
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithElement() throws Exception {
        doTest(null, root, "/catalog");
        doTest(null, root.getChild("cd"), "/catalog/cd[1]");
        doTest(null, root.getChild("cd").getChild("title"),
               "/catalog/cd[1]/title");
        try {
            doTest(null, root.getChild("NOTFOUND"), ".");
            fail("Element parameter which is null should not be allowed.");
        } catch (Exception e) {
        }
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithAttribute() throws Exception {
        Element child = root.getChild("cd");
        doTest(null, child.getAttribute("country"), "/catalog/cd[1]/@country");
        try {
            doTest(null, child.getAttribute("NOTFOUND"), "");
            fail("Expected an Illegal Argument Exception");
        } catch (IllegalArgumentException e) {
        }
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithText() throws Exception {
        Element child = root.getChild("cd").getChild("title");
        doTest(null, (Text) child.getContent().get(0),
               "/catalog/cd[1]/title/text()");
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithObservable() throws Exception {
        doTest(null, (ODOMObservable) root, "/catalog");
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithElementRelative() throws Exception {
        doTest(root, root.getChild("cd").getChild("artist"), "cd[1]/artist");
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithAttributeRelative() throws Exception {
        doTest(root, root.getChild("cd").getAttribute("country"),
               "cd[1]/@country");
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithTextRelative() throws Exception {
        doTest(root, (Text) root.getChild("cd").getChild("title").
                            getContent().get(0), "cd[1]/title/text()");
    }


    /**
     * Test the creation of a relative xpath with an observable element.
     */
    public void testCreateXPathWithObservableRelative() throws Exception {
        doTest(root, (ODOMObservable) root.getChild("cd").getChild("artist"),
               "cd[1]/artist");
    }


    /**
     * Overloaded helper method.
     */
    protected void doTest(Element start, Attribute end, String expected) {
        ODOMXPath xpath = new ODOMXPath(start, end);
        verifyResult(expected, xpath.getExternalForm());
    }


    /**
     * Overloaded helper method.
     */
    protected void doTest(Element start, Text end, String expected) {
        ODOMXPath xpath = new ODOMXPath(start, end);
        verifyResult(expected, xpath.getExternalForm());
    }


    /**
     * Overloaded helper method.
     */
    protected void doTest(Element start, ODOMObservable end, String expected) {
        ODOMXPath xpath = new ODOMXPath(start, end);
        verifyResult(expected, xpath.getExternalForm());
    }


    /**
     * Overloaded helper method.
     */
    protected void doTest(Element start, Element end, String expected) {
        ODOMXPath xpath = new ODOMXPath(start, end);
        verifyResult(expected, xpath.getExternalForm());
    }


    /**
     * Helper method.
     */
    protected void verifyResult(String expected, String actual) {
        assertEquals("Values should match", expected, actual);
    }


    /**
     * Test the creation of an ODOMXPath when node exists.
     */
    public void testCreateNodeExists() throws Exception {
        // Check when element exists
        ODOMXPath path = new ODOMXPath("cd[2]");
        ODOMObservable result = path.create((ODOMElement) root, factory);
        assertNull("Result should be null", result);

        // Check when attribute exists
        path = new ODOMXPath("cd[2]/@country");
        result = path.create((ODOMElement) root, factory);
        assertNull("Result should be null", result);

        // Check when text exists
        path = new ODOMXPath("cd[2]/title/text()");
        result = path.create((ODOMElement) root, factory);
        assertNull("Result should be null", result);
    }


    /**
     * Test the creation of an ODOMXPath when node exists.
     */
    public void testCreateNodeTooGeneral() throws Exception {
        // Check when element exists
        ODOMXPath path = new ODOMXPath("cd");
        try {
            path.create((ODOMElement) root, factory);
            fail("Expected an IllegalStateException.");
        } catch (IllegalStateException e) {
        }
    }


    /**
     * Test creation of a multi-step path where none of the path element steps
     * exist and where all but the last element step exists.
     */
    public void testCreateElementMultiStep() throws Exception {
        ODOMElement context = (ODOMElement) factory.element("root");
        ODOMXPath path = new ODOMXPath("a/b");

        // Test creation of whole path from empty context
        ODOMObservable newNode = path.create(context, factory);

        assertTrue("New node not an element (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Element);

        assertEquals("Created element path not as",
                     "/root/a/b",
                     new ODOMXPath(newNode).getExternalForm());

        // Test creation of single additional step
        path = new ODOMXPath(path, "c");

        newNode = path.create(context, factory);

        assertTrue("New node not an element (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Element);

        assertEquals("Created element path not as",
                     "/root/a/b/c",
                     new ODOMXPath(newNode).getExternalForm());
    }


    /**
     * Test creation of a multi-step path where none of the path element steps
     * exist and where all the element steps exist.
     */
    public void testCreateAttributeMultiStep() throws Exception {
        ODOMElement context = (ODOMElement) factory.element("root");
        ODOMXPath path = new ODOMXPath("a/@b");

        // Test creation of whole path from empty context
        ODOMObservable newNode = path.create(context, factory);

        assertTrue("New node not an attribute (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Attribute);

        assertEquals("Created attribute path not as",
                     "/root/a/@b",
                     new ODOMXPath(newNode).getExternalForm());

        // Test creation of single additional step
        path = new ODOMXPath("a/@c");

        newNode = path.create(context, factory);

        assertTrue("New node not an attribute (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Attribute);

        assertEquals("Created attribute path not as",
                     "/root/a/@c",
                     new ODOMXPath(newNode).getExternalForm());
    }


    /**
     * Test creation of a multi-step path where none of the path element steps
     * exist and where all the element steps exist.
     */
    public void testCreateTextMultiStep() throws Exception {
        ODOMElement context = (ODOMElement) factory.element("root");
        ODOMXPath path = new ODOMXPath("a/b/text()");

        // Test creation of whole path from empty context
        ODOMObservable newNode = path.create(context, factory);

        assertTrue("New node not text (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Text);

        assertEquals("Created text path not as",
                     "/root/a/b/text()",
                     new ODOMXPath(newNode).getExternalForm());

        // Test creation of single additional step
        path = new ODOMXPath("a/text()");

        newNode = path.create(context, factory);

        assertTrue("New node not text (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Text);

        assertEquals("Created text path not as",
                     "/root/a/text()",
                     new ODOMXPath(newNode).getExternalForm());
    }


    /**
     * Test creation of multi-step paths where some of the path element steps
     * already exist, but two or more do not. This tests scenarios with
     * element, attribute and text terminal steps.
     */
    public void testCreateVariousMultiStep() throws Exception {
        // Because I want a different document structure for this testing
        // than the one created in setUp, this test checks a number of
        // different creation scenarios (over and above those cases handled
        // in existing tests) to avoid too much setup overhead.
        String source =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "    <catalogue>" +
                "        <page number=\"1\">" +
                "            <item number=\"238-1322\">" +
                "                <name>Bruce the Shark</name>" +
                "                <price currency=\"GBP\">12.99</price>" +
                "                <description>" +
                "                    Talking and moving plush toy" +
                "                </description>" +
                "            </item>" +
                "        </page>" +
                "    </catalogue>" +
                "</root>";

        setUp(source);

        // This should cause the creation of a second page with a number
        // attribute
        ODOMXPath path = new ODOMXPath("catalogue/page[2]/@number");

        ODOMObservable newNode = path.create((ODOMElement) root, factory);

        assertTrue("New node not an attribute (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Attribute);

        assertEquals("Created page attribute path not as",
                     "/root/catalogue/page[2]/@number",
                     new ODOMXPath(newNode).getExternalForm());

        // This should cause the creation of a new item with nested name
        // element
        path = new ODOMXPath("catalogue/page[2]/item/name");

        newNode = path.create((ODOMElement) root, factory);

        assertTrue("New node not an element (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Element);

        assertEquals("Created item name element path not as",
                     "/root/catalogue/page[2]/item/name",
                     new ODOMXPath(newNode).getExternalForm());

        // This should cause the creation of a text node within the previously
        // created item's name element
        path = new ODOMXPath(path, "text()");

        newNode = path.create((ODOMElement) root, factory);

        assertTrue("New node not a text (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Text);

        assertEquals("Created item name text path not as",
                     "/root/catalogue/page[2]/item/name/text()",
                     new ODOMXPath(newNode).getExternalForm());
    }


    /**
     * Tests multi-step path creation where the path is absolute.
     */
    public void testCreateWithAbsolutePath() throws Exception {
        ODOMXPath path = new ODOMXPath("/catalog/cd[6]/title/text()");

        // This should cause the creation of CD nodes 5 and 6 and a title
        // node within CD 6 with a nested text node
        ODOMObservable newNode = path.create((ODOMElement) root, factory);

        assertTrue("New CD title text not a text (is a " +
                   newNode.getClass().getName() + ")",
                   newNode instanceof Text);

        assertEquals("Created CD title text path not as",
                     path.getExternalForm(),
                     new ODOMXPath(newNode).getExternalForm());
    }


    /**
     * Tests multi-step path creation where the path is absolute and the
     * context is not valid for the absolute path given.
     */
    public void testCreateWithAbsolutePathBadContext() throws Exception {
        ODOMXPath path = new ODOMXPath("/catalog/cd[6]/title/text()");

        // This will demonstrate invalid application of an absolute path
        // (the context is not appropriate to the absolute path because its
        // name doesn't match the first step in the absolute path)
        try {
            ODOMElement cd = (ODOMElement) root.getChild("cd");
            path.create(cd, factory);

            fail("Should have received an XPathException");
        } catch (XPathException e) {
            // Expected condition
        }
    }


    /**
     * Test the creation of an ODOMXPath.
     */
    public void testCreateSimple() throws Exception {
        // Check when element does not exist.
        String elementName = "newElement";
        ODOMXPath path = new ODOMXPath(elementName);
        ODOMObservable result = path.create((ODOMElement) root, factory);
        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Element);
        assertEquals("Value should match", elementName,
                     ((Element) result).getName());

        // Check when attribute does not exist.
        path = new ODOMXPath("newElement/@attribute");
        result = path.create((ODOMElement) root, factory);
        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Attribute);
        assertEquals("Value should match", "attribute",
                     ((Attribute) result).getName());
        assertEquals("Value should match", "",
                     ((Attribute) result).getValue());

        // Check when text does not exist.
        path = new ODOMXPath("newElement/text()");
        result = path.create((ODOMElement) root, factory);

        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Text);
        assertEquals("Value should match", "",
                     ((Text) result).getText());
        ((Text) result).setText("Testing");
    }


    /**
     * Test the creation of an ODOMXPath.
     */
    public void testCreateWithNamespace() throws Exception {
        // Check when element does not exist.
        String elementName = "ns1:newElement/notNamespaced/ns1:namespaced";
        ODOMXPath path = new ODOMXPath(elementName);
        ODOMObservable result = null;
        try {
            result = path.create((ODOMElement) root, factory);
            fail("Expected IllegalStateException (no namespaces set)");
        } catch (IllegalStateException e) {
        }
        path = new ODOMXPath(elementName, NAMESPACES);
        result = path.create((ODOMElement) root, factory);
        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Element);
        assertEquals("Value should match", "namespaced",
                     ((Element) result).getName());
        assertEquals("Value should match", "ns1",
                     ((Element) result).getNamespace().getPrefix());
        Element parent = result.getParent();
        assertEquals("Value for parent should match",
                     "notNamespaced", parent.getName());
        assertEquals("Prefix for parent namespace should match",
                     "", parent.getNamespacePrefix());
    }


    /**
     * Test the creation of an ODOMXPath.
     */
    public void testCreateWithNamespaceExists() throws Exception {
        // Check when element does not exist.
        String elementName = "ns1:newElement";
        ODOMXPath path = new ODOMXPath(elementName);
        ODOMObservable result = null;
        try {
            result = path.create((ODOMElement) root, factory);
            fail("Expected IllegalStateException (no namespaces set)");
        } catch (IllegalStateException e) {
        }
        path = new ODOMXPath(elementName, NAMESPACES);
        result = path.create((ODOMElement) root, factory);
        assertNull("Result should be null", result);
    }


    /**
     * Test the creation of an ODOMXPath.
     */
    public void testCreateWithPredicateNoneExist() throws Exception {
        // Check when element does not exist.
        String elementName = "predicateElement[4]";
        ODOMXPath path = new ODOMXPath(elementName);
        ODOMObservable result = null;
        result = path.create((ODOMElement) root, factory);
        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Element);
        assertEquals("Value should match", "predicateElement",
                     ((Element) result).getName());

        assertNotNull("Should now be in the ODOM",
                      path.selectSingleNode((ODOMElement) root));


    }


    /**
     * Test the creation of an ODOMXPath.
     */
    public void testCreateWithPredicateForText() throws Exception {
        // Check when element does not exist.
        String path = "cd[1]/title/text()[2]";
        ODOMXPath xpath = new ODOMXPath(path);
        ODOMObservable result = xpath.create((ODOMElement) root, factory);

        assertNotNull("Result should not be null", result);
        assertTrue("Type should match", result instanceof Text);
        ((Text) result).setText("Garbage");
        //showDocument();
        assertNotNull("Should now be in the ODOM",
                      xpath.selectSingleNode((ODOMElement) root));

    }


    /**
     * Tests that the {@link ODOMXPath#selectSingleNode} method works when the
     * the ODOMXPath is associated with a namespace
     *
     * @throws Exception if an error occurs
     */
    public void testSelectSingleNodeWithNamespaces() throws Exception {

        // select the newElement node in the
        // http://www.w3.org/2001/XMLSchema namespace.
        ODOMXPath xpath = new ODOMXPath("ns1:newElement", NAMESPACES);
        ODOMObservable node = xpath.selectSingleNode((ODOMObservable) root);

        // ensure the node is not null
        assertNotNull("node selected was null", node);

        // should have returned a ODOMElement
        assertEquals("should have returned a ODOMElement",
                     ODOMElement.class,
                     node.getClass());

        ODOMElement element = (ODOMElement) node;

        // check the name of the element
        assertEquals("element should be named 'newElement'",
                     "newElement",
                     element.getName());

        // check the namespaceURI of the element
        assertEquals("element bound to an unexpected namespace",
                     "http://www.w3.org/2001/XMLSchema",
                     element.getNamespaceURI());
    }


    /**
     * Tests that the {@link ODOMXPath#selectSingleNode} method works when the
     * the ODOMXPath is associated with a namespace that is the same namespace
     * as the context nodes namespace but that the prefixes are different
     *
     * @throws Exception if an error occurs
     */
    public void testSelectSingleNodeWithNamespacePrefixes() throws Exception {

        // select the newElement node in the
        // http://www.w3.org/2001/XMLSchema namespace.

        // use a different prefix (same URI) to that in the DOM
        ODOMXPath xpath = new ODOMXPath("p:newElement", new Namespace[]{
            Namespace.getNamespace("p", "http://www.w3.org/2001/XMLSchema")
        });

        ODOMObservable node = xpath.selectSingleNode((ODOMObservable) root);

        // ensure the node is not null
        assertNotNull("node selected was null", node);

        // should have returned a ODOMElement
        assertEquals("should have returned a ODOMElement",
                     ODOMElement.class,
                     node.getClass());

        ODOMElement element = (ODOMElement) node;

        // check the name of the element
        assertEquals("element should be named 'newElement'",
                     "newElement",
                     element.getName());

        // check the namespaceURI of the element
        assertEquals("element bound to an unexpected namespace",
                     "http://www.w3.org/2001/XMLSchema",
                     element.getNamespaceURI());
    }


    /**
     * Tests that the {@link ODOMXPath#selectSingleElement} method
     *
     * @throws Exception if an error occurs
     */
    public void testSelectElementNode() throws Exception {

        ODOMXPath xpath = new ODOMXPath("cd[1]");
        // select the first cd element
        Element element = xpath.selectSingleElement(root);

        // ensure the node is not null
        assertNotNull("node selected was null", element);

        // check the name of the element
        assertEquals("element should be named 'cd'",
                     "cd",
                     element.getName());

        // check the namespaceURI of the element
        assertEquals("element bound to an unexpected namespace",
                     "",
                     element.getNamespaceURI());
    }


    /**
     * Tests that the {@link ODOMXPath#selectSingleElement} method throws an
     * exception if the path does not refer to an element
     *
     * @throws Exception if an error occurs
     */
    public void testSelectElementNodeNoElement() throws Exception {

        // xpath to an attribute
        ODOMXPath xpath = new ODOMXPath("cd[1]/@country");

        try {
            // select the xpath. We expect an element
            xpath.selectSingleElement(root);
            fail("Expected an excpetion as xpath is to an attribute");
        } catch (XPathException e) {
            // expected
        }
    }


    /**
     * Test the removal of an Element node.
     */
    public void testRemoveElement() throws Exception {
        ODOMXPath path = new ODOMXPath("cd");

        ODOMObservable removed = path.remove((ODOMObservable) root);

        assertNotNull("Removed element should exist", removed);
        assertTrue("Should be an Element", removed instanceof Element);
        Element element = (Element) removed;
        assertEquals("Name should match", "cd", element.getName());
        assertEquals("Country should match", "USA",
                     element.getAttributeValue("country"));
        Element title = element.getChild("title");
        assertEquals("Title should match", "Empire Burlesque",
                     ((Text) title.getContent().get(0)).getText());
    }


    /**
     * Test the removal of a Attribute node.
     */
    public void testRemoveAttribute() throws Exception {
        ODOMXPath path = new ODOMXPath("cd/@country");

        ODOMObservable removed = path.remove((ODOMObservable) root);

        assertNotNull("Removed element should exist", removed);
        assertTrue("Should be an Attribute", removed instanceof Attribute);
        Attribute attribute = (Attribute) removed;
        assertEquals("Name should match", "country", attribute.getName());
        assertEquals("Country should match", "USA", attribute.getValue());
    }


    /**
     * Test the removal of a Text node.
     */
    public void testRemoveText() throws Exception {
        ODOMXPath path = new ODOMXPath("cd/title/text()");

        ODOMObservable removed = path.remove((ODOMObservable) root);

        assertNotNull("Removed element should exist", removed);
        assertTrue("Should be a Text node", removed instanceof Text);
        Text element = (Text) removed;
        assertEquals("Value should match", "Empire Burlesque",
                     element.getText());
    }


    /**
     * Test the removal of a node that doesn't exist.
     */
    public void testRemoveNodeUnknown() throws Exception {
        ODOMXPath path = new ODOMXPath("cd/@nocountry");

        ODOMObservable removed = path.remove((ODOMObservable) root);
        assertNull("Removed element should not exist", removed);
    }


    /**
     * Test the removal of a node with an invalid context.
     */
    public void testRemoveInvalidContext() throws Exception {
        ODOMXPath path = new ODOMXPath("title");

        ODOMObservable removed = path.remove((ODOMObservable) root);
        assertNull("Removed element should not exist", removed);
    }


    /**
     * Test the removal of a node which results in parent nodes being removed.
     */
    public void testRemoveParentNodesParentHasContent() throws Exception {
        ODOMXPath path = new ODOMXPath("cd/title/@testAttribute");

        ODOMObservable removed = path.remove((ODOMObservable) root);
        assertNotNull("Removed element should exist", removed);
        assertTrue("Should be an Attribute: " + removed,
                   removed instanceof Attribute);
        Attribute attribute = (Attribute) removed;
        assertEquals("Name should match", "testAttribute",
                     attribute.getName());
        assertEquals("Value should match", "remove", attribute.getValue());
    }


    /**
     * Test the removal of a node which results in parent nodes being removed.
     */
    public void testRemoveParentNodes() throws Exception {
        ODOMXPath path = new ODOMXPath("cd/price/@testRemove");

        ODOMObservable removed = path.remove((ODOMObservable) root);
        assertNotNull("Removed element should exist", removed);
        assertTrue("Should be an Element: " + removed,
                   removed instanceof Element);
        Element element = (Element) removed;
        assertEquals("Name should match", "price", element.getName());
    }


    /**
     * Test the removal of node and its parent nodes.
     */
    public void testRemoveGrandparentNodes() throws Exception {
        ODOMElement one = (ODOMElement) factory.element("one");
        ODOMElement two = (ODOMElement) factory.element("two");
        ODOMElement three = (ODOMElement) factory.element("three");
        ODOMAttribute threeAttrib =
                (ODOMAttribute) factory.attribute("threeAttrib", "");

        one.addContent(two);
        two.addContent(three);
        three.setAttribute(threeAttrib);

        ODOMXPath oneToThree = new ODOMXPath("two/three");
        ODOMXPath oneToThreeAttrib = new ODOMXPath(oneToThree,
                                           "@" + threeAttrib.getName());

        // Do the removal.
        ODOMObservable observable = oneToThreeAttrib.remove(one);

        assertNotNull("Node shouldn't be null", observable);
        assertTrue("Type should match", observable instanceof ODOMElement);
        ODOMElement element = (ODOMElement) observable;
        assertEquals("Name should match", "two", element.getName());
    }


    /**
     * Test removal of a grandchild that is the only child of an only child.
     */
    public void testRemoveMultiStepGrandchildElement() throws Exception {
        ODOMElement root = (ODOMElement) factory.element("root");
        ODOMElement child = (ODOMElement) factory.element("child");
        ODOMElement grandchild = (ODOMElement) factory.element("grandchild");
        ODOMXPath path = new ODOMXPath("child/grandchild");

        root.addContent(child.addContent(grandchild));

        ODOMObservable deleted = path.remove(root);

        assertNotNull("The deleted grandchild should have been found",
                      deleted);

        assertTrue("The returned node should have been an element",
                   deleted instanceof Element);

        assertEquals("The returned node not named as",
                     "child",
                     ((ODOMElement) deleted).getName());
    }


    /**
     * Test removal of a grandchild that is the only child of an only child.
     */
    public void testRemoveMultiStepGrandchildAttribute() throws Exception {
        ODOMElement root = (ODOMElement) factory.element("root");
        ODOMElement child = (ODOMElement) factory.element("child");
        ODOMAttribute grandchild = (ODOMAttribute) factory.attribute(
                "grandchild", "value");
        ODOMXPath path = new ODOMXPath("child/@grandchild");

        root.addContent(child);
        child.setAttribute(grandchild);

        ODOMObservable deleted = path.remove(root);

        assertNotNull("The deleted grandchild should have been found",
                      deleted);

        assertTrue("The returned node should have been an element",
                   deleted instanceof Element);

        assertEquals("The returned node not named as",
                     "child",
                     ((ODOMElement) deleted).getName());
    }


    /**
     * Test removal of a grandchild that is the only child of an only child.
     */
    public void testRemoveMultiStepGrandchildText() throws Exception {
        ODOMElement root = (ODOMElement) factory.element("root");
        ODOMElement child = (ODOMElement) factory.element("child");
        ODOMText grandchild = (ODOMText) factory.text("grandchild");
        ODOMXPath path = new ODOMXPath("child/text()");

        root.addContent(child.addContent(grandchild));

        ODOMObservable deleted = path.remove(root);

        assertNotNull("The deleted grandchild should have been found",
                      deleted);

        assertTrue("The returned node should have been an element",
                   deleted instanceof Element);

        assertEquals("The returned node not named as",
                     "child",
                     ((ODOMElement) deleted).getName());
    }


    /**
     * Show the XML document.
     */
    protected void showDocument() throws IOException {
        showDocument(doc.getRootElement());
    }


    protected void showDocument(Element root) throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setTextTrim(true);
        outputter.setNewlines(true);
        StringWriter writer = new StringWriter();
        outputter.output(root, writer);
        System.out.println("-- START --");
        System.out.println(writer.toString());
        System.out.println("-- END --");

    }


    /**
     * Show all XPaths for the given root node.
     *
     * Used for debugging only.
     *
     * Sample output:
     * /catalog/cd[1]/@country-->USA
     * /catalog/cd[1]/title/text()-->Empire Burlesque
     * /catalog/cd[1]/title
     * /catalog/cd[1]/artist/text()-->Bob Dylan
     * /catalog/cd[1]/artist
     * /catalog/cd[1]/price/text()-->10.90
     * /catalog/cd[1]/price
     */
    public void notestShowAllXPaths() throws Exception {
        depthFirstTraversal((ODOMObservable) root);
    }


    /**
     * Helper method.
     */
    private void depthFirstTraversal(ODOMObservable node) {
        final Filter filter = new Filter() {
            public boolean matches(Object o) {
                return o instanceof Text;
            }
        };
        if (node != null) {
            List children = null;
            if (node instanceof Element) {
                Element element = (Element) node;
                List attList = element.getAttributes();
                for (int i = 0; attList != null && i < attList.size(); i++) {
                    Attribute attribute = (Attribute) attList.get(i);
                    System.out.println(new ODOMXPath(attribute).getExternalForm() +
                                       "-->" + attribute.getValue());
                }
                children = element.getChildren();
                for (int i = 0; (children != null) && (i < children.size()); i++) {
                    depthFirstTraversal((ODOMObservable) children.get(i));
                }
                List list = ((Element) node).getContent(filter);
                if ((list != null) && (list.size() > 0)) {
                    for (int i = 0; i < list.size(); i++) {
                        Text text = (Text) list.get(i);
                        if (text.getText().trim().length() > 0) {
                            System.out.println(new ODOMXPath(text).getExternalForm() +
                                               "-->" + text.getText());
                        }
                    }
                }
            }
            System.out.println(new ODOMXPath(node).getExternalForm());
        }
    }


    /**
     * Test the addition of namespaces to the URL map.
     */
    public void testAddNamespaceToMap() throws Exception {

        MockXPath xpath = new MockXPath();
        try {
            Class[] types = {Namespace.class};
            Object[] params = {null};

            // Test with a null namespace parameter
            PrivateAccessor.invoke(xpath, "addNamespaceToMap", types, params);
            Map map = getNamespacePrivateField(xpath);
            assertEquals("Map size should be zero", 0, map.size());

            // Test with a valid namespace (should add value to map).
            Namespace namespace = NAMESPACES[0];
            params = new Object[]{namespace};
            PrivateAccessor.invoke(xpath, "addNamespaceToMap", types, params);
            map = getNamespacePrivateField(xpath);
            assertEquals("Map size should match", 1, map.size());
            assertTrue("Map value contain key",
                       map.containsKey(namespace.getPrefix()));
            assertTrue("Map value contain value",
                       map.containsValue(namespace.getURI()));

            // Attempt to add the same namespace should simply be ignored.
            namespace = NAMESPACES[0];
            params = new Object[]{namespace};
            PrivateAccessor.invoke(xpath, "addNamespaceToMap", types, params);
            map = getNamespacePrivateField(xpath);
            assertEquals("Map size should match", 1, map.size());
            assertTrue("Map value contain key",
                       map.containsKey(namespace.getPrefix()));
            assertTrue("Map value contain value",
                       map.containsValue(namespace.getURI()));

            // Attempt to add a namespace with a different URI.
            namespace =
            Namespace.getNamespace("ns1",
                                   "http://www.w3.org/2001/XMLSchema_Different");
            params = new Object[]{namespace};
            assertFalse("Error should not have been logged",
                        xpath.errorLogged);

            PrivateAccessor.invoke(xpath, "addNamespaceToMap", types, params);
            map = getNamespacePrivateField(xpath);
            assertEquals("Map size should match", 1, map.size());
            assertTrue("Error should've been logged", xpath.errorLogged);
            assertTrue("Map value contain key",
                       map.containsKey(namespace.getPrefix()));
            assertFalse("Map value NOT contain value",
                        map.containsValue(namespace.getURI()));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            fail(throwable.getMessage());
        }
    }

    /**
     * Test the equals method for reflexive, symmetric, transitive and null
     * comparisons (amongst others).
     */
    public void testExternalFormsEqual() throws Exception {
        ODOMXPath xpath1 = new ODOMXPath(".");
        ODOMXPath xpath2 = new ODOMXPath(".");
        ODOMXPath xpath3 = new ODOMXPath(".");
        ODOMXPath xpath4 = new ODOMXPath("catalog");

        // Reflexive.
        assertTrue(xpath1.externalFormsEqual(xpath1));

        // Symmetric
        assertTrue(xpath1.externalFormsEqual(xpath2));
        assertTrue(xpath2.externalFormsEqual(xpath1));

        // Transitive
        assertTrue(xpath1.externalFormsEqual(xpath2));
        assertTrue(xpath2.externalFormsEqual(xpath3));
        assertTrue(xpath1.externalFormsEqual(xpath3));

        // Null value.
        assertFalse(xpath1.externalFormsEqual(null));

        // Not equals
        assertFalse(xpath1.externalFormsEqual(xpath4));
        assertFalse(xpath4.externalFormsEqual(xpath1));
        assertFalse(xpath2.externalFormsEqual(xpath4));
    }


    /**
     * Test the {@link ODOMXPath#selectNodes} (ODOMObservable) method
     */
    public void testODOMObservableSelectNodes() throws Exception {
        doTestSelectNodes(true);
    }

   /**
     * Test the selection of nodes with namespaces.
     * @param isODOMObservable used to indicate whether the selectNodes that
    * takes an ODOMObservable as the context or the one that takes an Object
    * context should be tested
    */
    public void doTestSelectNodes(boolean isODOMObservable) throws Exception {
        String prefix = "test";
        String uri = "http://www.volantis.com/testnamespace";
        String ns = prefix + ":";
        String xml =
                "<root xmlns:" +
                prefix +
                "=\"" +
                uri +
                "\" " +
                "id=\"root\">" +
                "<" +
                ns +
                "company>" +
                "<" +
                ns +
                "staff>" +
                "<" +
                ns +
                "director directorName=\"bert\">" +
                "</" +
                ns +
                "director>" +
                "</" +
                ns +
                "staff>" +
                "</" +
                ns +
                "company>" +
                "<" +
                ns +
                "company companyName=\"eds\">" +
                "</" +
                ns +
                "company>" +
                "</root>";
        Document doc = createDocument(xml);

        Namespace namespaces[] = {
            Namespace.getNamespace(prefix, uri)
        };

        final ODOMXPath xpath = new ODOMXPath(ns + "staff/" + ns + "director",
                                      namespaces);
        final Element child = doc.getRootElement().getChild("company",
                                                            namespaces[0]);

        List elements;
        if (isODOMObservable) {
            elements = xpath.selectNodes(child);
        } else {
            elements = xpath.selectNodes(child);
        }
        assertEquals("Number of elements should match", 1, elements.size());
        assertEquals("Name should match", "bert",
                     ((Element) elements.get(0)).getAttributeValue(
                             "directorName"));
    }

    /**
     * Create a JDOM document from the input xml string.
     *
     * @param xml the input xml string.
     * @return the newly created JDOM document.
     */
    private Document createDocument(String xml) {
        SAXBuilder builder = new SAXBuilder();
        builder.setFactory(factory);

        Document document = null;
        try {
            document = builder.build(new StringReader(xml));
        } catch (JDOMException e) {
            e.printStackTrace();
            fail("Could not create document with inputL: " + xml);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not create document with input: " + xml);
        }
        return document;
    }


    /**
     * Tests the {@link ODOMXPath#getParentStr} method
     *
     * @throws Exception if an error occurs
     */
    public void testGetParentStr() throws Exception {
        ODOMXPath path = new ODOMXPath("/lpdm:a/lpdm:b/@c",
                               new Namespace[]{MCSNamespace.LPDM});

        assertEquals("parent not as", "/lpdm:a/lpdm:b", path.getParentStr());
    }


    /**
     * Tests the {@link ODOMXPath#getParentStr} method when the path does not
     * have a parent
     *
     * @throws Exception if an error occurs
     */
    public void testGetParentStrWhenNoParent() throws Exception {
        ODOMXPath path = new ODOMXPath("lpdm:b",
                               new Namespace[]{MCSNamespace.LPDM});

        assertEquals("parent not as ", null, path.getParentStr());
    }


    /**
     * Tests the {@link ODOMXPath#getParentStr} method when the path does not
     * have a parent and the path is absolute
     *
     * @throws Exception if an error occurs
     */
    public void testGetParentStrWhenNoParentAndAbsolutePath()
            throws Exception {
        ODOMXPath path = new ODOMXPath("/lpdm:a",
                               new Namespace[]{MCSNamespace.LPDM});

        assertEquals("parent not as ", null, path.getParentStr());
    }


    /**
     * Tests the {@link ODOMXPath#getParent} method
     *
     * @throws Exception if an error occurs
     */
    public void testGetParent() throws Exception {
        XPath path = new XPath("/lpdm:a/lpdm:b",
                               new Namespace[]{MCSNamespace.LPDM});
        XPath parent =  path.getParent();
        assertEquals("parent not as ",
                     "/lpdm:a",
                     parent.getExternalForm());

        Map namespaces = getNamespacePrivateField(parent);
        assertEquals("namespace not as ",
                     MCSNamespace.LPDM.getURI(),
                     (String) namespaces.get(
                             MCSNamespace.LPDM.getPrefix()));
    }


    /**
     * Tests the {@link ODOMXPath#getParent} method when the path does not have
     * a parent
     *
     * @throws Exception if an error occurs
     */
    public void testGetParentWhenNoParent() throws Exception {
        ODOMXPath path = new ODOMXPath("lpdm:b",
                               new Namespace[]{MCSNamespace.LPDM});

        assertEquals("parent not as ", null, path.getParent());
    }


    public void testSelectAttribute() throws Exception {
        final ODOMElement fourthCdElement = ((ODOMElement) (root.getChildren()
                .get(3)));
        ODOMAttribute attribute = (ODOMAttribute) fourthCdElement.getChild(
                "title")
                .getAttribute("testAttribute");
        assertEquals("sanity check", "remove", attribute.getValue());

        assertSelectFromODOMObservable(attribute);
    }


    public void testSelectAttributeWithinElemWithNS() throws Exception {
        final ODOMElement newElement = ((ODOMElement) (root.getChildren()
                .get(4)));
        ODOMAttribute attribute = (ODOMAttribute) newElement.getAttribute(
                "country");
        assertEquals("sanity check", "Nowhere", attribute.getValue());

        assertSelectFromODOMObservable(attribute);
    }


    public void testSelectAttributeWithNSinPath() throws Exception {
        final ODOMElement newElement2 = ((ODOMElement) (root.getChildren()
                .get(5)));
        ODOMAttribute attribute = (ODOMAttribute) newElement2.getChild(
                "newElement2_2").getAttribute("attr2_2");
        assertEquals("sanity check", "attr2_2Value", attribute.getValue());

        assertSelectFromODOMObservable(attribute);
    }

    public void testSelectTextNodeWithNSinPath() throws Exception {
        final ODOMElement newElement2 = ((ODOMElement) (root.getChildren()
                .get(5)));
        ODOMText odomText = (ODOMText) newElement2.getChild("newElement2_2").getContent().get(0);

        assertEquals("sanity check", "newElement2_2_text", odomText.getTextTrim());

        assertSelectFromODOMObservable(odomText);
    }


    public void testSelectElementWithNS() throws Exception {
        final ODOMElement newElement2 = ((ODOMElement) (root.getChildren()
                .get(5)));
        assertEquals("sanity check", "newElement2", newElement2.getName());

        assertSelectFromODOMObservable(newElement2);
    }

    public void testSelectElementWithNSinPath() throws Exception {
        final ODOMElement newElement2_2 = (ODOMElement) ((ODOMElement) (root.getChildren()
                .get(5))).getChild("newElement2_2");
        assertNotNull("sanity check", newElement2_2);

        assertSelectFromODOMObservable(newElement2_2);
    }


    private void assertSelectFromODOMObservable(ODOMObservable odomObservable)
            throws XPathException {
        ODOMXPath xpath = new ODOMXPath(odomObservable);
        assertEquals("selected odomObservable", odomObservable,
                     xpath.selectSingleNode(doc));
    }


    /**
     * Mock ODOMXPath object that:
     * 1. Prevents calls to logError in EclipseCommonPlugin (which fails due to
     * unitialized data) by overriding the logError method.
     * 2. Flags when a error is logged (so that test cases may verify that
     * logging the message has been logged).
     */
    class MockXPath extends ODOMXPath {
        boolean errorLogged = false;


        MockXPath() {
            super(".");
        }


        // javadoc inherited.
        protected void logError(String msg) {
            errorLogged = true;
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/4	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/2	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-Jun-04	4691/3	allan	VBM:2004060202 Allow setFocus(ODOMXPath) to work with Elements

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 02-Apr-04	3717/1	doug	VBM:2004022404 Fixed ClassCastException problem

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 17-Feb-04	2988/1	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 23-Jan-04	2682/1	doug	VBM:2003112506 Added StylePropertiesSection to eclipse gui

 09-Jan-04	2506/1	philws	VBM:2004010810 Fix ODOMXPath marker persistence

 07-Jan-04	2440/1	doug	VBM:2004010704 Fixed getParentStr() so that it handles absolute paths correctly

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 15-Dec-03	2160/6	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 12-Dec-03	2123/4	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 10-Dec-03	2057/11	doug	VBM:2003112803 fixed supermerge problems

 10-Dec-03	2057/8	doug	VBM:2003112803 Addressed several rework issues

 09-Dec-03	2057/6	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 10-Dec-03	1968/2	richardc	VBM:2003111502 Second semi-tested draft for code release

 28-Nov-03	2041/3	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 04-Dec-03	2105/4	philws	VBM:2003120301 Add more test cases and fix create method step ODOMXPath handling bug

 04-Dec-03	2105/2	philws	VBM:2003120301 Fix ODOMXPath's create method handling of multi-step creation and absolute paths

 26-Nov-03	2018/10	byron	VBM:2003112503 Ensure recursion in constructor never happens

 26-Nov-03	2018/8	byron	VBM:2003112503 Fixed ODOMXPath equals method and added testcases

 25-Nov-03	2018/6	byron	VBM:2003112503 ODOMXPath augmentations and recursive delete apparent bug investigation - updated testcases

 25-Nov-03	2018/4	byron	VBM:2003112503 ODOMXPath augmentations and recursive delete apparent bug investigation

 24-Nov-03	1919/8	byron	VBM:2003111504 Provide ODOM ODOMXPath facilities - handled creation where xpaths select > 1 node.

 21-Nov-03	1919/6	byron	VBM:2003111504 Provide ODOM ODOMXPath facilities - update with issues resolved

 20-Nov-03	1919/4	byron	VBM:2003111504 Provide ODOM ODOMXPath facilities

 20-Nov-03	1919/1	byron	VBM:2003111504 Provide ODOM ODOMXPath facilities

 ===========================================================================
*/
