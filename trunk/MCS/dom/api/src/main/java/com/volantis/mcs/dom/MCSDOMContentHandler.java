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
 * $Header: /src/voyager/com/volantis/mcs/dom/MCSDOMContentHandler.java,v 1.2 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * 17-Apr-03    Geoff           VBM:2003040305 - Moved from testsuite version
 *                              of package to normal version of package, as it
 *                              is now used by Protocols for parsing WMLTasks.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * A simple content handler providing SAX 2.x handling of XML used to 
 * construct a Mariner DOM object hierarchy. 
 * <p>
 * "Complex" things like processing instructions, namespaces, etc are 
 * currently ignored, so this is only really useful for parsing "snippets" of
 * XML. 
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 * 
 * @todo may need to get this parser to use the DOM pool(s).
 */
public class MCSDOMContentHandler extends DefaultHandler {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The Mariner DOM Document being generated.
     */
    protected Document document = null;

    /**
     * The Node hierarchy currently being processed during input.
     *
     * @associates Node 
     */
    protected Stack currentNodes = new Stack();

    /**
     * A short-cut to the node at the top of the input stack.
     */
    protected Node currentNode = null;

    /**
     * Returns the document that has just been generated.
     *
     * @return the Mariner DOM Document created from a SAX parse of XML
     */
    public Document getDocument() {
        return document;
    }

    // SAX event handler
    public void startDocument() {
        document = domFactory.createDocument();
    }

    // SAX event handler
    public void endDocument() throws SAXException {
        // The stack should be empty if the document was well-formed
        // Note I think a SAX2 parser will find this by default anyway, 
        // certainly Xerces seems to...
        if (!currentNodes.empty()) {
            throw new ExtendedSAXException("handler stack not empty");
        }
    }

    // SAX event handler
    public void characters(char[] chars, int start, int length) {
        Text text;

        // Either create a new text node or add the given text to the
        // current one
        if ((currentNode == null) ||
            !(currentNode instanceof Text)) {
            text = domFactory.createText();

            if (currentNode != null) {
                text.addToTail((Element)currentNode);
            } else {
                document.addNode(text);
            }

            currentNode = text;
            currentNodes.push(text);
        } else {
            text = (Text)currentNode;
        }

        text.append(chars, start, length);
    }

    // SAX event handler
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) {
        String name = localName;
        Element element;

        if (name == null) {
            name = qName;
        }

        // Text nodes can't have hierarchy within them
        if ((currentNode != null) &&
                (currentNode instanceof Text)) {
            currentNodes.pop();

            if (currentNodes.isEmpty()) {
                currentNode = null;
            } else {
                currentNode = (Node) currentNodes.peek();
            }
        }

        // Create a new node for the element being started and add it
        // into the hierarchy at the right point
        element = domFactory.createElement(name);

        if (currentNode != null) {
            element.addToTail((Element) currentNode);
        } else {
            document.addNode(element);
        }

        currentNodes.push(element);
        currentNode = element;

        // Add the attributes to the element
        for (int i = 0, attrLength = attributes.getLength();
             i < attrLength;
             i++) {
            name = attributes.getLocalName(i);

            if (name == null) {
                name = attributes.getQName(i);
            }

            element.setAttribute(name, attributes.getValue(i));
        }
    }

    // SAX event handler
    public void endElement(String uri, String localName, String qName) {
        if (currentNode instanceof Text) {
            currentNodes.pop();
        }

        if (!currentNodes.isEmpty()) {
            currentNodes.pop();
        }

        // Re-acquire the node at the top of the input stack, if any
        if (currentNodes.isEmpty()) {
            currentNode = null;
        } else {
            currentNode = (Node)currentNodes.peek();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
