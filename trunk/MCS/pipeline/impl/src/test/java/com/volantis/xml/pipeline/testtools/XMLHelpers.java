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
package com.volantis.xml.pipeline.testtools;

import com.volantis.xml.xerces.parsers.DOMParser;
import com.volantis.xml.xml.serialize.OutputFormat;
import com.volantis.xml.xml.serialize.XMLSerializer;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessTestable;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.jdom.Document;
import org.jdom.output.DOMOutputter;
import org.jdom.input.SAXBuilder;
import org.jdom.input.SAXHandler;
import org.custommonkey.xmlunit.XMLAssert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This is a utility class for XML. By XML I mean any kind of DOM and I know
 * of three: w3c dom, jdom and volantis dom as used in the protocols and SAX.
 * @todo later - move some of this to Synergetics and merge with MCS XMLHelpers.
 */
public class XMLHelpers {

    /**
     * Render a W3C DOM Element as a String.
     * @param element The Element.
     * @return The String.
     * @throws java.io.IOException If some goes wrong.
     */
    public static String w3cDOMElement2String(Element element)
            throws IOException {
        StringWriter writer = new StringWriter();

        OutputFormat format = new OutputFormat("text", "ISO-8859-1", true);
        format.setPreserveSpace(true);

        XMLSerializer serializer = new XMLSerializer(writer, format);
        serializer.serialize(element);

        // The serializer will include the xml versioning line as the
        // first line but since that is not really part of the element
        // we remove it.
        StringBuffer buffer = writer.getBuffer();
        char c = 0;
        do {
            c = buffer.charAt(0);
            buffer.deleteCharAt(0);
        } while (c!='\n');

        // The last character is also a '\n'
        buffer.deleteCharAt(buffer.length()-1);

        return buffer.toString();
    }

    /**
     * Create a W3C DOM Element from a String representation.
     * @param string The String representation of the DOM Element
     * @return The DOM Element described by the String.
     * @throws java.io.IOException If something goes wrong.
     * @throws org.xml.sax.SAXException If something goes wrong.
     */
    public static Element stringToW3CDOMElement(String string)
            throws IOException, SAXException {
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(string)));
        return parser.getDocument().getDocumentElement();
    }

    /**
     * Test a String and a JDOM Docuement for equality. Uses XMLUnit.
     * @param expectedString The String representation of what is expected.
     * @param actual The JDOM Document that is an actual result.
     */
    public static void assertEquals(String expectedString, Document actual)
            throws Exception {
        StringReader reader = new StringReader(expectedString);                
        Document expected = new SAXBuilder().build(reader);

        DOMOutputter domOutputter = new DOMOutputter();

        XMLAssert.assertXMLEqual(domOutputter.output(expected),
                                   domOutputter.output(actual));
    }

    /**
     * Provide an XMLProcess that is a SAXHandler. A SAXHandler is used to
     * build JDOM documents from SAX events.
     * @param saxHandler The SAX handler to incorporate into the XMLProcess.
     * @return An XMLProcess that forwards events to a SAXHandler.
     */
    public static XMLProcess
            createSAXHandlerProcess(final SAXHandler saxHandler) {
        XMLProcessTestable process = new XMLProcessTestable() {
            public void startElement(String namespaceURI, String localName,
                                     String qName, Attributes attrs)
                    throws SAXException {
                saxHandler.startElement(namespaceURI, localName,
                                        qName, attrs);
            }

            public void endElement(String namespaceURI, String localName,
                                   String qName) throws SAXException {
                saxHandler.endElement(namespaceURI, localName, qName);
            }

            public void characters(char[] chars, int start, int length)
                    throws SAXException {
                saxHandler.characters(chars, start, length);
            }
        };

        return process;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 29-Jun-03	98/1	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/
