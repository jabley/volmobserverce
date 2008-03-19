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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DelegatingContentHandler
    implements ContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ContentHandler old;

    private int oldDepth;

    private ContentHandler delegate;

    private int depth;

    protected Locator locator;

    protected void pushContentHandler(ContentHandler contentHandler) {
        if (old != null) {
            throw new IllegalStateException("Nested push not supported");
        }
        old = delegate;
        oldDepth = depth;
        delegate = contentHandler;
        depth = 1;
        //System.out.println ("ContentHandler: " + delegate);
        //System.out.println ("Depth: " + depth);
    }

    protected void popContentHandler() {
        delegate = old;
        depth = oldDepth;
        old = null;
        oldDepth = 0;

        //System.out.println ("ContentHandler: " + delegate);
        //System.out.println ("Depth: " + depth);
    }

    protected void enter() {
        depth += 1;
        //System.out.println ("Depth: " + depth);
    }

    protected void exit() {
        depth -= 1;
        //System.out.println ("Depth: " + depth);
        if (depth <= 0) {
            popContentHandler();
        }
    }

    public void characters(char[] chars, int i, int i1) throws SAXException {
        delegate.characters(chars, i, i1);
    }

    public void endDocument() throws SAXException {
        exit();
        delegate.endDocument();
    }

    public void endElement(String s, String s1, String s2) throws SAXException {
        //System.out.println("End: uri: " + s + " localName: " + s1 + " qName: " + s2);
        exit();
        delegate.endElement(s, s1, s2);
    }

    public void endPrefixMapping(String s) throws SAXException {
        exit();
        delegate.endPrefixMapping(s);
    }

    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {
        delegate.ignorableWhitespace(chars, i, i1);
    }

    public void processingInstruction(String s, String s1) throws SAXException {
        delegate.processingInstruction(s, s1);
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        delegate.setDocumentLocator(locator);
    }

    public void skippedEntity(String s) throws SAXException {
        delegate.skippedEntity(s);
    }

    public void startDocument() throws SAXException {
        enter();
        delegate.startDocument();
    }

    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes attributes)
        throws SAXException {

        //System.out.println("Start: uri: " + namespaceURI + " localName: " + localName + " qName: " + qName);
        enter();
        delegate.startElement(namespaceURI, localName, qName, attributes);
    }

    public void startPrefixMapping(String s, String s1) throws SAXException {
        enter();
        delegate.startPrefixMapping(s, s1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
