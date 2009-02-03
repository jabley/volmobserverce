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
package com.volantis.xml.pipeline.testtools;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;

/**
 * A simple content handler wrapper that delegates to a specified content
 * handler.  This a convenience class to provide an easy mechanism to override
 * only certain ContentHandler methods
 */
public class PassThruContentHandler implements ContentHandler {

    /**
     * The ContentHandler to which this class delegates.
     */
    private ContentHandler delegate;

    /**
     * Construct a new PassThrContentHandler
     * @param delegate The ContentHandler to which this class delegates.
     */
    public PassThruContentHandler(ContentHandler delegate) {
        this.delegate = delegate;
    }

    /**
     * Get the delegate content handler
     * @return the delegate ContentHandler
     */
    public ContentHandler getDelegate() {
        return delegate;
    }

    // javadoc inherited from interface
    public void characters(char[] chars, int i, int i1) throws SAXException {
        delegate.characters(chars, i, i1);
    }

    // javadoc inherited from interface
    public void endDocument() throws SAXException {
        delegate.endDocument();
    }

    // javadoc inherited from interface
    public void endElement(String s, String s1, String s2)
            throws SAXException {
        delegate.endElement(s, s1, s2);
    }

    // javadoc inherited from interface
    public void endPrefixMapping(String s) throws SAXException {
        delegate.endPrefixMapping(s);
    }

    // javadoc inherited from interface
    public void ignorableWhitespace(char[] chars, int i, int i1)
            throws SAXException {
        delegate.ignorableWhitespace(chars, i, i1);
    }

    // javadoc inherited from interface
    public void processingInstruction(String s, String s1)
            throws SAXException {
        delegate.processingInstruction(s, s1);
    }

    // javadoc inherited from interface
    public void setDocumentLocator(Locator locator) {
        delegate.setDocumentLocator(locator);
    }

    // javadoc inherited from interface
    public void skippedEntity(String s) throws SAXException {
        delegate.skippedEntity(s);
    }

    // javadoc inherited from interface
    public void startDocument() throws SAXException {
        delegate.startDocument();
    }

    // javadoc inherited from interface
    public void startElement(String s, String s1, String s2,
                             Attributes attributes) throws SAXException {
        delegate.startElement(s, s1, s2, attributes);
    }

    // javadoc inherited from interface
    public void startPrefixMapping(String s, String s1) throws SAXException {
        delegate.startPrefixMapping(s, s1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 ===========================================================================
*/
