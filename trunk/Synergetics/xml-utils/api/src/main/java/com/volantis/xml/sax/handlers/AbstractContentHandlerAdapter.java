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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.sax.handlers;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * An implementation of ContentHandler that does nothing. This class is
 * not instantiable and is meant to be extended to provide specific
 * implementations of ContentHandler interface methods as required.
 */
public abstract class AbstractContentHandlerAdapter implements ContentHandler {
    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
    }

    // javadoc inherited
    public void startDocument() throws SAXException {
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    // javadoc inherited
    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
    }

    // javadoc inherited
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    }

    // javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
    }

    // javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
    }

    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
    }

    // javadoc inherited
    public void skippedEntity(String name) throws SAXException {
    }
}
