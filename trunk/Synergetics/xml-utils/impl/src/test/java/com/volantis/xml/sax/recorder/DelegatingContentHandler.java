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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DelegatingContentHandler
        implements ContentHandler {

    private final ContentHandler delegate;

    public DelegatingContentHandler(ContentHandler delegate) {
        this.delegate = delegate;
    }

    public void endDocument()
            throws SAXException {
        delegate.endDocument();
    }

    public void startDocument()
            throws SAXException {
        delegate.startDocument();
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {
        delegate.characters(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        delegate.ignorableWhitespace(ch, start, length);
    }

    public void endPrefixMapping(String prefix)
            throws SAXException {
        delegate.endPrefixMapping(prefix);
    }

    public void skippedEntity(String name)
            throws SAXException {
        delegate.skippedEntity(name);
    }

    public void setDocumentLocator(Locator locator) {
        delegate.setDocumentLocator(locator);
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        delegate.processingInstruction(target, data);
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        delegate.startPrefixMapping(prefix, uri);
    }

    public void endElement(
            String namespaceURI, String localName,
            String qName)
            throws SAXException {
        delegate.endElement(namespaceURI, localName, qName);
    }

    public void startElement(
            String namespaceURI, String localName,
            String qName, Attributes atts)
            throws SAXException {
        delegate.startElement(namespaceURI, localName, qName, atts);
    }
}
