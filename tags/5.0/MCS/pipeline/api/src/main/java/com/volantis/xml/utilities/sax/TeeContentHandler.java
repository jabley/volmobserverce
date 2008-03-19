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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.utilities.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;

/**
 * A {@link ContentHandler} implementation that Tee's the SAX events to two
 * ContentHandlers.
 */
public class TeeContentHandler implements ContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2005. ";

    /**
     * The first ContentHandler that events will be forwarded to
     */
    private final ContentHandler firstContentHandler;

    /**
     * The second ContentHandler that events will be forwarded to
     */
    private final ContentHandler secondContentHandler;

    /**
     * Initializes a new <code>TeeContentHandler</code> with the two
     * {@link ContentHandler} implementations that the events will be
     * teed to.
     * @param firstContentHandler the first <code>ContentHandler</code>
     * @param secondContentHandler the second <code>ContentHandler</code>
     */
    public TeeContentHandler(ContentHandler firstContentHandler, 
                             ContentHandler secondContentHandler) {
        if (firstContentHandler == null) {
            throw new IllegalArgumentException(
                        "First ContentHandler cannot be null");
        }
        if (secondContentHandler == null) {
            throw new IllegalArgumentException(
                        "Second ContentHandler cannot be null");
        }
        this.firstContentHandler = firstContentHandler;
        this.secondContentHandler = secondContentHandler;
    }

    // javadoc inherited
    public void characters (char ch[], int start, int length)
	throws SAXException {
        firstContentHandler.characters(ch, start, length);
        secondContentHandler.characters(ch, start, length);
    }

    // javadoc inherited
    public void endDocument()
	throws SAXException {
        firstContentHandler.endDocument();
        secondContentHandler.endDocument();
    }

    // javadoc inherited
    public void endElement (String namespaceURI, String localName,
			    String qName)
	throws SAXException {
        firstContentHandler.endElement(namespaceURI, localName, qName);
        secondContentHandler.endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void endPrefixMapping (String prefix)
	throws SAXException {
        firstContentHandler.endPrefixMapping(prefix);
        secondContentHandler.endPrefixMapping(prefix);
    }

    // javadoc inherited
    public void ignorableWhitespace (char ch[], int start, int length)
	throws SAXException {
        firstContentHandler.ignorableWhitespace(ch, start, length);
        secondContentHandler.ignorableWhitespace(ch, start, length);
    }

    // javadoc inherited
    public void processingInstruction (String target, String data)
	throws SAXException {
        firstContentHandler.processingInstruction(target, data);
        secondContentHandler.processingInstruction(target, data);
    }

    // javadoc inherited
    public void setDocumentLocator (Locator locator) {
        firstContentHandler.setDocumentLocator(locator);
        secondContentHandler.setDocumentLocator(locator);
    }

    // javadoc inherited
    public void skippedEntity (String name)
	throws SAXException {
        firstContentHandler.skippedEntity(name);
        secondContentHandler.skippedEntity(name);
    }

    // javadoc inherited
    public void startDocument ()
	throws SAXException {
        firstContentHandler.startDocument();
        secondContentHandler.startDocument();
    }

    // javadoc inherited
    public void startElement (String namespaceURI, String localName,
			      String qName, Attributes atts)
	throws SAXException {
        firstContentHandler.startElement(namespaceURI, localName, qName, atts);
        secondContentHandler.startElement(namespaceURI, localName, qName, atts);
    }

    // javadoc inherited
    public void startPrefixMapping (String prefix, String uri)
	throws SAXException {
        firstContentHandler.startPrefixMapping(prefix, uri);
        secondContentHandler.startPrefixMapping(prefix, uri);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7762/4	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 ===========================================================================
*/
