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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/dom/DOMUtilities.java,v 1.5 2003/04/25 17:38:58 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * 15-Jan-03    Chris W         VBM:2002111508 - Added toString method that
 *                              takes an Element rather than a Document
 * 22-Apr-03    Geoff           VBM:2003040305 - Add assertion methods to test 
 *                              DOM nodes.
 * 23-Apr-03    Allan           VBM:2003042302 - Added 
 *                              provideDOMNormalizedString(). Tidied up some 
 *                              exception throwing and optimized imports. 
 * 23-May-03    Mat             VBM:2003042907 - Changed to create XMLOutputter
 *                              in line with the new way it works.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MCSDOMContentHandler;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.dom.output.AbstractCharacterEncoder;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.AnnotatingXMLDocumentWriter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Provides supporting utilities for reading XML files, strings and input
 * streams to construct Mariner DOM object hierarchies and for outputting
 * Mariner DOM object hierarchies to strings.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class DOMUtilities {

    private static final String defaultParser =
        "org.apache.xerces.parsers.SAXParser";

    /**
     * A trivial character encoder which is suitable for use in test cases
     * for encoding characters during DOM output.
     */
    private static final AbstractCharacterEncoder encoder =
            new DebugCharacterEncoder();

    /**
     * Given a String that represents a DOM, put it through a XMLReader and
     * subsequently the DOMUtilities.toString() method to provide a DOM
     * normalized version of the original String.
     *
     * @param domString The String representing a DOM.
     * @param encoder an object providing character encoding support
     * @return The DOM normalized version of the domString.
     * @throws SAXException if there is a problem parsing the domString
     * @throws java.io.IOException if there is a problem reading the domString
     */
    public static String provideDOMNormalizedString(String domString,
                                                    CharacterEncoder encoder)
            throws SAXException, IOException {


        XMLReader reader = DOMUtilities.getReader();
        String normalized;
        Document document = DOMUtilities.read(reader, domString);
        normalized = DOMUtilities.toString(document, encoder);

        return normalized;
    }

    /**
     * Given a String that represents a DOM, put it through a XMLReader and
     * subsequently the DOMUtilities.toString() method to provide a DOM
     * normalized version of the original String that is not reliant on any
     * specific protocol.
     *
     * @param domString The String representing a DOM.
     * @return The DOM normalized version of the domString.
     * @throws SAXException if there is a problem parsing the domString
     * @throws java.io.IOException if there is a problem reading the domString
     */
    public static String provideDOMNormalizedString(String domString)
            throws SAXException, IOException {

        if (domString.length() == 0) {
            return domString;
        }
        
        XMLReader reader = DOMUtilities.getReader();
        String normalized;
        Document document;
        try {
            document = DOMUtilities.read(reader, domString);
        } catch (SAXException e) {
            throw new ExtendedSAXException("Unable to parse: " + domString, e);
        }
        normalized = DOMUtilities.toString(document);

        return normalized;
    }

    /**
     * Return an XMLReader with content handler and error handler initialized
     * to support the Mariner DOM object hierarchy. By default this will
     * use the Xerces parser unless the XMLReaderFactory identification has
     * been set as a system property.
     *
     * @throws SAXException a SAXException or a parser-specific exception
     */
    public static XMLReader getReader() throws SAXException {
        XMLReader reader;
        MCSDOMContentHandler handler = new MCSDOMContentHandler();

        try {
            reader = XMLReaderFactory.createXMLReader();
        } catch (Exception e) {
            reader = XMLReaderFactory.createXMLReader(defaultParser);
        }

        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);

        return reader;
    }

    /**
     * Returns the Mariner DOM object hierarchy equivalent to the given XML
     * string.
     *
     * @param xmlString a string containing XML
     * @return a Mariner DOM Document
     * @throws SAXException
     * @throws java.io.IOException
     */
    public static Document read(String xmlString)
            throws SAXException, IOException {
        InputSource source = getInputSource(xmlString);

        XMLReader reader = getReader();

        reader.parse(source);

        return ((MCSDOMContentHandler) reader.getContentHandler()).
                getDocument();
    }

    /**
     * Get an input source that wraps the contained string.
     *
     * @param xmlString a string containing XML
     * @return an InputSource
     */
    private static InputSource getInputSource(String xmlString) {
        StringReader stringReader = new StringReader(xmlString);
        return new InputSource(stringReader);
    }

    /**
     * Given an XMLReader initialized by DOMUtilities.getReader, this
     * method returns the Mariner DOM object hierarchy equivalent to
     * the given XML string.
     *
     * @param reader    an XMLReader instance returned by getReader
     * @param xmlString a string containing XML
     * @return a Mariner DOM Document
     * @throws SAXException
     * @throws java.io.IOException
     */
    public static Document read(XMLReader reader,
                                String xmlString)
            throws SAXException, IOException {
        InputSource source = getInputSource(xmlString);

        reader.parse(source);

        return ((MCSDOMContentHandler) reader.getContentHandler()).
                getDocument();
    }

    /**
     * Given an XMLReader initialized by DOMUtilities.getReader, this
     * method returns the Mariner DOM object hierarchy equivalent to
     * the content of the given input stream.
     *
     * @param reader an XMLReader instance returned by getReader
     * @param stream a stream supplying XML
     * @return a Mariner DOM Document
     * @throws java.lang.Exception a SAXException or IOException.
     */
    public static Document read(XMLReader reader,
                                InputStream stream) throws Exception {
        InputStreamReader streamReader = new InputStreamReader(stream);
        InputSource source = new InputSource(streamReader);

        reader.parse(source);

        return ((MCSDOMContentHandler) reader.getContentHandler()).
                getDocument();
    }

    /**
     * Reads a node sequence.
     *
     * @param xmlString a string containing an XML Document Fragment.
     * @return a Mariner DOM Document
     * @throws SAXException
     * @throws java.io.IOException
     */
    public static NodeSequence readSequence(String xmlString)
            throws SAXException, IOException {
        InputSource source = getInputSource("<root>" + xmlString + "</root>");

        XMLReader reader = getReader();

        reader.parse(source);

        Document document = ((MCSDOMContentHandler) reader.getContentHandler()).
                getDocument();
        Element root = document.getRootElement();
        if (!"root".equals(root.getName())) {
            throw new IllegalStateException("Root element name mismatch, " +
                    "expected 'root', actual '" + root.getName() + "'");
        }

        return root.removeChildren();
    }

    /**
     * Given a Mariner DOM object hierarchy, this method creates and returns
     * an XML string equivalent to the hierarchy content.
     *
     * @param dom a Document instance
     * @return a String representing the dom object hierarchy content
     * @throws java.io.IOException
     */
    public static String toString(Document dom)
            throws IOException {

        return toString(dom, encoder);
    }

    /**
     * Given a Mariner DOM object hierarchy and a character encoder, this
     * method creates and returns an XML string equivalent to the hierarchy
     * content.
     *
     * @param dom     a Document instance
     * @param encoder an object providing character encoding support
     * @return a String representing the dom object hierarchy content
     * @throws java.io.IOException
     */
    public static String toString(Document dom, CharacterEncoder encoder)
            throws IOException {

        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                new XMLDocumentWriter(writer), encoder);

        outputter.output(dom);

        return writer.toString();
    }

    /**
     * Given a Mariner DOM object hierarchy and a protocol object, this method
     * creates and returns an XML string equivalent to the hierarchy content.
     * <p>
     * This overload is useful for those tests which operate without a
     * Protocol lying around.
     *
     * @param element The Element whose hierarchy we wish to see.
     * @return a String representing the dom object hierarchy content
     */
    public static String toString(Element element) {

        // Construct a dummy encoder that passes everything straight through.
        return toString(element, encoder);

    }

    /**
     * Given a Mariner DOM object hierarchy and a protocol object, this method
     * creates and returns an XML string equivalent to the hierarchy content.
     * <p>
     * This overload provides service to the other two, as not many tests will
     * have a CharacterEncoder instance lying around to provide here.
     *
     * @param element The Element whose hierarchy we wish to see.
     * @param encoder The character encoder to use.
     * @return a String representing the dom object hierarchy content
     */
    public static String toString(Element element, CharacterEncoder encoder) {

        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                new AnnotatingXMLDocumentWriter(writer), encoder);

        try {
            outputter.output(element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return writer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 12-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 21-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/4	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 20-Apr-04	3715/1	claire	VBM:2004040201 Improving WML menu item renderers

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 23-Oct-03	1620/1	steve	VBM:2003102006 Update Test case for regions
 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
