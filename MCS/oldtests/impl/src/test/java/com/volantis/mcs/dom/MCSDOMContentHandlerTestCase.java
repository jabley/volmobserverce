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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/dom/MCSDOMContentHandlerTestCase.java,v 1.2 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Geoff           VBM:2003040305 - Created; a TestCase for the 
 *                              MCSDOMContentHandler class.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

/**
 * A TestCase for the {@link com.volantis.mcs.dom.MCSDOMContentHandler} class.
 */ 
public class MCSDOMContentHandlerTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public MCSDOMContentHandlerTestCase(String s) {
        super(s);
    }

    /**
     * Test that an XML snippet containing only an empty root element may be 
     * parsed. This is the simplest successful case, and demonstrates that
     * simple {@link com.volantis.mcs.dom.Element} parsing works.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    public void testRootElementEmpty() 
            throws IOException, SAXException {
        Node node = parseXmlSnippet("<root/>");
        Element element = checkRoot(node);
        DOMAssertionUtilities.assertNoChildren(element);
        DOMAssertionUtilities.assertNoPeers(element);
    }

    /**
     * Test that an XML snippet containing a root element with some attributes 
     * may be parsed. This demonstrates that attribute parsing works.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    public void testRootElementContainingAttributes() 
            throws IOException, SAXException {
        Node node = parseXmlSnippet("<root a1='v1' a2='v2'/>");
        Element element = checkRoot(node);
        DOMAssertionUtilities.assertAttributeEquals("a1", "v1", element);
        DOMAssertionUtilities.assertAttributeEquals("a2", "v2", element);
        DOMAssertionUtilities.assertNoChildren(element);
        DOMAssertionUtilities.assertNoPeers(element);
    }

    /**
     * Test that an XML snippet containing a root element with some text may 
     * be parsed. This demonstrates that {@link com.volantis.mcs.dom.Text} parsing works.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    public void testRootElementContainingText() 
            throws IOException, SAXException {
        Node node = parseXmlSnippet("<root>text</root>");
        Element element = checkRoot(node);
        Text text = DOMAssertionUtilities.assertText("text", element.getHead());
        DOMAssertionUtilities.assertNoPeers(text);
    }

    /**
     * Test that an XML snippet containing a root element with an empty child 
     * element may be parsed. This demonstrates that simple child-parent
     * relationships for {@link com.volantis.mcs.dom.Element}s works.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    public static void testRootElementContainingEmptyElement() 
            throws IOException, SAXException {
        Node node = parseXmlSnippet("<root><element/></root>");
        Element element = checkRoot(node);
        element = DOMAssertionUtilities.assertElement("element", element.getHead());
        DOMAssertionUtilities.assertNoChildren(element);
        DOMAssertionUtilities.assertNoPeers(element);
    }

    /**
     * Test that an XML snippet that is poorly formed because it does not 
     * contain anything fails to parse.
     * 
     * @throws java.io.IOException
     */ 
    public void testPoorlyFormedEmpty() throws IOException {
        assertPoorlyFormed("");
    }
    
    /**
     * Test that an XML snippet that is poorly formed because it does not 
     * contain a root element fails to parse.
     * 
     * @throws java.io.IOException
     */ 
    public void testPoorlyFormedText() throws IOException {
        assertPoorlyFormed("text");
    }

    /**
     * Test that an XML snippet that is poorly formed because it is unbalanced 
     * (i.e does not contain the end tag to match a start tag) fails to parse.
     * 
     * @throws java.io.IOException
     */ 
    public void testPoorlyFormedElementUnbalanced() throws IOException {
        assertPoorlyFormed("<unbalanced>");
    }

    // Infrastructure
    
    /**
     * Ensure that the node provided is the "root" element and that it has no
     * children.
     * 
     * @param node which must be the "root" node
     * @return the "root" {@link com.volantis.mcs.dom.Element}.
     */ 
    private static Element checkRoot(Node node) {
        Element element = DOMAssertionUtilities.assertElement("root", node);
        assertNull(element.getNext());
        return element;
    }

    /**
     * Ensure that an XML snippet is poorly formed (i.e. fails parsing).
     * 
     * @param poorlyFormedXml a snippet of poorly formed XML
     * @throws java.io.IOException
     */ 
    private static void assertPoorlyFormed(String poorlyFormedXml)
            throws IOException {
        try {
            parseXmlSnippet(poorlyFormedXml);
            fail("poorly formed xml: " + poorlyFormedXml);
        } catch (SAXException e) {
            // Normal, keep going.
        }
    }

    /**
     * Parse a string containing an XML snippet into a tree of DOM nodes.
     * 
     * @param wellFormedXml the xml snippet.
     * @return a {@link com.volantis.mcs.dom.Node} containing a tree of DOM Nodes.
     * 
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */ 
    private static Node parseXmlSnippet(String wellFormedXml)
            throws IOException, SAXException {
        // Create a Document for the provided XML snippet.
        // This deliberately duplicates code in DOMUtilities.
        // Since we are testing MCSDOMContentHandler, we wish to create
        // and initialise it ourselves, in a "controlled" environment.
        MCSDOMContentHandler domParser = new MCSDOMContentHandler();
        XMLReader saxParser =
                new com.volantis.xml.xerces.parsers.SAXParser();
        saxParser.setContentHandler(domParser);
        StringReader stringReader = new StringReader(wellFormedXml);
        InputSource source = new InputSource(stringReader);
        saxParser.parse(source);
        Document document = domParser.getDocument();

        // Retrieve the "real" root node from the Document. 
        return document.getRootElement();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/5	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
