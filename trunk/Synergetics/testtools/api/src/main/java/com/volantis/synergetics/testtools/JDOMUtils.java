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
package com.volantis.synergetics.testtools;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Utility class for JDOM convertions
 */
public class JDOMUtils {

    /**
     * Default JDOMFactory
     */
    private static JDOMFactory JDOM_FACTORY = new DefaultJDOMFactory();

    /**
     * Converts some XML into a document
     *
     * @param document the XML string
     * @return a Document instance
     *
     * @throws org.jdom.JDOMException if an error occurs
     * @throws java.io.IOException    if an error occurs
     */
    public static Document createDocument(String document)
        throws JDOMException, IOException {
        return createDocument(document, JDOM_FACTORY);
    }

    /**
     * Converts some XML into a document using the specified JDOMFactory
     *
     * @param document the XML string
     * @param factory  the JDOMFactory to use
     * @return a Document instance
     *
     * @throws org.jdom.JDOMException if an error occurs
     * @throws java.io.IOException    if an error occurs
     */
    public static Document createDocument(String document,
                                          JDOMFactory factory)
        throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder(false);
        builder.setFactory(factory);
        return builder.build(new StringReader(document));
    }

    /**
     * Converts some XML into a JDOM element
     *
     * @param element the XML string
     * @return an Element instance
     *
     * @throws org.jdom.JDOMException if an error occurs
     * @throws java.io.IOException    if an error occurs
     */
    public static Element createElement(String element)
        throws JDOMException, IOException {
        return createElement(element, JDOM_FACTORY);
    }

    /**
     * Converts some XML into a JDOM element using the specified JDOMFactory
     *
     * @param element the XML string
     * @param factory the JDOMFactory to use
     * @return an Element instance
     *
     * @throws org.jdom.JDOMException if an error occurs
     * @throws java.io.IOException    if an error occurs
     */
    public static Element createElement(String element, JDOMFactory factory)
        throws JDOMException, IOException {
        Element root = createDocument(element, factory).getRootElement();
        root.detach();
        return root;
    }

    /**
     * Converts a JDOM document to a string
     *
     * @param document the Document to be converted
     * @return a String representation of the Document argument
     *
     * @throws java.io.IOException if an error occurs
     */
    public static String convertToString(Document document)
        throws IOException {
        if (document == null) {
            throw new IllegalArgumentException("document cannot be null");
        }
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(document, writer);
        return writer.toString();
    }

    /**
     * Converts a JDOM element to a string
     *
     * @param element the Element to be converted
     * @return a String representation of the Document argument
     *
     * @throws java.io.IOException if an error occurs
     */
    public static String convertToString(Element element) throws IOException {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        StringWriter writer = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(element, writer);
        return writer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	491/1	allan	VBM:2005062308 Move ArrayUtils into Synergetics and add a new toString

 04-May-04	199/1	doug	VBM:2004042906 Fixed migration problem with the device repository

 ===========================================================================
*/
